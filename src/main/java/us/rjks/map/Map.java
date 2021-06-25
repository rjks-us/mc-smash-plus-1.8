package us.rjks.map;

import org.apache.commons.io.FileUtils;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 15.06.2021 / 14:34
 *
 **************************************************************************/

public class Map {

    private String ingameName, configName, author, description, directory;
    private Material icon;
    private boolean loaded;
    private HashMap<String, Object> property = new HashMap<>();
    private HashMap<String, Location> locations = new HashMap<>();

    private Plugin plugin;

    private File configFile, loadedFile;

    public Map(Plugin plugin, String ingameName, String directory, String configName) {
        this.ingameName = ingameName;
        this.configName = configName;
        this.directory = directory;

        this.plugin = plugin;
        this.loaded = false;

        //Check if World is already in plugin directory
        for (World world : Bukkit.getWorlds()) {
            if (world.getName().equalsIgnoreCase(this.configName)) this.loaded = true;
        }

        this.configFile = new File(directory + this.configName);
        this.loadedFile = new File("plugins/../" + this.configName);
    }

    public boolean loadMap() throws Exception {
        if(!isLoaded()) {
            if(!loadedFile.exists()) {
                loadedFile.mkdirs();
                FileUtils.copyDirectory(configFile, loadedFile);
                World world = new WorldCreator(this.configName).createWorld();
                setLoaded(true);
                return true;
            }
        }
        return false;
    }

    public boolean unloadMap() throws Exception {
        if (isLoaded()) {
            for (World world : Bukkit.getWorlds()) {
                if(world.getName().equalsIgnoreCase(configName)) {
                    for (Player all : world.getPlayers()) {
                        all.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                    }

                    Bukkit.unloadWorld(world, true);
                    FileUtils.deleteDirectory(this.loadedFile);
                    setLoaded(false);
                    return true;
                }
            }
        }
        return false;
    }

    public void addLocation(String name, Location location) {
        locations.put(name, location);
    }

    public void removeLocation(String name) {
        locations.remove(name);
    }

    public Location getLocation(String name) {
        return locations.get(name);
    }

    public Location getRandomLocation() {
        if (locations.size() == 0) return null;
        return locations.get(new Random().nextInt(locations.size()));
    }

    public void setProperty(String key, Object value) {
        property.put(key, value);
    }

    public Object getProperty(String key) {
        return property.get(key);
    }

    public boolean hasProperty(String key) {
        return (property.get(key) != null);
    }

    public boolean teleportPlayerToWorld(Player player) {
        if (isLoaded()) {
            player.teleport(Bukkit.getWorld(this.configName).getSpawnLocation());
            return true;
        }
        return false;
    }

    public boolean isLoaded() {
        return loaded;
    }

    public String getIngameName() {
        return ingameName;
    }

    public String getDescription() {
        return description;
    }

    public Plugin getPlugin() {
        return plugin;
    }

    public String getDirectory() {
        return directory;
    }

    public File getConfigFile() {
        return configFile;
    }

    public File getLoadedFile() {
        return loadedFile;
    }

    public HashMap<String, Object> getProperty() {
        return property;
    }

    public Material getIcon() {
        return icon;
    }

    public String getAuthor() {
        return author;
    }

    public String getConfigName() {
        return configName;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setIcon(Material icon) {
        this.icon = icon;
    }

    public HashMap<String, Location> getLocations() {
        return locations;
    }
}
