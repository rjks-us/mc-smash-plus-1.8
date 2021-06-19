package us.rjks.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import us.rjks.smash.Main;
import us.rjks.utils.ItemManager;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 19.06.2021 / 14:38
 *
 **************************************************************************/

public class Smash implements CommandExecutor {

    private Plugin plugin;

    public Smash(Plugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {
            if (args.length == 1) {
                if (args[0].equalsIgnoreCase("reload")) {
                    try {
                        Main.getGameManager().reload();
                        sender.sendMessage("You reloaded the Plugin");
                    } catch (Exception e) {
                        e.printStackTrace();
                        sender.sendMessage("There was an error while reloading the plugin, check the console");
                    }
                } else if (args[0].equalsIgnoreCase("spawn")) {
                    Main.getGameManager().getPluginManager().registerEvents(Main.getGameManager().getItemManager(), plugin);
                    Main.getGameManager().getItemManager().addLocation(((Player) sender).getLocation());
                    Main.getGameManager().getItemManager().spawnRandomItemToLocation(((Player) sender).getLocation());
                }
            }
        }
        return false;
    }
}
