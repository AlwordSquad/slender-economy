package org.alword.secommand.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class PeacefulCommand implements CommandExecutor {
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
        if (command.getName().equalsIgnoreCase("peaceful")){
            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)){
                player.removePotionEffect(PotionEffectType.NIGHT_VISION);
                player.sendMessage(ChatColor.AQUA + "Теперь мобы будут вас атаковать");
            } else {
                player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, Integer.MAX_VALUE, 1));
                player.sendMessage(ChatColor.AQUA + "Теперь мобы НЕ будут вас атаковать");
            }
            return true;
        }

        return false;
    }
}
