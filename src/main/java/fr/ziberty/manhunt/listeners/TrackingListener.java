package fr.ziberty.manhunt.listeners;

import fr.ziberty.manhunt.Manhunt;
import fr.ziberty.manhunt.tasks.PutPlayerInSpectator;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.weather.WeatherChangeEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TrackingListener implements Listener {
    private Manhunt main;
    private HashMap<Player, Integer> playerTrackingMap = new HashMap<>();
    private HashMap<Player, Location> lastKnownSpeedrunnerLocationInOverworld = new HashMap<>();
    private HashMap<Player, Location> lastKnownSpeedrunnerLocationInNether = new HashMap<>();
    private HashMap<Player, Location> lastKnownSpeedrunnerLocationInEnd = new HashMap<>();
    private HashMap<Player, Location> playerNetherPortalLocation = new HashMap<>();
    private HashMap<Player, Location> playerEndPortalLocation = new HashMap<>();

    private HashMap<Player, Boolean> playerVisitedNether = new HashMap<>();
    private HashMap<Player, Boolean> playerVisitedEnd = new HashMap<>();
    public HashMap<Player, Integer> getPlayerTrackingMap() {
        return playerTrackingMap;
    }
    public HashMap<Player, Location> getLastKnownSpeedrunnerLocationInOverworld() {
        return lastKnownSpeedrunnerLocationInOverworld;
    }
    public HashMap<Player, Location> getLastKnownSpeedrunnerLocationInNether() {
        return lastKnownSpeedrunnerLocationInNether;
    }
    public HashMap<Player, Location> getLastKnownSpeedrunnerLocationInEnd() {
        return lastKnownSpeedrunnerLocationInEnd;
    }
    private void setLastKnownSpeedrunnerLocationInOverworld(HashMap<Player, Location> lastKnownSpeedrunnerLocationInOverworld) {
        this.lastKnownSpeedrunnerLocationInOverworld = lastKnownSpeedrunnerLocationInOverworld;
    }
    private void setLastKnownSpeedrunnerLocationInNether(HashMap<Player, Location> lastKnownSpeedrunnerLocationInNether) {
        this.lastKnownSpeedrunnerLocationInNether = lastKnownSpeedrunnerLocationInNether;
    }
    private void setLastKnownSpeedrunnerLocationInEnd(HashMap<Player, Location> lastKnownSpeedrunnerLocationInEnd) {
        this.lastKnownSpeedrunnerLocationInEnd = lastKnownSpeedrunnerLocationInEnd;
    }
    public HashMap<Player, Location> getPlayerNetherPortalLocation() {
        return playerNetherPortalLocation;
    }
    public HashMap<Player, Location> getPlayerEndPortalLocation() {
        return playerEndPortalLocation;
    }
    public void setPlayerVisitedNether(HashMap<Player, Boolean> playerVisitedNether) {
        this.playerVisitedNether = playerVisitedNether;
    }
    public void setPlayerVisitedEnd(HashMap<Player, Boolean> playerVisitedEnd) {
        this.playerVisitedEnd = playerVisitedEnd;
    }
    public void setPlayerTrackingMap(HashMap<Player, Integer> playerTrackingMap) {
        this.playerTrackingMap = playerTrackingMap;
    }
    public boolean hasPlayerVisitedNether(Player player) {
        return playerVisitedNether.get(player);
    }
    public boolean hasPlayerVisitedEnd(Player player) {
        return playerVisitedEnd.get(player);
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
                int playerMapNumber = 0;
                if (main.getSpeedrunnersList().size() != 1) {
                    do {
                        playerMapNumber = playerTrackingMap.get(player) + 1;
                        if (playerMapNumber >= main.getSpeedrunnersList().size()) playerMapNumber = 0;
                    } while (!main.getSpeedrunnersList().get(playerMapNumber).getGameMode().equals(GameMode.SURVIVAL));
                    playerTrackingMap.put(player, playerMapNumber);
                }
                Player trackedPlayer = main.getSpeedrunnersList().get(playerMapNumber);
                String monde = "Overworld";
                if (trackedPlayer.getWorld().getEnvironment() == World.Environment.NETHER) {
                    monde = "Nether";
                } else if (trackedPlayer.getWorld().getEnvironment() == World.Environment.THE_END) {
                    monde = "End";
                }
                player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText("§eJoueur pointé : §c" + trackedPlayer.getDisplayName() + "§e | Monde : §c" + monde));
            }
        }
    }

    @EventHandler
    public void setSpeedrunnerGamemodeToSpectatorOnDeath(PlayerRespawnEvent event) {
        Player player = event.getPlayer();
        if (main.isPlayerSpeedrunner(player)) {
            PutPlayerInSpectator putPlayerInSpectator = new PutPlayerInSpectator(player);
            putPlayerInSpectator.runTaskTimer(main, 0, 1);
        }
    }

    public void setPlayersFirstLocations() {
        HashMap<Player, Location> overworldLocation = new HashMap<>();
        HashMap<Player, Location> netherLocation = new HashMap<>();
        HashMap<Player, Location> endLocation = new HashMap<>();
        HashMap<Player, Boolean> playerVisitedNether = new HashMap<>();
        HashMap<Player, Boolean> playerVisitedEnd = new HashMap<>();
        for (Player player : Bukkit.getOnlinePlayers()) {
            overworldLocation.put(player,  new Location(Bukkit.getWorld("world"), 0, 0, 0));
            netherLocation.put(player,  new Location(Bukkit.getWorld("world_nether"), 0, 0, 0));
            endLocation.put(player,  new Location(Bukkit.getWorld("world_the_end"), 0, 0, 0));
            playerVisitedNether.put(player, false);
            playerVisitedEnd.put(player, false);

            setLastKnownSpeedrunnerLocationInOverworld(overworldLocation);
            setLastKnownSpeedrunnerLocationInNether(netherLocation);
            setLastKnownSpeedrunnerLocationInEnd(endLocation);
            setPlayerVisitedNether(playerVisitedNether);
            setPlayerVisitedEnd(playerVisitedEnd);
        }
    }

    @EventHandler
    public void onWeatherChange(WeatherChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();
        if (world.getEnvironment() == World.Environment.NETHER && !hasPlayerVisitedNether(player)) {
            playerVisitedNether.put(player, true);
            playerNetherPortalLocation.put(player, player.getLocation());
        }
        if (world.getEnvironment() == World.Environment.THE_END && !hasPlayerVisitedEnd(player)) {
            playerVisitedEnd.put(player, true);
            playerEndPortalLocation.put(player, player.getLocation());
        }
        if (main.isPlayerSpeedrunner(player)) {
            String worldString = "l'overworld";
            switch (world.getEnvironment()) {
                case NETHER:
                    worldString = "le nether";
                    break;
                case THE_END:
                    worldString = "l'end";
                    break;
            }
            for (Player p : Bukkit.getOnlinePlayers()) {
                if (playerTrackingMap.get(p) == main.getSpeedrunnersList().indexOf(player)) {
                    p.sendMessage("§a" + player.getDisplayName() + " est passé dans " + worldString);
                }
            }
        }
    }
}
