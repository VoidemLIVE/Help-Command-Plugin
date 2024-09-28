package net.greenwoodmc.helpcommand.commands;

import me.clip.placeholderapi.PlaceholderAPI;
import net.greenwoodmc.helpcommand.HelpCommand;
import net.greenwoodmc.helpcommand.util.TextUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Pattern pattern = Pattern.compile("\\$\\[(.*?)\\]");
        Matcher matcherPageText = pattern.matcher(pageText);
        Matcher matcherPrevPage = pattern.matcher(config.getString("pagePrompt.arrowBack"));
        Matcher matcherNextPage = pattern.matcher(config.getString("pagePrompt.arrowForward"));
        String backArrow = config.getString("pagePrompt.arrowBack");
        String forwardArrow = config.getString("pagePrompt.arrowForward");
        int currPageNumber;
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

                                if (matcherPrevPage.find()) {
                                    String inside = matcherPrevPage.group(1);
                                    if ("prevNum".equalsIgnoreCase(inside)) {
                                        backArrow = backArrow.replace("$[prevNum]", String.valueOf(previousPageNumber));
                                        pagePromptBuilder.append(TextUtil.color(backArrow + " "))
                                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help " + previousPageNumber));
                                    }
                                } else {
                                    pagePromptBuilder.append(TextUtil.color(backArrow + " "))
                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help " + previousPageNumber));
                                }
                            }

                            TextComponent pageTextComponent = null;
                            if (matcherPageText.find()) {
                                String inside = matcherPageText.group(1);
                                if ("pageNum".equalsIgnoreCase(inside)) {
                                    pageText = pageText.replace("$[pageNum]", String.valueOf(currPageNumber));
                                    pageTextComponent = new TextComponent(TextUtil.color(pageText));
                                }
                            } else {
                                pageTextComponent = new TextComponent(TextUtil.color(pageText));
                            }

                            pagePromptBuilder.append(pageTextComponent);

                            if (currPageNumber < lastPage) { // Show next page arrow if not on the last page
                                int nextPageNumber = currPageNumber + 1;

                                if (matcherNextPage.find()) {
                                    String inside = matcherNextPage.group(1);
                                    if ("nextNum".equalsIgnoreCase(inside)) {
                                        forwardArrow = forwardArrow.replace("$[nextNum]", String.valueOf(nextPageNumber));
                                        pagePromptBuilder.append(" " + TextUtil.color(forwardArrow))
                                                .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help " + nextPageNumber));
                                    }
                                } else {
                                    pagePromptBuilder.append(" " + TextUtil.color(forwardArrow))
                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help " + nextPageNumber));
                                }
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
