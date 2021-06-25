package us.rjks.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import us.rjks.map.Map;
import us.rjks.smash.GameManager;
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
                    Main.getGameManager().getItemManager().addLocation(((Player) sender).getLocation());
                    Main.getGameManager().getItemManager().spawnRandomItemToLocation(((Player) sender).getLocation());
                }
            } else if (args.length == 4) {
                /**
                 * Map Manager
                 * */
                if (args[0].equalsIgnoreCase("map")) {
                    if (args[1].equalsIgnoreCase("create")) {
                        try {
                            Map map = Main.getGameManager().getMapManager().createMap(args[2], args[3]);
                            if (map != null) {
                                sender.sendMessage("Successfully created Map " + args[2] + " AS " + args[3]);
                                map.loadMap();
                                map.teleportPlayerToWorld(((Player) sender).getPlayer());
                            } else {
                                sender.sendMessage("This Map could not be found in the Map directory");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            sender.sendMessage("An error accoured");
                        }
                    } else if (args[1].equalsIgnoreCase("addProp")) {
                        try {
                            Map map = Main.getGameManager().getMapManager().getMapByName(args[2]);
                            if (map != null) {
                                Main.getGameManager().getMapManager().addPropertyToMap(map, args[3], args[3]);
                                sender.sendMessage("You added Property " + args[3] + " to " + map.getConfigName());
                            } else {
                                sender.sendMessage("This Map could not be found in the Map directory");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            sender.sendMessage("An error accoured");
                        }
                    } else if (args[1].equalsIgnoreCase("addLocCol")) {
                        try {
                            Map map = Main.getGameManager().getMapManager().getMapByName(args[2]);
                            if (map != null) {
                                Main.getGameManager().getMapManager().addLocationToCollection(map, args[3], ((Player) sender).getLocation());
                                sender.sendMessage("You added your current Location to Collection " + args[3] + " on map " + map.getConfigName());
                            } else {
                                sender.sendMessage("This Map could not be found in the Map directory");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            sender.sendMessage("An error accoured");
                        }
                    } else if (args[1].equalsIgnoreCase("addLoc")) {
                        try {
                            Map map = Main.getGameManager().getMapManager().getMapByName(args[2]);
                            if (map != null) {
                                Main.getGameManager().getMapManager().addLocationToMap(map, args[3], ((Player) sender).getLocation());
                                sender.sendMessage("You added your current Location as " + args[3] + " on map " + map.getConfigName());
                            } else {
                                sender.sendMessage("This Map could not be found in the Map directory");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            sender.sendMessage("An error accoured");
                        }
                    }
                }
            } else if (args.length == 3) {
                if (args[0].equalsIgnoreCase("map")) {
                    System.out.println(args[1]);
                    System.out.println(Main.getGameManager());
                    System.out.println(Main.getGameManager().getMapManager());
                    if (Main.getGameManager().getMapManager().getMapByName(args[1]) != null) {
                        Map map = Main.getGameManager().getMapManager().getMapByName(args[1]);
                        if (args[2].equalsIgnoreCase("load")) {
                            if (!map.isLoaded()) {
                                try {
                                    map.loadMap();
                                    sender.sendMessage("Successfully loaded Map " + args[0]);
                                    if (!Main.getGameManager().getMapManager().mapIsSetUp(map)) {
                                        sender.sendMessage("This map is not set Up right");
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    sender.sendMessage("Map cannot be loaded");
                                }
                            } else {
                                sender.sendMessage("This map is already loaded");
                            }
                        } else if (args[2].equalsIgnoreCase("unload")) {
                            if (map.isLoaded()) {
                                try {
                                    map.unloadMap();
                                    sender.sendMessage("Successfully unloaded Map " + args[0]);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    sender.sendMessage("Map cannot be loaded");
                                }
                            } else {
                                sender.sendMessage("This map is not loaded");
                            }
                        } else if (args[2].equalsIgnoreCase("tp") || args[2].equalsIgnoreCase("teleport")) {
                            if (map.isLoaded()) {
                                map.teleportPlayerToWorld(((Player) sender).getPlayer());
                                sender.sendMessage("You are now on world " + map.getConfigName());
                            } else {
                                sender.sendMessage("This map is not loaded");
                            }
                        }
                    } else {
                        sender.sendMessage("The Map " + args[1] + " cannot be found");
                    }
                }
            }
        }
        return false;
    }
}
