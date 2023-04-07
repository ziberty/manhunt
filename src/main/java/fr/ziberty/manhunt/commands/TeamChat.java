package fr.ziberty.manhunt.commands;

import fr.ziberty.manhunt.Manhunt;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeamChat implements CommandExecutor {
    private Manhunt main;
    public TeamChat(Manhunt manhunt) {
        this.main = manhunt;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (commandSender instanceof Player) {
            Player player = (Player) commandSender;
            if (command.getName().equalsIgnoreCase("t")) {
                if (strings.length == 0) {
                    player.sendMessage("§c/t <message>");
                    return false;
                }
                StringBuilder bc = new StringBuilder();
                for (String part : strings) {
                    bc.append(part).append(" ");
                }
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (main.isPlayerSpeedrunner(player) && main.isPlayerSpeedrunner(p)) {
                        p.sendMessage("§e[Speedrunners]§f " + player.getDisplayName() + " : " + bc.toString());
                    } else if (!main.isPlayerSpeedrunner(player) && !main.isPlayerSpeedrunner(p)) {
                        p.sendMessage("§e[Hunters]§f " + player.getDisplayName() + " : " + bc.toString());
                    }
                }
                return true;
            }
        }
        return false;
    }
}
