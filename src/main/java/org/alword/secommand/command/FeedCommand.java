package org.alword.secommand.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class FeedCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Только игроки могут использовать эту команду");
            return true;
        }

        if (!sender.hasPermission("se.vip")) {
            sender.sendMessage(ChatColor.DARK_RED + "Недостаточно прав");
            return true;
        }

        Player player = (Player) sender;
        if (command.getName().equalsIgnoreCase("feed")){
            player.setFoodLevel(20);
            player.sendMessage(ChatColor.AQUA + "Вы поели");
        }

        return true;
    }
}
