package net.greenwoodmc.helpcommand;

import net.greenwoodmc.helpcommand.commands.hcCommand;
import net.greenwoodmc.helpcommand.tabcomplete.hc;
import net.greenwoodmc.helpcommand.util.TextUtil;

import java.awt.*;
import java.util.List;
import java.util.stream.Collectors;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.plugin.java.JavaPlugin;
import net.greenwoodmc.helpcommand.commands.help;

public class HelpCommand extends JavaPlugin {

    public void onEnable() {
        getLogger().info("Help Command Enabled");
        getLogger().info("Author: VoidemLIVE");
        getLogger().info("Version: " + getDescription().getVersion());
        int pluginId = 15592;
        new Metrics(this, pluginId);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getCommand("help").setExecutor(new help());
        getCommand("hc").setExecutor(new hcCommand());
        getCommand("hc").setTabCompleter(new hc());

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
}