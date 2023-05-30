package org.alword.command;

import org.alword.BalanceEconomy;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class VillageCommand implements CommandExecutor {
    private final Village village;
    public  VillageCommand(BalanceEconomy plugin, Village village){
        Objects.requireNonNull(plugin.getCommand("village")).setExecutor(this);
        this.village = village;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        int balance = village.getVillagerEmeraldAmount();
        if (balance >= 0) {
            sender.sendMessage("§eБаланс жителей: §a" + balance);
        } else {
            sender.sendMessage("§eДолг жителей: §4" + -balance);
        }
        return true;
    }
}
