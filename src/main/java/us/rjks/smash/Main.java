package us.rjks.smash;

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
        gameManager = new GameManager();

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

    public static GameManager getPlugin() {
        return gameManager;
    }
}
