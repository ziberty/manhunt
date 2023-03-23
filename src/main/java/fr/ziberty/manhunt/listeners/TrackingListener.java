package fr.ziberty.manhunt.listeners;

import fr.ziberty.manhunt.Manhunt;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TrackingListener implements Listener {
    private Manhunt main;
    private HashMap<Player, Integer> playerTrackingMap = new HashMap<>();
    public HashMap<Player, Integer> getPlayerTrackingMap() {
        return playerTrackingMap;
    }
    public void setPlayerTrackingMap(HashMap<Player, Integer> playerTrackingMap) {
        this.playerTrackingMap = playerTrackingMap;
    }
    public TrackingListener(Manhunt manhunt) {
        this.main = manhunt;
    }

    @EventHandler
    public void onCompassClick(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Action action = event.getAction();
        ItemStack itemStack = event.getItem();

        if (itemStack == null || main.isPlayerSpeedrunner(player)) return;

        if (itemStack.getType() == Material.COMPASS) {
            if (action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR) {
                if (main.getSpeedrunnersList().size() == 1) return;
                int playerMapNumber;
                do {
                    playerMapNumber = playerTrackingMap.get(player) + 1;
                } while (!main.getSpeedrunnersList().get(playerMapNumber).getGameMode().equals(GameMode.SURVIVAL));
                if (playerMapNumber > main.getSpeedrunnersList().size()) playerMapNumber = 0;
                playerTrackingMap.put(player, playerMapNumber);
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§ePointe vers §c" + main.getSpeedrunnersList().get(playerMapNumber)));
            }
        }
    }

    @EventHandler
    public void setSpeedrunnerGamemodeToSpectatorOnDeath(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        player.setGameMode(GameMode.SPECTATOR);
    }
}
