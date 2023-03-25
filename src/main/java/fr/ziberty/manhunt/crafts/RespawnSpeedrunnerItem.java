package fr.ziberty.manhunt.crafts;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

public class RespawnSpeedrunnerItem {
    public RespawnSpeedrunnerItem() {
        setRespawnItem();
    }

    private void setRespawnItem() {
        ItemStack itemStack = new ItemStack(Material.SEA_PICKLE);
        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName("§bCornichon sacré du respawn");
        itemMeta.addEnchant(Enchantment.PROTECTION_ENVIRONMENTAL, 1, false);
        itemMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemMeta.setLore(Arrays.asList("§7Permet de ressusciter un allié mort", "§7Est consommé une fois l'allié ressuscité"));
        itemStack.setItemMeta(itemMeta);

        ShapedRecipe shapedRecipe = new ShapedRecipe(itemStack);
        shapedRecipe.shape("OIO", "CDG", "ORO");
        shapedRecipe.setIngredient('O', Material.OBSIDIAN);
        shapedRecipe.setIngredient('I', Material.IRON_BLOCK);
        shapedRecipe.setIngredient('C', Material.COAL_BLOCK);
        shapedRecipe.setIngredient('D', Material.DIAMOND);
        shapedRecipe.setIngredient('G', Material.GOLD_BLOCK);
        shapedRecipe.setIngredient('R', Material.REDSTONE_BLOCK);
        Bukkit.getServer().addRecipe(shapedRecipe);
    }
}
