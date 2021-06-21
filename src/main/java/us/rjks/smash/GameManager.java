package us.rjks.smash;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import us.rjks.functions.DoubleJump;
import us.rjks.listener.Join;
import us.rjks.listener.Quit;
import us.rjks.map.MapManager;
import us.rjks.module.ModuleType;
import us.rjks.module.SpigotTeam;
import us.rjks.utils.*;

import java.util.ArrayList;
import java.util.logging.Level;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 13.06.2021 / 14:23
 *
 **************************************************************************/

public class GameManager {

    private TitleManager titleManager;
    private Config config;
    private SpigotTeam team;
    private TabList tabList;
    private ItemManager itemManager;
    private MapManager mapManager;

    private Plugin plugin;

    private boolean active = false;

    private ArrayList<Player> ingame = new ArrayList<>();
    private ArrayList<Player> spectators = new ArrayList<>();

    private GameState gameState;
    private PluginManager pm;

    private LobbyTimer lobbyTimer;
    private IngameTimer ingameTimer;
    private EndTimer endTimer;

    public GameManager() throws Exception {
        this.plugin = Main.getInstance();
        this.titleManager = new TitleManager();
        this.pm = Bukkit.getPluginManager();

        this.config = new Config(plugin, plugin.getDataFolder() + "/", "config", ModuleType.YML, false);
        this.config.loadTemplate("config.yml");
        this.config.loadFile();

        /**
         * Map Manager
         * */
        MapManager.Requirement requirement = new MapManager.Requirement();
        requirement.addLocationRequirement("spawn");
        requirement.addPropertyRequirement("deathheight");

        this.mapManager = new MapManager(plugin, plugin.getDataFolder() + "/", "maps", ModuleType.YML, false);
        this.mapManager.createEmptyFile();
        this.mapManager.initMapManager(requirement);
        this.mapManager.loadMaps();

        /**
         * Team Manager
         * */
        this.team = new SpigotTeam(plugin, plugin.getDataFolder() + "/", "teams", ModuleType.YML, false);
        this.team.loadTemplate("teams.yml");
        this.team.loadFile();
        this.team.loadTeams();

        /**
         * TabList Manager
         * */
        this.tabList = new TabList(plugin, plugin.getDataFolder() + "/", "tablist", ModuleType.YML, false);
        this.tabList.loadTemplate("tablist.yml");
        this.tabList.loadFile();
        this.tabList.loadTabList();

        /**
         * Item Manager
         * */
        this.itemManager = new ItemManager(plugin, plugin.getDataFolder() + "/", "items", ModuleType.YML, false);
        this.itemManager.loadTemplate("items.yml");
        this.itemManager.loadFile();
        this.itemManager.loadItems();

        setGameState(GameState.LOBBY);

        this.active = true;
    }

    public void loadListeners() {
        pm.registerEvents(new Join(), plugin);
        pm.registerEvents(new Quit(), plugin);
    }

    public void reload() throws Exception {
        config.reloadFile();
        team.reloadFile();
        tabList.reloadFile();
    }

    public void disablePlugin() {
        HandlerList.unregisterAll();
        Bukkit.getPluginManager().disablePlugin(getPlugin());
        getPlugin().getLogger().log(Level.INFO, "The Plugin has been disabled");
    }

    /**
     * Enable/Disable functions on a current gameState
     * */
    public void setGameState(GameState gameState) {
        switch (gameState) {
            case LOBBY:
                this.lobbyTimer = new LobbyTimer(this.plugin, 20, 20);
                this.pm.registerEvents(this.lobbyTimer, plugin);
                this.pm.registerEvents(this.itemManager, plugin);
                this.lobbyTimer.start();
                break;
            case INGAME:
                this.lobbyTimer.stop();
                this.ingameTimer = new IngameTimer(this.plugin, 20, 20);
                HandlerList.unregisterAll(this.lobbyTimer);
                this.pm.registerEvents(ingameTimer, plugin);
                this.ingameTimer.start();

                pm.registerEvents(new DoubleJump(), plugin);
            break;
            case END:
                this.ingameTimer.stop();
                this.endTimer = new EndTimer(this.plugin, 20, 20);
                HandlerList.unregisterAll(this.ingameTimer);
                this.pm.registerEvents(this.endTimer, plugin);
                this.endTimer.start();

                //Disable Flying for all online players
                Bukkit.getOnlinePlayers().forEach(player -> {
                    player.setFlying(false);
                });

                HandlerList.unregisterAll(new DoubleJump());
            break;
        }

        this.gameState = gameState;
    }

    public PluginManager getPluginManager() {
        return pm;
    }

    public GameState getGameState() {
        return gameState;
    }

    public TabList getTabList() {
        return tabList;
    }

    public MapManager getMapManager() {
        return mapManager;
    }

    public ItemManager getItemManager() {
        return itemManager;
    }

    public boolean isActive() {
        return active;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public Config getConfig() {
        return config;
    }

    public SpigotTeam getTeam() {
        return team;
    }

    public TitleManager getTitleManager() {
        return titleManager;
    }
}
