package org.alword.secommand.handler;

import org.alword.secommand.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.potion.PotionEffectType;

public class PeacefulHandler implements Listener{
    public PeacefulHandler(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        Entity target = event.getTarget();
        if (target instanceof Player) {
            Player player = (Player) target;
            if (player.hasPotionEffect(PotionEffectType.NIGHT_VISION)) {
                event.setCancelled(true);
            }
        }
    }
}
