package net.greenwoodmc.helpcommand;

import net.greenwoodmc.helpcommand.tabcomplete.hc;
import net.greenwoodmc.helpcommand.util.TextUtil;
import java.util.stream.Collectors;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class HelpCommand extends JavaPlugin {

    public void onEnable() {
        getLogger().info("Help Command Enabled");
        getLogger().info("Author: VoidemLIVE");
        getLogger().info("Version: " + getDescription().getVersion());
        getLogger().info("Version(s) intended for: 1.18-1.20.2");
        int pluginId = 15592;
        new Metrics(this, pluginId);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getCommand("help").setExecutor(this);
        getCommand("hc").setExecutor(this);
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
        FileConfiguration config = getConfig();
        Player player = (Player)sender;
        if (!(sender instanceof Player)) {
            sender.sendMessage(config.getString("playersOnly"));
            return true;
        } else {
            String ver;
            String arg1;
            if (cmd.getName().equalsIgnoreCase("help")) {
                if (config.getBoolean("helpcmd")) {
                    ver = (String)config.getStringList("help").stream().collect(Collectors.joining("\n"));
                    player.sendMessage(TextUtil.color(ver));
                } else {
                    arg1 = config.getString("disabled");
                    player.sendMessage(TextUtil.color(arg1));
                }
            } else if (cmd.getName().equalsIgnoreCase("hc")) {
                if (args.length >= 1) {
                    arg1 = args[0];
                    if (arg1.equalsIgnoreCase("reload")) {
                        ver = config.getString("reload");
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
                    arg1 = config.getString("noargument");
                    player.sendMessage(TextUtil.color(arg1));
                }
            }

            return true;
        }
    }
}