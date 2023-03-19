package fr.ziberty.manhunt.inventories;

import fr.ziberty.manhunt.Manhunt;
import fr.ziberty.manhunt.utils.ItemStackHelper;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SpeedrunnersInventory {

    private Manhunt main;

    public Inventory getInventory() {
        return inventory;
    }

    private void setInventory(Inventory inventory) {
        this.inventory = buildInventory(inventory);
    }

    private Inventory inventory;

    public SpeedrunnersInventory(Manhunt manhunt) {
        this.main = manhunt;
        setInventory(Bukkit.createInventory(null, 54, "Speedrunners"));
    }

    private Inventory buildInventory(Inventory inventory) {
        ItemStackHelper itemStackHelper = new ItemStackHelper();
        inventory.setItem(49, itemStackHelper.getItemStack(Material.BARRIER, "§cRetour", "", 1));
        int index = 0;
        for (Player player : Bukkit.getOnlinePlayers()) {
            String description = "§4Hunter";
            if (main.isPlayerSpeedrunner(player)) description = "§2Speedrunner";
            inventory.setItem(index, itemStackHelper.getHeadItem(player, player.getName(), description, 1));
            index++;
        }
        return inventory;
    }

}
