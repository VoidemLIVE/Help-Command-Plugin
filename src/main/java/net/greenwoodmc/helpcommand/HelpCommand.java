package net.greenwoodmc.helpcommand;

import net.greenwoodmc.helpcommand.commands.hcCommand;
import net.greenwoodmc.helpcommand.listeners.helpAliases;
import net.greenwoodmc.helpcommand.listeners.PlayerJoinListener;
import net.greenwoodmc.helpcommand.tabcomplete.hc;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import net.greenwoodmc.helpcommand.commands.help;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.List;

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
        FileConfiguration config = getConfig();
        getCommand("help").setExecutor(new help());
        getCommand("hc").setExecutor(new hcCommand());
        getCommand("hc").setTabCompleter(new hc());
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
        if (!config.getStringList("aliases").isEmpty()) {
            getLogger().info("Aliases: Enabled");
            getServer().getPluginManager().registerEvents(new helpAliases(), this);
            registerAliases();
        } else {
            getLogger().info("Aliases: Disabled");
        }

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

    private void registerAliases() {
        CommandMap commandMap = getCommandMap();
        List<String> aliases = getConfig().getStringList("aliases");
        for (String alias : aliases) {
            BukkitCommand command = new BukkitCommand(alias) {
                @Override
                public boolean execute(@NotNull CommandSender commandSender, @NotNull String s, @NotNull String[] strings) {
                    return false;
                }
            };
            commandMap.register(getDescription().getName(), command);
        }
    }

    private CommandMap getCommandMap() {
        try {
            Field commandMapField = getServer().getClass().getDeclaredField("commandMap");
            commandMapField.setAccessible(true);
            return (CommandMap) commandMapField.get(getServer());
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
}