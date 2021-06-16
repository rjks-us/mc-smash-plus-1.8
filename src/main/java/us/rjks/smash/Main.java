package us.rjks.smash;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

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
        } catch (Exception e) {
            System.out.println("[INFO] Fatal error while loading the plugin");
            e.printStackTrace();
            System.out.println("[INFO] Plugin disabling due of the fatal error");
            Bukkit.getPluginManager().disablePlugin(this);
        }

        super.onEnable();
    }

    @Override
    public void onDisable() {
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
