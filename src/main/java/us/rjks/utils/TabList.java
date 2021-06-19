package us.rjks.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import us.rjks.module.ModuleType;
import us.rjks.module.SpigotModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 04.06.2021 / 18:02
 *
 **************************************************************************/

public class TabList extends SpigotModule {

    private Scoreboard scoreboard;
    private ArrayList<Rank> ranks = new ArrayList<Rank>();
    private HashMap<Player, Rank> cache = new HashMap<>();

    public TabList(Plugin plugin, String directory, String name, ModuleType type, boolean autoCreate) {
        super(plugin, directory, name, type, autoCreate);
    }

    public Rank getDefaultRank() {
        for (Rank all : ranks) {
            if (all.isDefaultRank()) {
                return all;
            }
        }
        return null;
    }

    public Rank getRankByName(String name) {
        for (Rank rank : ranks) {
            if (rank.getName().equalsIgnoreCase(name)) {
                return rank;
            }
        }

        return getDefaultRank();
    }

    public Rank getRankByPlayer(Player player) {
        if(cache.containsKey(player)) {
            return cache.get(player);
        }
        System.out.println();
        if(getDefaultRank() == null) {
            getPlugin().getLogger().log(Level.WARNING, "THERE IS NOT DEFAULT RANK SET, LOCATE THE tablist.yml AND SET THE DEFAULT ROLE TO default > true");
        }
        return getDefaultRank();
    }

    public void loadTabList() {
        scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();

        for (String rank : getConfig().getConfigurationSection("").getKeys(false)) {
            ranks.add(new Rank(rank,
                    getConfig().getString(rank + ".permission"),
                    getConfig().getString(rank + ".color"),
                    getConfig().getString(rank + ".tabprefix"),
                    getConfig().getString(rank + ".tabsuffix"),
                    getConfig().getString(rank + ".chatformat"),
                    getConfig().getString(rank + ".hex"),
                    getConfig().getInt(rank + ".power"),
                    getConfig().getBoolean(rank + ".defaultRank")
            ));
        }

        int amount = 0;
        for (Rank rank : ranks) {
            Team team = scoreboard.registerNewTeam((100000 - rank.getPower()) + rank.getName());
            team.setPrefix(ChatColor.translateAlternateColorCodes('&', rank.getTabprefix()));
            team.setSuffix(ChatColor.translateAlternateColorCodes('&', rank.getTabsuffix()));
            amount++;
        }
        getPlugin().getLogger().log(Level.INFO, "[RANK] Loaded " + amount + " ranks");
    }

    public synchronized void setTabList(Player player) {

        Rank team = null;

        for (Rank rank : ranks) {
            if (player.hasPermission(rank.getPermission())) {
                team = rank;
            }
        }

        if(getDefaultRank() != null) {
            if(team == null) team = getDefaultRank();

            scoreboard.getTeam((100000 - team.getPower()) + team.getName()).addEntry(player.getName());
            player.setDisplayName(scoreboard.getTeam((100000 - team.getPower()) + team.getName()).getPrefix() + player.getName() + "§r");

            cache.remove(player);
            cache.put(player, team);
            for (Player all : Bukkit.getOnlinePlayers()) {
                all.setScoreboard(scoreboard);
            }
        } else {
            getPlugin().getLogger().log(Level.WARNING, "THERE IS NOT DEFAULT RANK SET, LOCATE THE tablist.yml AND SET THE DEFAULT ROLE TO default > true");
        }
    }

    public static class Rank {

        private String name, permission, color, tabprefix, tabsuffix, chatformat, hex;
        private boolean defaultRank;
        private Integer power;

        public Rank(String name, String permission, String color, String tabprefix, String tabsuffix, String chatformat, String hex, Integer power, boolean defaultRank) {
            this.name = name;
            this.permission = permission;
            this.color = color;
            this.tabprefix = tabprefix;
            this.tabsuffix = tabsuffix;
            this.chatformat = chatformat;
            this.defaultRank = defaultRank;
            this.hex = hex;
            this.power = power;
        }

        public String getHex() {
            return hex;
        }

        public String getName() {
            return name;
        }

        public String getChatformat() {
            return ChatColor.translateAlternateColorCodes('&', chatformat);
        }

        public String getColor() {
            return color;
        }

        public String getPermission() {
            return permission;
        }

        public String getTabprefix() {
            return ChatColor.translateAlternateColorCodes('&', tabprefix);
        }

        public String getTabsuffix() {
            return ChatColor.translateAlternateColorCodes('&', tabsuffix);
        }

        public Integer getPower() {
            return power;
        }

        public boolean isDefaultRank() {
            return defaultRank;
        }
    }
}
