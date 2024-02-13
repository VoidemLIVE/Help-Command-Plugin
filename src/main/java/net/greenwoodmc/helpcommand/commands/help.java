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

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class help implements CommandExecutor {
    FileConfiguration config = JavaPlugin.getPlugin(HelpCommand.class).getConfig();
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(config.getString("playersOnly"));
            return true;
        } else {
            Player player = (Player) sender;
            String ver;
            String ver2;
            String pageText = config.getString("pagePrompt.page");
            Integer currPageNumber;
            Integer nextPageNumber;
            Integer previousPageNumber;
            String colour1 = config.getString("pagePrompt.pageNumberColour");
            String colour2 = config.getString("pagePrompt.pageTextColour");
            ChatColor COLOUR1 = ChatColor.valueOf(colour1);
            ChatColor COLOUR2 = ChatColor.valueOf(colour2);
            List<Integer> enabledPages = config.getIntegerList("pagesEnabled");
            Integer lastPage = config.getInt("numberOfPages");
            JavaPlugin plug = JavaPlugin.getPlugin(HelpCommand.class);
            boolean papiInstalled = HelpCommand.isPapiInstalled(plug);

            if (cmd.getName().equalsIgnoreCase("help")) {
                if (config.getBoolean("helpcmd")) {
                    if (args.length >= 1 && !args[0].equals("1")) {
                        // For if there's 2 args
                        currPageNumber = Integer.valueOf(args[0]);
                        nextPageNumber = currPageNumber + 1;
                        previousPageNumber = currPageNumber - 1;
                        if (currPageNumber > 0 && currPageNumber < lastPage) {
                            // If between 1 and the last page number - 1
                            if (enabledPages.contains(currPageNumber)) {
                                ver = (String) config.getStringList("help." + currPageNumber).stream().collect(Collectors.joining("\n"));
                                if (papiInstalled) {
                                    ver2 = PlaceholderAPI.setPlaceholders(player, ver);
                                } else {
                                    ver2 = ver;
                                }
                                player.sendMessage(TextUtil.color(ver2));
                                if (enabledPages.contains(nextPageNumber)) {
                                    String previousPage = "[<< " + previousPageNumber + "]";
                                    String nextPage = "[" + nextPageNumber + " >>]";
                                    String currentPage = pageText + " " + currPageNumber;
                                    ComponentBuilder nextPageMSG = new ComponentBuilder(previousPage)
                                            .color(COLOUR1)
                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help " + previousPageNumber))
                                            .append(" " + currentPage + " ")
                                            .color(COLOUR2)
                                            .append(nextPage)
                                            .color(COLOUR1)
                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help " + nextPageNumber));
                                    player.spigot().sendMessage(nextPageMSG.create());
                                }
                            } else {
                                player.sendMessage(TextUtil.color(config.getString("pageNA")));
                            }
                        } else if (currPageNumber.equals(lastPage)) {
                            // If the last page number
                            if (enabledPages.contains(lastPage)) {
                                ver = (String) config.getStringList("help." + lastPage).stream().collect(Collectors.joining("\n"));
                                if (papiInstalled) {
                                    ver2 = PlaceholderAPI.setPlaceholders(player, ver);
                                } else {
                                    ver2 = ver;
                                }
                                player.sendMessage(TextUtil.color(ver2));
                                ComponentBuilder nextPageMSG = new ComponentBuilder("[<< " + previousPageNumber + "] ")
                                        .color(COLOUR1)
                                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help " + previousPageNumber))
                                        .append(pageText + " " + currPageNumber)
                                        .color(COLOUR2);
                                player.spigot().sendMessage(nextPageMSG.create());
                            } else {
                                player.sendMessage(TextUtil.color(config.getString("pageNA")));
                            }
                        } else if (currPageNumber < 1 || currPageNumber > lastPage) {
                            // If smaller than 1 or larger than the last page number
                            player.sendMessage(TextUtil.color(config.getString("pageNA")));
                        }
                    } else {
                        // If no page number specified, send page 1
                        if ((enabledPages.contains(2)) || (args[0].equals("1"))) {
                            // If page 2 exists
                            ver = (String) config.getStringList("help.1").stream().collect(Collectors.joining("\n"));
                            if (papiInstalled) {
                                ver2 = PlaceholderAPI.setPlaceholders(player, ver);
                            } else {
                                ver2 = ver;
                            }
                            player.sendMessage(TextUtil.color(ver2));
                            ComponentBuilder nextPageMSG = new ComponentBuilder(pageText + " 1")
                                    .color(COLOUR2)
                                    .append(" [2 >>]")
                                    .color(COLOUR1)
                                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help 2"));
                            player.spigot().sendMessage(nextPageMSG.create());
                        } else {
                            // If page 2 doesn't exist
                            ver = (String) config.getStringList("help.1").stream().collect(Collectors.joining("\n"));
                            ver2 = PlaceholderAPI.setPlaceholders(player, ver);
                            player.sendMessage(TextUtil.color(ver2));
                        }
                    }
                } else {
                    currPageNumber = Integer.valueOf(config.getString("disabled"));
                    player.sendMessage(TextUtil.color(String.valueOf(currPageNumber)));
                }
            }
        }
        return false;
    }
}
