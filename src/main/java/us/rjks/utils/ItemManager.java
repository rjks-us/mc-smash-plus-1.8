package us.rjks.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import us.rjks.module.ModuleType;
import us.rjks.module.SpigotHologram;
import us.rjks.module.SpigotModule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Level;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 19.06.2021 / 13:23
 *
 **************************************************************************/

public class ItemManager extends SpigotModule implements Listener {

    private ArrayList<Location> locations = new ArrayList<>();

    private ArrayList<Item> items = new ArrayList<>();
    private ArrayList<LivingItem> livingItems = new ArrayList<>();

    public ItemManager(Plugin plugin, String directory, String name, ModuleType type, boolean autoCreate) {
        super(plugin, directory, name, type, autoCreate);
    }

    public void loadItems() {
        int amount = 0;
        for (String item : getConfig().getConfigurationSection("").getKeys(false)) {
            Item item1 = new Item(ChatColor.translateAlternateColorCodes('&', getConfig().getString(item + ".name")), Material.valueOf(getConfig().getString(item + ".material")));
            item1.setDisplayname(ChatColor.translateAlternateColorCodes('&', getConfig().getString(item + ".display")));
            item1.setDescription(ChatColor.translateAlternateColorCodes('&', getConfig().getString(item + ".description")));
            items.add(item1);

            amount++;
        }
        getPlugin().getLogger().log(Level.INFO, "[ITEM] Loaded " + amount + " items");
    }

    public void addLocation(Location location) {
        locations.add(location);
    }

    public void addLocation(ArrayList<Location> location) {
        for (Location location1 : location) {
            locations.add(location1);
        }
    }

    public void spawnRandomItem() {
        if (livingItems.size() == locations.size()) return;
        ArrayList<Location> temp = new ArrayList<>();
        for (LivingItem livingItem : livingItems) {
            temp.add(livingItem.getLocation());
        }

        Location location = temp.get(new Random().nextInt(temp.size()));
        Item item = items.get(new Random().nextInt(items.size()));

        org.bukkit.entity.Item drop = location.getWorld().dropItem(location, new ItemStack(item.getMaterial()));

        livingItems.add(new LivingItem(item, location, drop));
    }

    public void spawnRandomItemToLocation(Location location) {
        if (livingItems.size() == locations.size()) return;
        ArrayList<Location> temp = new ArrayList<>();
        for (LivingItem livingItem : livingItems) {
            temp.add(livingItem.getLocation());
        }

        Item item = items.get(new Random().nextInt(items.size()));

        double y = location.getY();
        org.bukkit.entity.Item drop = location.getWorld().dropItem(location, new ItemStack(item.getMaterial()));

        /**
         * This is needed due of the item drop animation, we can't cancel it but we are teleporting the item after it.
         * */
        Bukkit.getScheduler().scheduleSyncDelayedTask(getPlugin(), new Runnable() {
            @Override
            public void run() {
                location.setY(y);
                drop.teleport(location);
            }
        }, 5);

        livingItems.add(new LivingItem(item, location, drop));
    }

    public void removeAllItems() {
        for (LivingItem livingItem : livingItems) {
            livingItem.remove();
        }
    }

    public void showHologramsToPlayer(Player player) {
        for (LivingItem livingItem : livingItems) {
            livingItem.spigotHologram.show(player);
        }
    }

    /**
     * Only executed when listener is registered.
     * */
    @EventHandler
    public void onPickUp(PlayerPickupItemEvent event) {
        LivingItem livingItemm = null;
        for (LivingItem livingItem : livingItems) {
            if (livingItem.getDroppedItem().equals(event.getItem())) {
                livingItemm = livingItem;
                Item item = livingItem.getItem();
                livingItem.remove();
                event.getPlayer().setDisplayName("You Picked up the " + item.getName() + " item");
                event.getPlayer().getInventory().addItem(new ItemStack(item.getMaterial()));
                event.setCancelled(true);
            }
        }
        livingItems.remove(livingItemm);
    }

    private class Item {

        public Material material;
        public String name, displayname, description;

        public Item(String name, Material material) {
            this.name = name;
            this.material = material;
            this.displayname = name;
        }

        public void setDisplayname(String displayname) {
            this.displayname = displayname;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getName() {
            return name;
        }

        public String getDescription() {
            return description;
        }

        public String getDisplayname() {
            return displayname;
        }

        public Material getMaterial() {
            return material;
        }
    }

    private class LivingItem {

        private ItemManager.Item item;
        private org.bukkit.entity.Item droppedItem;
        private SpigotHologram spigotHologram;
        private Location location;

        public LivingItem(ItemManager.Item item, Location location, org.bukkit.entity.Item droppedItem) {
            this.item = item;
            this.location = location;
            this.droppedItem = droppedItem;

            this.spigotHologram = new SpigotHologram(getPlugin(), location);
            this.spigotHologram.addLine(item.getName());
            this.spigotHologram.setSmall(true);
            this.spigotHologram.load();
            this.spigotHologram.showForAll();
        }

        public void remove() {
            this.droppedItem.remove();
            this.spigotHologram.hideForAll();
        }

        public void setDroppedItem(org.bukkit.entity.Item droppedItem) {
            this.droppedItem = droppedItem;
        }

        public org.bukkit.entity.Item getDroppedItem() {
            return droppedItem;
        }

        public Location getLocation() {
            return location;
        }

        public ItemManager.Item getItem() {
            return item;
        }

        public SpigotHologram getSpigotHologram() {
            return spigotHologram;
        }
    }
}
