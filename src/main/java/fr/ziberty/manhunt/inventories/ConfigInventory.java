package fr.ziberty.manhunt.inventories;

import fr.ziberty.manhunt.utils.ItemStackHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;

public class ConfigInventory {

    public Inventory getInventory() {
        return inventory;
    }

    private void setInventory(Inventory inventory) {
        this.inventory = buildInventory(inventory);
    }

    private Inventory inventory;

    public ConfigInventory() {
        setInventory(Bukkit.createInventory(null, 27, "Configuration Manhunt"));
    }

    private Inventory buildInventory(Inventory inventory) {
        ItemStackHelper itemStackHelper = new ItemStackHelper();
        inventory.setItem(2, itemStackHelper.getItemStack(Material.DRAGON_EGG, "§bSpeedrunners", "§7Choisir les speedrunners", 1));
        inventory.setItem(6, itemStackHelper.getItemStack(Material.GREEN_CONCRETE, "§aCommencer", "", 1));
        return inventory;
    }

}
