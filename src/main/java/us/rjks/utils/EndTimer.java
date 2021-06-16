package us.rjks.utils;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerLoginEvent;
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
 *  Erstellt: 15.06.2021 / 11:26
 *
 **************************************************************************/

public class EndTimer extends SpigotTimer implements Listener {

    private Integer counter = 10;

    public EndTimer(Plugin instance, long numb1, long numb2) {
        super(instance, numb1, numb2);
    }

    @Override
    public void execute() {
        GameManager gameManager = Main.getGameManager();

        Bukkit.getOnlinePlayers().forEach(player -> {
            gameManager.getTitleManager().sendActionBar(player, "§cThe Server restarts in " + counter + " seconds");
        });
        if (counter == 0) {
            Bukkit.broadcastMessage("§cThe Server Restarts now");
            //Bukkit.getServer().reload();
            this.stop();
        }

        counter--;

        super.execute();
    }

    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        event.disallow(PlayerLoginEvent.Result.KICK_OTHER, "The Server is currently restarting.");
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("§8[§c-§8] §7Player §4" + event.getPlayer().getName() + " §7left the game");
    }
}
