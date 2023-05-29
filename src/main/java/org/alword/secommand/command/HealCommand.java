package org.alword.secommand.command;

import org.bukkit.ChatColor;
import org.bukkit.attribute.Attribute;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class HealCommand implements CommandExecutor {
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
        if (command.getName().equalsIgnoreCase("heal")){
            double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();
            player.setHealth(maxHealth);
            player.sendMessage(ChatColor.AQUA + "Здоровье востановлено");
        }

        return true;
    }
}
