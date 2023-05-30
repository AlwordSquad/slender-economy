package org.alword;
import org.alword.command.Village;
import org.alword.command.VillageCommand;
import org.alword.event.EntityDieEvent;
import org.alword.event.EmeraldOnJoin;
import org.bukkit.plugin.java.JavaPlugin;

public final class BalanceEconomy extends JavaPlugin {

    @Override
    public void onEnable() {
        this.getLogger().info("Экономика балансируется");
        new EntityDieEvent(this);
        new EmeraldOnJoin(this);
        Village village = new Village(this);
        new VillageCommand(this, village);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
