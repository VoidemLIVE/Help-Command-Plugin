package net.greenwoodmc.helpcommand.commands;

import net.greenwoodmc.helpcommand.HelpCommand;
import net.greenwoodmc.helpcommand.util.TextUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class hcCommand implements CommandExecutor {
    FileConfiguration config = JavaPlugin.getPlugin(HelpCommand.class).getConfig();
    JavaPlugin plug = JavaPlugin.getPlugin(HelpCommand.class);
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        String ver;
        String arg1;
        Boolean isPlayer;
        if (!(sender instanceof Player)) {
            isPlayer = false;
        } else {
            isPlayer = true;
        }

        if (cmd.getName().equalsIgnoreCase("hc")) {
            if (args.length >= 1) {
                arg1 = args[0];
                if (arg1.equalsIgnoreCase("reload") || arg1.equalsIgnoreCase("rl")) {
                    ver = config.getString("reload");
                    plug.reloadConfig();
                    try {
                        config.load(new File(plug.getDataFolder(), "config.yml"));
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (InvalidConfigurationException e) {
                        throw new RuntimeException(e);
                    }
                    if (isPlayer) {
                        sender.sendMessage(TextUtil.color(ver));
                    } else {
                        plug.getLogger().info("Plugin reloaded");
                    }
                }

                if (arg1.equalsIgnoreCase("version") || arg1.equalsIgnoreCase("ver")) {
                    ver = plug.getDescription().getVersion();
                    if (isPlayer) {
                        sender.sendMessage("HelpCommand Version: " + ver);
                        try {
                            URL url = new URL("https://api.spigotmc.org/simple/0.1/index.php?action=getResource&id=102926");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            if (connection.getResponseCode() == 200) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                StringBuilder response = new StringBuilder();
                                String line;

                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }

                                String spigotVersion = extractVersionFromResponse(response.toString());

                                if (!spigotVersion.equals(config.getString("version"))) {
                                    sender.sendMessage(TextUtil.color("&dA new version of HelpCommand is available! download &eV" + spigotVersion + "&d here: &ahttps://www.spigotmc.org/resources/102926/"));
                                }
                            } else {
                                plug.getLogger().warning("Failed to retrieve plugin information from SpigotMC. HTTP response code: " + connection.getResponseCode());
                            }
                        } catch (IOException e) {
                            plug.getLogger().warning("Error checking Spigot version: " + e.getMessage());
                        }
                    } else {
                        plug.getLogger().info("HelpCommand Version: " + ver);
                        try {
                            URL url = new URL("https://api.spigotmc.org/simple/0.1/index.php?action=getResource&id=102926");
                            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                            if (connection.getResponseCode() == 200) {
                                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                                StringBuilder response = new StringBuilder();
                                String line;

                                while ((line = reader.readLine()) != null) {
                                    response.append(line);
                                }

                                String spigotVersion = extractVersionFromResponse(response.toString());

                                if (!spigotVersion.equals(config.getString("version"))) {
                                    plug.getLogger().info(TextUtil.color("A new version of HelpCommand is available! download V" + spigotVersion + "here: https://www.spigotmc.org/resources/102926/"));
                                }
                            } else {
                                plug.getLogger().warning("Failed to retrieve plugin information from SpigotMC. HTTP response code: " + connection.getResponseCode());
                            }
                        } catch (IOException e) {
                            plug.getLogger().warning("Error checking Spigot version: " + e.getMessage());
                        }
                    }
                }

                if (arg1.equalsIgnoreCase("help")) {
                    if (isPlayer) {
                        sender.sendMessage(TextUtil.color("&6HelpCommand wiki: &ehttps://hcdocs.voidem.com"));
                        sender.sendMessage(TextUtil.color("&6Support Discord: &ehttps://support.voidem.com"));
                    } else {
                        plug.getLogger().info("HelpCommand wiki: https://hcdocs.voidem.com");
                        plug.getLogger().info("Support Discord: https://support.voidem.com");
                    }
                }
            } else {
                if (isPlayer) {
                    arg1 = config.getString("noargument");
                    sender.sendMessage(TextUtil.color(arg1));
                } else {
                    plug.getLogger().info("No argument");
                }
            }
        }

        return false;
    }

    private String extractVersionFromResponse(String jsonResponse) {
        Plugin plugin = JavaPlugin.getPlugin(HelpCommand.class);

        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            Object versionObject = jsonObject.get("current_version");
            if (versionObject != null) {
                return versionObject.toString();
            } else {
                plugin.getLogger().warning("Failed to extract version from JSON response. 'current_version' field not found.");
            }
        } catch (ParseException e) {
            plugin.getLogger().warning("Error parsing JSON response: " + e.getMessage());
        }
        return "";
    }
}
