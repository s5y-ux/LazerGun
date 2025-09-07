package net.ddns.vcccd;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateChecker implements Listener {

    String gameVersion = "1.2.3";
    private String Version = constructData();
	private final Main main;

    public UpdateChecker(Main main){
        this.main = main;
    }

    private void sendUpdateMessage(CommandSender sender) {
		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7Update Avaliable for [&6LazerGun&7]"));
		sender.sendMessage(ChatColor.GRAY + "Your version -> " + ChatColor.GOLD + gameVersion);
		sender.sendMessage(ChatColor.GRAY + "Latest version -> " + ChatColor.GOLD + Version);
	}

     //Used to construct the JSON data for the rest of the class
    private String constructData(){

        //Exception Handling for API
        try {

            //API URL for JSON data on Crypto
            @SuppressWarnings("deprecation")
			URL url = new URL("https://api.spigotmc.org/legacy/update.php?resource=113418");

            //Creates the connection and sends a GET request
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            //Response is read and stored in a String Builder object reading the lines in the Buffer
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            //Closes the reader and Parses the JSON response
            reader.close();
            
            return (response.toString());

        } catch (Exception e) {

            //Otherwise, we can just send an error to the server console
            return (null);
        }
    }
    
    @EventHandler
    public void on(PlayerJoinEvent event) {
        if(event.getPlayer().isOp()) {
            if(!this.Version.equals(this.gameVersion)) {
            	sendUpdateMessage(event.getPlayer());
            	sendUpdateMessage(main.getConsole());
            }
        }
    }
    
}
