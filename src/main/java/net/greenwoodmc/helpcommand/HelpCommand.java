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
        //int pluginId = ;
        //new Metrics(this, pluginId);
        getConfig().options().copyDefaults();
        saveDefaultConfig();
        getCommand("help").setExecutor(this);
        getCommand("hc").setExecutor(this);
        getCommand("hc").setTabCompleter(new hc());
    }

    public void onDisable() {
        this.getLogger().info("Help Plugin Disabled");
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command");
            return true;
        } else {
            Player player = (Player)sender;
            String ver;
            String arg1;
            if (cmd.getName().equalsIgnoreCase("help")) {
                if (this.getConfig().getBoolean("helpcmd")) {
                    FileConfiguration config = this.getConfig();
                    ver = (String)config.getStringList("help").stream().collect(Collectors.joining("\n"));
                    player.sendMessage(TextUtil.color(ver));
                } else {
                    arg1 = this.getConfig().getString("disabled");
                    player.sendMessage(TextUtil.color(arg1));
                }
            } else if (cmd.getName().equalsIgnoreCase("hc")) {
                if (args.length >= 1) {
                    arg1 = args[0];
                    if (arg1.equalsIgnoreCase("reload")) {
                        ver = this.getConfig().getString("reload");
                        this.reloadConfig();
                        player.sendMessage(TextUtil.color(ver));
                    }

                    if (arg1.equalsIgnoreCase("show")) {
                        FileConfiguration config = this.getConfig();
                        String helpmsg = (String)config.getStringList("help").stream().collect(Collectors.joining("\n"));
                        player.sendMessage(TextUtil.color(helpmsg));
                    }

                    if (arg1.equalsIgnoreCase("version")) {
                        ver = this.getDescription().getVersion();
                        player.sendMessage("HelpCommand Version: " + ver);
                    }

                    if (arg1.equalsIgnoreCase("help")) {
                        player.sendMessage(TextUtil.color("&6HelpCommand wiki: &ehttps://voidemtwitch.gitbook.io/help-command/"));
                        player.sendMessage(TextUtil.color("&6Support Discord: &ehttps://discord.com/invite/vbcqu6rts8"));
                    }
                } else {
                    arg1 = this.getConfig().getString("noargument");
                    player.sendMessage(TextUtil.color(arg1));
                }
            }

            return true;
        }
    }
}