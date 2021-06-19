package us.rjks.utils;

import net.minecraft.server.v1_8_R3.EntityWitch;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import us.rjks.module.SpigotTimer;
import us.rjks.smash.GameManager;
import us.rjks.smash.GameState;
import us.rjks.smash.Main;

import java.util.HashSet;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 15.06.2021 / 11:22
 *
 **************************************************************************/

public class LobbyTimer extends SpigotTimer implements Listener {

    private Integer counter = 30;

    public LobbyTimer(Plugin instance, long numb1, long numb2) {
        super(instance, numb1, numb2);
    }

    @Override
    public void execute() {
        GameManager gameManager = Main.getGameManager();

        if (Bukkit.getOnlinePlayers().size() >= 2 && Bukkit.getOnlinePlayers().size() <= 8) {
            Bukkit.getOnlinePlayers().forEach(player -> {
                gameManager.getTitleManager().sendActionBar(player, "§aGame starts in " + counter + " seconds");
                if (counter == 0) {
                    Bukkit.broadcastMessage("§aThe Game Starts now");
                    gameManager.setGameState(GameState.INGAME);
                }
                counter--;
            });
        } else {
            counter = 30;
            Bukkit.getOnlinePlayers().forEach(player -> {
                gameManager.getTitleManager().sendActionBar(player, "§cThere have to be at least 2 Players to start the game");
            });
        }
        super.execute();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        event.setJoinMessage("§8[§a+§8] §7Player §2" + event.getPlayer().getName() + " §7joined the game");
        Main.getGameManager().getTabList().setTabList(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        event.setQuitMessage("§8[§c-§8] §7Player §4" + event.getPlayer().getName() + " §7left the game");
    }
}
