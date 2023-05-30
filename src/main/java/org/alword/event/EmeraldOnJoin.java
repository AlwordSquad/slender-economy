package org.alword.event;

import org.alword.BalanceEconomy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Calendar;


public class EmeraldOnJoin implements Listener {
    private final BalanceEconomy economy;
    public EmeraldOnJoin(BalanceEconomy economy) {
        economy.getServer().getPluginManager().registerEvents(this, economy);
        this.economy = economy;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        var emeraldPerEnter = (int)economy.getConfig().get("emeraldPerEnter");
        var nickName = e.getPlayer().getName();
        var config = this.economy.getConfig();
        var playerPath = "joins." + nickName;
        var calendar = Calendar.getInstance();
        var dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        if (!config.contains((playerPath))) {
            giveEmerald(e.getPlayer(), emeraldPerEnter);
        } else {
            var dayOfLogin = (int) config.get(playerPath);
            var needEmerald = dayOfLogin != dayOfMonth;
            if(needEmerald) giveEmerald(e.getPlayer(), emeraldPerEnter);
        }
        config.set(playerPath, dayOfMonth);
        economy.saveConfig();
    }

    private void giveEmerald(Player player, int amount) {
        player.sendMessage("§aС возвращением! Вы получили эмеральд!");
        player.getInventory().addItem(new ItemStack(Material.EMERALD, amount));
    }
}
