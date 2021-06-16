package us.rjks.utils;

import net.minecraft.server.v1_8_R3.IChatBaseComponent;
import net.minecraft.server.v1_8_R3.PacketPlayOutChat;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

/***************************************************************************
 *
 *  Urheberrechtshinweis
 *  Copyright Ⓒ Robert Kratz 2021
 *  Erstellt: 04.06.2021 / 18:41
 *
 **************************************************************************/

public class TitleManager {

    public void sendActionBar(Player player, String message) {
        try {
            CraftPlayer p = (CraftPlayer)player;
            IChatBaseComponent cbc = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");
            PacketPlayOutChat ppoc = new PacketPlayOutChat(cbc, (byte)2);
            p.getHandle().playerConnection.sendPacket(ppoc);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitle(Player player, String text, String sub) {
        player.sendTitle(text, sub);
    }


}
