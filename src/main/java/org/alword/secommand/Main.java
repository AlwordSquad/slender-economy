package org.alword.secommand;

import org.alword.secommand.command.FeedCommand;
import org.alword.secommand.command.FlyCommand;
import org.alword.secommand.command.HealCommand;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {

    @Override
    public void onEnable() {
        getServer().getPluginCommand("fly").setExecutor(new FlyCommand());
        getServer().getPluginCommand("heal").setExecutor(new HealCommand());
        getServer().getPluginCommand("feed").setExecutor(new FeedCommand());
    }
}
