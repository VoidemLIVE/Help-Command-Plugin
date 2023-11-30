package net.greenwoodmc.helpcommand.commands;

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
        } else {
            Player player = (Player) sender;
            String ver;
            String arg1;
            String colour1 = config.getString("pagePrompt.mainTextColour");
            String colour2 = config.getString("pagePrompt.pageNumberColour");
            ChatColor COLOUR1 = ChatColor.valueOf(colour1);
            ChatColor COLOUR2 = ChatColor.valueOf(colour2);
            String nextPage = config.getString("pagePrompt.mainText");
            List<Integer> enabledPages = config.getIntegerList("pagesEnabled");
            JavaPlugin plug = JavaPlugin.getPlugin(HelpCommand.class);

            if (cmd.getName().equalsIgnoreCase("help")) {
                if (config.getBoolean("helpcmd")) {
                    if (args.length >= 1) {
                        arg1 = args[0];
                        // For if there's 2 args
                        if (arg1.equals("1")) {
                            // arg = 1
                            if (enabledPages.contains(1)) {
                                ver = (String) config.getStringList("help.1").stream().collect(Collectors.joining("\n"));
                                player.sendMessage(TextUtil.color(ver));
                                if (enabledPages.contains(2)) {
                                    ComponentBuilder nextPageMSG = new ComponentBuilder(nextPage)
                                            .color(COLOUR1)
                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help 2"))
                                            .append("2")
                                            .color(COLOUR2);
                                    player.spigot().sendMessage(nextPageMSG.create());
                                }
                            } else {
                                player.sendMessage(TextUtil.color(config.getString("pageNA")));
                            }
                        }
                        if (arg1.equals("2")) {
                            // arg = 2
                            if (enabledPages.contains(2)) {
                                ver = (String) config.getStringList("help.2").stream().collect(Collectors.joining("\n"));
                                player.sendMessage(TextUtil.color(ver));
                                if (enabledPages.contains(3)) {
                                    ComponentBuilder nextPageMSG = new ComponentBuilder(nextPage)
                                            .color(COLOUR1)
                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help 3"))
                                            .append("3")
                                            .color(COLOUR2);
                                    player.spigot().sendMessage(nextPageMSG.create());
                                }
                            } else {
                                player.sendMessage(TextUtil.color(config.getString("pageNA")));
                            }
                        }
                        if (arg1.equals("3")) {
                            // arg = 3
                            if (enabledPages.contains(3)) {
                                ver = (String) config.getStringList("help.3").stream().collect(Collectors.joining("\n"));
                                player.sendMessage(TextUtil.color(ver));
                                if (enabledPages.contains(4)) {
                                    ComponentBuilder nextPageMSG = new ComponentBuilder(nextPage)
                                            .color(COLOUR1)
                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help 4"))
                                            .append("4")
                                            .color(COLOUR2);
                                    player.spigot().sendMessage(nextPageMSG.create());
                                }
                            } else {
                                player.sendMessage(TextUtil.color(config.getString("pageNA")));
                            }
                        }
                        if (arg1.equals("4")) {
                            // arg = 4
                            if (enabledPages.contains(4)) {
                                ver = (String) config.getStringList("help.4").stream().collect(Collectors.joining("\n"));
                                player.sendMessage(TextUtil.color(ver));
                                if (enabledPages.contains(5)) {
                                    ComponentBuilder nextPageMSG = new ComponentBuilder(nextPage)
                                            .color(COLOUR1)
                                            .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help 5"))
                                            .append("5")
                                            .color(COLOUR2);
                                    player.spigot().sendMessage(nextPageMSG.create());
                                }
                            } else {
                                player.sendMessage(TextUtil.color(config.getString("pageNA")));
                            }
                        }
                        if (arg1.equals("5")) {
                            // arg = 5
                            if (enabledPages.contains(5)) {
                                ver = (String) config.getStringList("help.5").stream().collect(Collectors.joining("\n"));
                                player.sendMessage(TextUtil.color(ver));
                            } else {
                                player.sendMessage(TextUtil.color(config.getString("pageNA")));
                            }
                        }
                        if (Integer.parseInt(arg1) > 5 || 1 > Integer.parseInt(arg1)) {
                            // If larger than 5 or smaller than 1
                            player.sendMessage(TextUtil.color(config.getString("pageNA")));
                        }
                    } else {
                        // Page 1
                        if (enabledPages.contains(2)) {
                            // If page 2 exists
                            ver = (String) config.getStringList("help.1").stream().collect(Collectors.joining("\n"));
                            player.sendMessage(TextUtil.color(ver));
                            ComponentBuilder nextPageMSG = new ComponentBuilder(nextPage)
                                    .color(COLOUR1)
                                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/help 2"))
                                    .append("2")
                                    .color(COLOUR2);
                            player.spigot().sendMessage(nextPageMSG.create());
                        } else {
                            // If page 2 doesn't exist
                            ver = (String) config.getStringList("help.1").stream().collect(Collectors.joining("\n"));
                            player.sendMessage(TextUtil.color(ver));
                        }
                    }
                } else {
                    arg1 = config.getString("disabled");
                    player.sendMessage(TextUtil.color(arg1));
                }
            }
        }
        return false;
    }
}
