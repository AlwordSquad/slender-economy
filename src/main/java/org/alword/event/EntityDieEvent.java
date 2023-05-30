package org.alword.event;

import org.alword.BalanceEconomy;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.HashMap;


public class EntityDieEvent implements Listener {
    private final HashMap<EntityType, Integer> map;

    public EntityDieEvent(BalanceEconomy plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        map = new HashMap<>();
        map.put(EntityType.IRON_GOLEM, 20 * 60);
        map.put(EntityType.EVOKER, 20 * 60);
        map.put(EntityType.VINDICATOR, 20 * 60);
    }

    @EventHandler
    public void onEntityDie(EntityDeathEvent e) {
        var entity = e.getEntity();
        if (!map.containsKey(entity.getType())) return;
        e.getDrops().removeIf((itemStack) -> itemStack.getType() == Material.EMERALD || itemStack.getType() == Material.IRON_INGOT);
        e.setDroppedExp(0);
    }
}
