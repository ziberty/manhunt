package fr.ziberty.manhunt.utils;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Collections;

public class ItemStackHelper {

    public ItemStack getItemStack(Material material, String name, String description, int qty) {
        ItemStack itemStack = new ItemStack(material, qty);
        itemStack.setItemMeta(getItemMeta(itemStack, name, description));
        return itemStack;
    }

    private ItemMeta getItemMeta(ItemStack itemStack, String name, String description) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(Collections.singletonList(description));
        return meta;
    }

    public ItemStack getHeadItem(Player player, String name, String description, int qty) {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, qty);
        SkullMeta skull = (SkullMeta) item.getItemMeta();
        skull.setDisplayName(name);
        skull.setLore(Collections.singletonList(description));
        skull.setOwningPlayer(player);
        item.setItemMeta(skull);
        return item;
    }

}
