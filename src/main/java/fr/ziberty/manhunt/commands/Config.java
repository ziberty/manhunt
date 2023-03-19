package fr.ziberty.manhunt.commands;

import fr.ziberty.manhunt.Manhunt;
import fr.ziberty.manhunt.inventories.ConfigInventory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Config implements CommandExecutor {
    private Manhunt main;
    public Config(Manhunt manhunt) {
        this.main = manhunt;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (command.getName().equalsIgnoreCase("config")) {
                ConfigInventory configInventory = main.getConfigInventory();
                player.openInventory(configInventory.getInventory());
                return true;
            }
        }
        return false;
    }
}
