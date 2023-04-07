package fr.ziberty.manhunt.inventories;

import fr.ziberty.manhunt.Manhunt;
import fr.ziberty.manhunt.utils.ItemStackHelper;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class RespawnItemInventory {

    private Manhunt main;

    private Inventory inventory;

    public Inventory getInventory() {
        return inventory;
    }

    private void setInventory(Inventory inventory) {
        this.inventory = buildInventory(inventory);
    }

    public RespawnItemInventory(Manhunt manhunt) {
        this.main = manhunt;
        setInventory(Bukkit.createInventory(null, 54, "Ressusciter un allié"));
    }

    private Inventory buildInventory(Inventory inventory) {
        ItemStackHelper itemStackHelper = new ItemStackHelper();
        inventory.setItem(49, itemStackHelper.getItemStack(Material.BARRIER, "§cAnnuler", null, 1));
        int index = 0;
        for (Player player : main.getSpeedrunnersList()) {
            if (player.getGameMode() != GameMode.SPECTATOR) continue;
            inventory.setItem(index, itemStackHelper.getHeadItem(player, player.getName(), null, 1));
            index++;
        }
        return inventory;
    }
}
