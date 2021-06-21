package us.rjks.map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import us.rjks.module.ModuleType;
import us.rjks.module.SpigotModule;

import java.io.File;
import java.util.ArrayList;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 15.06.2021 / 14:33
 *
 **************************************************************************/

public class MapManager extends SpigotModule {

    private File mapDirectory;
    private Requirement requirement;
    private ArrayList<Map> mapCache = new ArrayList<>();

    public MapManager(Plugin plugin, String directory, String name, ModuleType type, boolean autoCreate) {
        super(plugin, directory, name, type, autoCreate);
    }

    /**
     * This has to be called as the default constructor
     * */
    public void initMapManager(Requirement requirement) {
        this.mapDirectory = new File("plugins/" + getPlugin().getName() + "/maps/");
        this.requirement = requirement;
    }

    public void loadMaps() {
        int amount = 0;
        for (String map : getConfig().getConfigurationSection("").getKeys(false)) {
            Map createdMap = new Map(getPlugin(), getConfig().getString(map + ".ingame"), "plugin/" + map + "/", map);
            createdMap.setAuthor(getConfig().getString(map + ".author"));
            createdMap.setDescription(getConfig().getString(map + ".description"));

            //Properties
            for (String properties : getConfig().getConfigurationSection(map + ".property").getKeys(false)) {
                createdMap.setProperty(properties, getConfig().getString(map + ".property." + properties));
            }

            //Location Collection
            for (String collection : getConfig().getConfigurationSection(map + ".locationcollection").getKeys(false)) {
                Location location = new Location(Bukkit.getWorld(createdMap.getConfigName()), 0, 0, 0);
                location.setX(getConfig().getDouble(map + ".locationcollection." + collection + ".x"));
                location.setY(getConfig().getDouble(map + ".locationcollection." + collection + ".y"));
                location.setZ(getConfig().getDouble(map + ".locationcollection." + collection + ".z"));
                location.setYaw((float) getConfig().getDouble(map + ".locationcollection." + collection + ".yaw"));
                location.setPitch((float) getConfig().getDouble(map + ".locationcollection." + collection + ".pitch"));

                createdMap.addLocation(collection, location);
            }

            mapCache.add(createdMap);
            System.out.println("[MAP] Loaded " + amount + " maps");
        }
    }

    public boolean mapIsSetUp(Map map) {
        if (this.requirement == null) return true;

        for (String requirementRequirement : this.requirement.getPropertyRequirements()) {
            if (!map.hasProperty(requirementRequirement)) {
                return false;
            }
        }
        for (String requirementRequirement : this.requirement.getLocationRequirements()) {
            for(String name : map.getLocations().keySet()){
                boolean state = false;
                if (name.startsWith(requirementRequirement)) {
                    state = true;
                }
                if (!state) return false;
            }
        }

        for (String requirementRequirement : this.requirement.getLocationCollectionRequirements()) {
            for(String name : map.getLocations().keySet()){
                boolean state = false;
                if (name.startsWith(requirementRequirement)) {
                    state = true;
                }
                if (!state) return false;
            }
        }
        return true;
    }

    /**
     * Property
     * */

    public void setPropertyToMap(Map map, String name, Object object) {
        map.setProperty(name, object);

        String path = map.getConfigName() + ".property.";
        getConfig().set(path + "." + name, object);
    }

    /**
     * Normal Location
     * */

    public void setLocationToMap(Map map, String name, Location location) throws Exception {
        map.addLocation(name, location);

        String path = map.getConfigName() + ".location.";
        getConfig().set(path + "." + name + ".x", Double.valueOf(location.getX()));
        getConfig().set(path + "." + name + ".y", Double.valueOf(location.getY()));
        getConfig().set(path + "." + name + ".z", Double.valueOf(location.getZ()));
        getConfig().set(path + "." + name + ".yaw", Float.valueOf(location.getYaw()));
        getConfig().set(path + "." + name + ".pitch", Float.valueOf(location.getPitch()));
        getConfig().set(path + "." + name + ".world", map.getConfigFile().getName());

        save();
    }

    /**
     * Spawn Collection
     * */

    public void addLocationToCollection(Map map, String collection, Location location) throws Exception {
        int amount = getAmountOfSpawnCollection(map, collection);

        String path = map.getConfigName() + ".locationcollection.";
        getConfig().set(path + "." + collection + "-" + amount + ".x", Double.valueOf(location.getX()));
        getConfig().set(path + "." + collection + "-" + amount + ".y", Double.valueOf(location.getY()));
        getConfig().set(path + "." + collection + "-" + amount + ".z", Double.valueOf(location.getZ()));
        getConfig().set(path + "." + collection + "-" + amount + ".yaw", Float.valueOf(location.getYaw()));
        getConfig().set(path + "." + collection + "-" + amount + ".pitch", Float.valueOf(location.getPitch()));
        getConfig().set(path + "." + collection + "-" + amount + ".world", map.getConfigFile().getName());

        save();
    }

    private int getAmountOfSpawnCollection(Map map, String name) {
        int i = 0;
        try {
            for (String elements : getConfig().getConfigurationSection(map.getConfigName() + ".locationcollection").getKeys(false)) {
                if (elements.startsWith(name)) {
                    i++;
                }
            }
        } catch (Exception e) {}
        return i;
    }

    public Map getMapByName(String name) {
        for (Map map1 : mapCache) {
            if (map1.getConfigName().equalsIgnoreCase(name) || map1.getIngameName().equalsIgnoreCase(name)) {
                return map1;
            }
        }
        return null;
    }

    public void createMapDirectory() throws Exception {
        if (mapDirectory.exists()) mapDirectory.mkdirs();
    }

    public static class Requirement {

        public ArrayList<String> propertyRequirements = new ArrayList<>();
        public ArrayList<String> locationRequirements = new ArrayList<>();
        public ArrayList<String> locationCollectionRequirements = new ArrayList<>();

        public Requirement() {}

        public void addPropertyRequirement(String object) {
            propertyRequirements.add(object);
        }

        public void removePropertyRequirements(String object) {
            propertyRequirements.remove(object);
        }

        public void addLocationRequirement(String object) {
            locationRequirements.add(object);
        }

        public void removeLocationRequirements(String object) {
            locationRequirements.remove(object);
        }

        public void addLocationCollectionRequirement(String object) {
            locationCollectionRequirements.add(object);
        }

        public void removeLocationCollectionRequirements(String object) {
            locationCollectionRequirements.remove(object);
        }

        public ArrayList<String> getLocationRequirements() {
            return locationRequirements;
        }

        public ArrayList<String> getPropertyRequirements() {
            return propertyRequirements;
        }

        public ArrayList<String> getLocationCollectionRequirements() {
            return locationCollectionRequirements;
        }
    }
}
