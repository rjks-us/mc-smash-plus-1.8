package us.rjks.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import us.rjks.module.SpigotTimer;
import us.rjks.smash.GameManager;
import us.rjks.smash.GameState;
import us.rjks.smash.Main;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 15.06.2021 / 11:25
 *
 **************************************************************************/

public class IngameTimer extends SpigotTimer implements Listener {

    private Integer counter = 500;

    public IngameTimer(Plugin instance, long numb1, long numb2) {
        super(instance, numb1, numb2);
    }

    @Override
    public void execute() {
        GameManager gameManager = Main.getGameManager();

        Bukkit.getOnlinePlayers().forEach(player -> {
            gameManager.getTitleManager().sendActionBar(player, "§cThe Game ends in " + counter + " seconds");
        });
        if (counter == 0) {
            Bukkit.broadcastMessage("§cThe Game ends now");
            gameManager.setGameState(GameState.END);
        }

        counter--;

        super.execute();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {

    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {

    }
}
