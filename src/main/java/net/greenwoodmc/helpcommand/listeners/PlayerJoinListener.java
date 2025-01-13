package net.greenwoodmc.helpcommand.listeners;

import net.greenwoodmc.helpcommand.HelpCommand;
import net.greenwoodmc.helpcommand.util.TextUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Plugin plugin = JavaPlugin.getPlugin(HelpCommand.class);

        if (!player.hasPermission("hc.admin") ||
                !plugin.getConfig().getBoolean("updateCheck")) {
            return;
        }
        // ver check now async
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL("https://api.spigotmc.org/simple/0.1/index.php?action=getResource&id=102926");
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setConnectTimeout(5000); // 5 second timeout

                    connection.setReadTimeout(5000);

                    if (connection.getResponseCode() == 200) {
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;

                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();
                        String spigotVersion = extractVersionFromResponse(response.toString());
                        String currentVersion = plugin.getConfig().getString("version");
                        if (!spigotVersion.isEmpty() && !spigotVersion.equals(currentVersion)) {
                            new BukkitRunnable() {
                                @Override
                                public void run() {
                                    if (player.isOnline()) {
                                        player.sendMessage(TextUtil.color(
                                                "&eA new version of HelpCommand is available! Your version: &aV" +
                                                        currentVersion + ", &edownload &aV" + spigotVersion +
                                                        "&e here: &dhttps://www.spigotmc.org/resources/102926/"
                                        ));
                                    }
                                }
                            }.runTask(plugin);
                        }
                    }
                } catch (IOException e) {
                    plugin.getLogger().warning("Error checking Spigot version: " + e.getMessage());
                }
            }
        }.runTaskAsynchronously(plugin);
    }
    private String extractVersionFromResponse(String jsonResponse) {
        Plugin plugin = JavaPlugin.getPlugin(HelpCommand.class);
        try {
            JSONParser parser = new JSONParser();
            JSONObject jsonObject = (JSONObject) parser.parse(jsonResponse);
            Object versionObject = jsonObject.get("current_version");
            return versionObject != null ? versionObject.toString() : "";
        } catch (ParseException e) {
            plugin.getLogger().warning("Error parsing JSON response: " + e.getMessage());
            return "";
        }
    }
}