package us.rjks.module;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 16.06.2021 / 16:59
 *
 **************************************************************************/

public class SpigotTeam extends SpigotModule implements Listener {

    private ArrayList<Team> teams = new ArrayList<>();

    private boolean friendlyFire = false;

    public SpigotTeam(Plugin plugin, String directory, String name, ModuleType type, boolean autoCreate) {
        super(plugin, directory, name, type, autoCreate);
    }

    public void loadTeams() throws Exception {
        for (String team : getConfig().getConfigurationSection("").getKeys(false)) {
            teams.add(new Team(getConfig().getString(""),
                    getConfig().getString(team + ".prefix"),
                    getConfig().getString(team + ".suffix"),
                    getConfig().getString(team + ".chatformat"),
                    getConfig().getString(team + ".tabformat"),
                    getConfig().getString(team + ".color"),
                    getConfig().getInt(team + ".members")));
        }
    }

    public Team getTeamByName(String name) {
        for (Team team : teams) {
            if (team.getName().equalsIgnoreCase(name)) {
                return team;
            }
        }
        return null;
    }

    public ArrayList<Team> getTeamsFull() {
        ArrayList<Team> teams = new ArrayList<>();
        for (Team team : teams) {
            if (!team.isEmpty()) {
                teams.add(team);
            }
        }
        return teams;
    }

    public Team getTeamByPlayer(Player player) {
        for (Team team : teams) {
            if (team.playerInTeam(player)) {
                return team;
            }
        }
        return null;
    }

    public Team addPlayerToRandomTeam(Player player) {
        Team lowest = teams.get(0);
        for (Team team : teams) {
            if(lowest.getPlayers().size() < team.getPlayers().size()) {
                lowest = team;
            }
        }
        lowest.addPlayerToTeam(player);
        return lowest;
    }

    public ArrayList<Team> getTeams() {
        return teams;
    }

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
    }

    @EventHandler(priority = EventPriority.LOW)
    public void onDamage(EntityDamageByEntityEvent event) {
        if (friendlyFire) {
            if (event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
                Team entityTeam = getTeamByPlayer(((Player) event.getEntity()).getPlayer()), damagerTeam = getTeamByPlayer(((Player) event.getDamager()).getPlayer());
                if (entityTeam.equals(damagerTeam)) {
                    event.setCancelled(true);
                }
            }
        }
    }
}
