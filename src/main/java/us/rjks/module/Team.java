package us.rjks.module;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 16.06.2021 / 17:00
 *
 **************************************************************************/

public class Team {

    private String name, prefix, suffix, chatformat, tabformat, color;

    private ArrayList<Player> players = new ArrayList<>();
    private HashMap<String, Object> props = new HashMap<>();

    private int maxPlayer = 0;

    public Team(String name) {
        this.name = name;
    }

    public Team(String name, String prefix, String suffix, String chatformat, String tabformat, String color, Integer member) {
        this.name = name;
        this.prefix = prefix;
        this.suffix = suffix;
        this.chatformat = chatformat;
        this.tabformat = tabformat;
        this.color = color;
        this.maxPlayer = member;
    }

    public boolean addPlayerToTeam(Player player) {
        if (isFull()) return false;
        if (players.contains(player)) return false;
        players.add(player);
        return true;
    }

    public boolean playerInTeam(Player player) {
        return players.contains(player);
    }

    public void removePlayer(Player player) {
        players.remove(player);
    }

    public boolean isEmpty() {
        return players.isEmpty();
    }

    public boolean isFull() {
        return (players.size() >= maxPlayer);
    }

    public void addProperty(String key, Object value) {
        props.put(key, value);
    }

    public void removeProperty(String key) {
        props.remove(key);
    }

    public boolean hasProperty(String key) {
        return props.containsKey(key);
    }

    public void clearProperties() {
        props.clear();
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public String getName() {
        return name;
    }

    public String getColor() {
        return color;
    }

    public String getChatformat() {
        return chatformat;
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getTabformat() {
        return tabformat;
    }

    public void setChatformat(String chatformat) {
        this.chatformat = chatformat;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public void setMaxPlayer(int maxPlayer) {
        this.maxPlayer = maxPlayer;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setProps(HashMap<String, Object> props) {
        this.props = props;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public void setTabformat(String tabformat) {
        this.tabformat = tabformat;
    }
}
