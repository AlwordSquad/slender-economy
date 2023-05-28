package org.alword.secommand.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlyCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игроки могут использовать эту команду");
            return true;
        }

        if (!sender.hasPermission("se.vip")) {
            sender.sendMessage(ChatColor.AQUA + "Недостаточно прав");
            return true;
        }

        Player player = (Player) sender;

            if (player.getAllowFlight()) {
                player.setAllowFlight(false);
                player.sendMessage(ChatColor.AQUA + "Полёт выключен");
            } else {
                player.setAllowFlight(true);
                player.sendMessage(ChatColor.AQUA + "Полёт включен");
            }

        return true;
    }
}
