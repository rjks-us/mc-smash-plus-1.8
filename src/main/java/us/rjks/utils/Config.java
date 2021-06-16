package us.rjks.utils;

import org.bukkit.ChatColor;
import org.bukkit.plugin.Plugin;
import us.rjks.module.ModuleType;
import us.rjks.module.SpigotModule;

import java.util.ArrayList;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 04.06.2021 / 14:51
 *
 **************************************************************************/

public class Config extends SpigotModule {

    public Config(Plugin plugin, String directory, String name, ModuleType type, boolean autoCreate) {
        super(plugin, directory, name, type, autoCreate);
    }

    public String getString(String name) {
        if (getCache().containsKey(name)) {
            return getCache().get(name).toString();
        }
        try {
            Object result = ChatColor.translateAlternateColorCodes('&', getConfig().getString(name));
            getCache().put(name, result);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public String getPermission(String name) {
        if (getCache().containsKey("permissions." + name)) {
            return getCache().get("permissions." + name).toString();
        }
        try {
            Object result = ChatColor.translateAlternateColorCodes('&', getConfig().getString("permissions." + name));
            getCache().put("permissions." + name, result);
            return result.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public boolean getBoolean(String name) {
        if (getCache().containsKey(name)) {
            return Boolean.valueOf(getCache().get(name).toString());
        }
        try {
            Object result = getConfig().getString(name);
            getCache().put(name, result);
            return getConfig().getBoolean(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Integer getInteger(String name) {
        if (getCache().containsKey(name)) {
            return Integer.getInteger(getCache().get(name).toString());
        }
        try {
            return (int)getConfig().get(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public Double getDouble(String name) {
        if (getCache().containsKey(name)) {
            return Double.parseDouble(getCache().get(name).toString());
        }
        try {
            return getConfig().getDouble(name);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0.0;
    }

    public ArrayList<String> getStringList(String name) {
        if (getCache().containsKey(name)) {
            return (ArrayList<String>)getCache().get(name);
        }
        try {
            return new ArrayList<>(getConfig().getStringList(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Integer> getIntegerList(String name) {
        if (getCache().containsKey(name)) {
            return (ArrayList<Integer>)getCache().get(name);
        }
        try {
            return new ArrayList<Integer>(getConfig().getIntegerList(name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
