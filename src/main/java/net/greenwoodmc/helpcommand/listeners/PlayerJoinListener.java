package net.greenwoodmc.helpcommand.listeners;

import net.greenwoodmc.helpcommand.HelpCommand;
import net.greenwoodmc.helpcommand.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.bukkit.configuration.file.FileConfiguration;

public class PlayerJoinListener implements Listener {
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = (Player) event.getPlayer();
        Plugin plugin = JavaPlugin.getPlugin(HelpCommand.class);
        FileConfiguration config = JavaPlugin.getPlugin(HelpCommand.class).getConfig();

        if (event.getPlayer().hasPermission("hc.admin")) {
            if (config.getBoolean("updateCheck")) {
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
                            player.sendMessage(TextUtil.color("&eA new version of HelpCommand is available! Your version: &aV" + config.getString("version") + ", &edownload &aV" + spigotVersion + "&e here: &dhttps://www.spigotmc.org/resources/102926/"));
                        }
                    } else {
                        plugin.getLogger().warning("Failed to retrieve plugin information from SpigotMC. HTTP response code: " + connection.getResponseCode());
                    }
                } catch (IOException e) {
                    plugin.getLogger().warning("Error checking Spigot version: " + e.getMessage());
                }
            }
        }
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