package net.greenwoodmc.helpcommand.listeners;

import net.greenwoodmc.helpcommand.HelpCommand;
import net.greenwoodmc.helpcommand.util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class helpAliases implements Listener {
    FileConfiguration config = JavaPlugin.getPlugin(HelpCommand.class).getConfig();
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String command = event.getMessage().substring(1);
        List<String> aliases = config.getStringList("aliases");
        Player player = event.getPlayer();
        String ver;
        int aliasIndex = aliases.indexOf(command) + 1; // add 1 because config starts at 1
        for (String alias : aliases) {
            if (command.equalsIgnoreCase(alias)) {
                ver = (String) config.getStringList("help." + aliasIndex).stream().collect(Collectors.joining("\n"));
                player.sendMessage(TextUtil.color(ver));
                event.setCancelled(true); // removes the "Unknown command. Type "/help" for help." thing
                return;
            }
        }
    }
}