package fr.ziberty.manhunt.inventories;

import fr.ziberty.manhunt.Manhunt;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class InventoryListener implements Listener {
    private Manhunt main;
    public InventoryListener(Manhunt manhunt) {
        this.main = manhunt;
    }
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        Inventory inventory = event.getInventory();
        Player player = (Player) event.getWhoClicked();
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null) return;
        ItemMeta itemMeta = itemStack.getItemMeta();
        if (itemMeta == null) return;

        if (event.getView().getTitle().equalsIgnoreCase("Configuration Manhunt")) {
            event.setCancelled(true);
            switch (itemMeta.getDisplayName()) {
                case "§bSpeedrunners":
                    player.openInventory(new SpeedrunnersInventory(main).getInventory());
                    break;
                case "§aCommencer":
                    //TODO: lancer la partie
                    break;
            }
        } else if (event.getView().getTitle().equalsIgnoreCase("Speedrunners")) {
            event.setCancelled(true);
            switch (itemStack.getType()) {
                case PLAYER_HEAD:
                    Player itemPlayer = Bukkit.getPlayer(itemMeta.getDisplayName());
                    if (main.isPlayerSpeedrunner(player)) {
                        main.getSpeedrunnersList().remove(player);
                    } else {
                        main.getSpeedrunnersList().add(player);
                    }
                    player.openInventory(new SpeedrunnersInventory(main).getInventory());
                    break;
                case BARRIER:
                    player.openInventory(main.getConfigInventory().getInventory());
                    break;
            }
        }
    }
}
