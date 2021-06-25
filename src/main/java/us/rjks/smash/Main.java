package us.rjks.smash;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import us.rjks.cmd.Smash;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 13.06.2021 / 14:22
 *
 **************************************************************************/

public class Main extends JavaPlugin {

    private static GameManager gameManager;
    private static Main instance;

    @Override
    public void onEnable() {
        instance = this;
        try {
            gameManager = new GameManager();
            gameManager.loadListeners();

            /**
             * Commands are relying on the GameManager so it has to be registered after the GameManager
             * */
            getCommand("smash").setExecutor(new Smash(this));
        } catch (Exception e) {
            System.out.println("[INFO] Fatal error while loading the plugin");
            e.printStackTrace();
            System.out.println("[INFO] Plugin disabling due of the fatal error");
            gameManager.disablePlugin();
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        try {
            getGameManager().getMapManager().disable();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onDisable();
    }

    @Override
    public void onLoad() {
        super.onLoad();
    }

    public static Main getInstance() {
        return instance;
    }

    public static GameManager getGameManager() {
        return gameManager;
    }

    public static Main getPlugin() {
        return instance;
    }
}
