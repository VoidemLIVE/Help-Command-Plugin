package net.greenwoodmc.helpcommand.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.greenwoodmc.helpcommand.HelpCommand;
import net.greenwoodmc.helpcommand.util.TextUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;
import java.util.stream.Collectors;

public class help implements CommandExecutor {
    FileConfiguration config = JavaPlugin.getPlugin(HelpCommand.class).getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(config.getString("playersOnly"));
            return true;
        }

        Player player = (Player) sender;
        String ver;
        String ver2;
        String pageText = config.getString("pagePrompt.page");
        int currPageNumber;
        String colour1 = config.getString("pagePrompt.pageNumberColour");
        String colour2 = config.getString("pagePrompt.pageTextColour");
        ChatColor COLOUR1 = ChatColor.valueOf(colour1);
        ChatColor COLOUR2 = ChatColor.valueOf(colour2);
        List<Integer> enabledPages = config.getIntegerList("pagesEnabled");
        int lastPage = enabledPages.get(enabledPages.size() - 1);
        JavaPlugin plug = JavaPlugin.getPlugin(HelpCommand.class);
        boolean papiInstalled = HelpCommand.isPapiInstalled(plug);
        boolean pagePromptEnabled = config.getBoolean("pagePrompt.enabled");

        if (cmd.getName().equalsIgnoreCase("help")) {
            if (config.getBoolean("helpcmd")) {
                if (args.length > 0) {
                    try {
                        currPageNumber = Integer.parseInt(args[0]);
                    } catch (NumberFormatException e) {
                        player.sendMessage(TextUtil.color(config.getString("pageNA")));
                        return true;
                    }
                } else {
                    currPageNumber = 1; // Default to page 1 if no arguments provided
                }

                if (currPageNumber >= 1 && currPageNumber <= lastPage) {
                    if (enabledPages.contains(currPageNumber)) {
                        ver = config.getStringList("help." + currPageNumber).stream().collect(Collectors.joining("\n"));
                        ver2 = papiInstalled ? PlaceholderAPI.setPlaceholders(player, ver) : ver;
                        player.sendMessage(TextUtil.color(ver2));

                        if (pagePromptEnabled && enabledPages.size() > 1) { // Only show page prompt if more than one page
                            ComponentBuilder pagePromptBuilder = new ComponentBuilder();

                            if (currPageNumber > 1) { // Show previous page arrow if not on the first page
                                int previousPageNumber = currPageNumber - 1;
                                pagePromptBuilder.append("[<< " + previousPageNumber + "] ")
                                        .color(COLOUR1)
                                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help " + previousPageNumber));
                            }

                            // Add the current page number without a click event
                            pagePromptBuilder.append(pageText + " " + currPageNumber)
                                    .color(COLOUR2);

                            if (currPageNumber < lastPage) { // Show next page arrow if not on the last page
                                int nextPageNumber = currPageNumber + 1;
                                pagePromptBuilder.append(" [" + nextPageNumber + " >>]")
                                        .color(COLOUR1)
                                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help " + nextPageNumber));
                            }

                            player.spigot().sendMessage(pagePromptBuilder.create());
                        }
                    } else {
                        player.sendMessage(TextUtil.color(config.getString("pageNA")));
                    }
                } else {
                    player.sendMessage(TextUtil.color(config.getString("pageNA")));
                }
            } else {
                int disabledPageNumber = Integer.parseInt(config.getString("disabled"));
                player.sendMessage(TextUtil.color(String.valueOf(disabledPageNumber)));
            }
        }
        return true;
    }

}
