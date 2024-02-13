package net.greenwoodmc.helpcommand;

import net.greenwoodmc.helpcommand.commands.hcCommand;
import net.greenwoodmc.helpcommand.listeners.PlayerJoinListener;
import net.greenwoodmc.helpcommand.tabcomplete.hc;
import org.bukkit.plugin.java.JavaPlugin;
import net.greenwoodmc.helpcommand.commands.help;

public class HelpCommand extends JavaPlugin {

    public void onEnable() {

        getLogger().info("Help Command Enabled");
        getLogger().info("Author: VoidemLIVE");
        getLogger().info("Version: " + getDescription().getVersion());
        if (getServer().getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("PlaceholderAPI: Enabled");
        } else {
            getLogger().info("PlaceholderAPI: Disabled");
        }
        int pluginId = 15592;
        new Metrics(this, pluginId);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getCommand("help").setExecutor(new help());
        getCommand("hc").setExecutor(new hcCommand());
        getCommand("hc").setTabCompleter(new hc());
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);

        try {
            Class.forName("org.spigotmc.SpigotConfig");
        } catch (ClassNotFoundException ex) {
            getLogger().severe("To run Help Command, you need to install Spigot or a fork of Spigot");
            getLogger().severe("Download here: https://www.spigotmc.org/wiki/spigot-installation/.");
            getPluginLoader().disablePlugin(this);
            return;
        }
    }

    public void onDisable() {
        getLogger().info("Help Plugin Disabled");
        getLogger().info("Author: VoidemLIVE");
        getLogger().info("Version: " + getDescription().getVersion());
    }

    public static boolean isPapiInstalled(JavaPlugin plugin) {
        return plugin.getServer().getPluginManager().getPlugin("PlaceholderAPI") != null;
    }
}