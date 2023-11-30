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

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(getConfig().getString("playersOnly"));
            return true;
        } else {
            FileConfiguration config = getConfig();
            Player player = (Player)sender;
            String ver;
            String arg1;
            String colour1 = config.getString("pagePrompt.mainTextColour");
            String colour2 = config.getString("pagePrompt.pageNumberColour");
            ChatColor COLOUR1 = ChatColor.valueOf(colour1);
            ChatColor COLOUR2 = ChatColor.valueOf(colour2);
            String nextPage = config.getString("pagePrompt.mainText");
            List<Integer> enabledPages = config.getIntegerList("pagesEnabled");
            if (cmd.getName().equalsIgnoreCase("hc")) {
                if (args.length >= 1) {
                    arg1 = args[0];
                    if (arg1.equalsIgnoreCase("reload")) {
                        ver = getConfig().getString("reload");
                        reloadConfig();
                        player.sendMessage(TextUtil.color(ver));
                    }

                    if (arg1.equalsIgnoreCase("show")) {
                        String helpmsg = (String)config.getStringList("help").stream().collect(Collectors.joining("\n"));
                        player.sendMessage(TextUtil.color(helpmsg));
                    }

                    if (arg1.equalsIgnoreCase("version")) {
                        ver = getDescription().getVersion();
                        player.sendMessage("HelpCommand Version: " + ver);
                    }

                    if (arg1.equalsIgnoreCase("help")) {
                        player.sendMessage(TextUtil.color("&6HelpCommand wiki: &ehttps://voidemtwitch.gitbook.io/help-command/"));
                        player.sendMessage(TextUtil.color("&6Support Discord: &ehttps://discord.com/invite/vbcqu6rts8"));
                    }
                } else {
                    arg1 = getConfig().getString("noargument");
                    player.sendMessage(TextUtil.color(arg1));
                }
            }

            return true;
        }
    }
}