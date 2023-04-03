package fr.ziberty.manhunt.listeners;

import fr.ziberty.manhunt.Manhunt;
import fr.ziberty.manhunt.inventories.RespawnItemInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class RespawnItemListener implements Listener {
    Manhunt main;
    public RespawnItemListener(Manhunt manhunt) {
        this.main = manhunt;
    }

    @EventHandler
    public void onRespawnItemClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack itemStack = event.getItem();

        if (itemStack == null) return;
        //if (itemStack == null || !main.isPlayerSpeedrunner(player)) return;

        if (itemStack.getType() == Material.SEA_PICKLE) {
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§bCornichon sacré du respawn")) {
                if (action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK) {
                    player.openInventory(new RespawnItemInventory(main).getInventory());
                }
            }
        }
    }

    @EventHandler
    public void onRespawnItemPlacing(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        ItemStack itemStack = event.getItemInHand();

        if (!main.isPlayerSpeedrunner(player)) return;

        if (itemStack.getType() == Material.SEA_PICKLE) {
            if (itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName() && itemStack.getItemMeta().getDisplayName().equalsIgnoreCase("§bCornichon sacré du respawn")) {
                event.setCancelled(true);
            }
        }
    }
}
