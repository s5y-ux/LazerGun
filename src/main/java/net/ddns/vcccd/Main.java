package net.ddns.vcccd;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class Main extends JavaPlugin{

	//Gets the server console
		ConsoleCommandSender console = getServer().getConsoleSender();
		
	private void message() {
		console.sendMessage(ChatColor.GREEN + "***************");
		console.sendMessage(" ");
		console.sendMessage(ChatColor.YELLOW + "LaZer Gun");
		console.sendMessage(ChatColor.GREEN + "Made by s5y");
		console.sendMessage(ChatColor.GREEN + "Version: " + ChatColor.WHITE + "1.2.2");
		console.sendMessage(" ");
		console.sendMessage(ChatColor.GREEN + "***************");
	}
		
	@Override
	public void onEnable() {
		FileConfiguration config = this.getConfig();
		config.addDefault("FireInExplosion", true);
		config.addDefault("ExplosionRadius", 5);
		config.addDefault("LevelCost", 10);
		config.addDefault("FireRange", 100);
		config.addDefault("GroupTargeting", true);
		config.addDefault("Damage", 10);
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new LazerGunFire(this), this);
        this.getCommand("lazergun").setExecutor(new LazerGun());
        message();
		
	}
	
	@Override
	public void onDisable() {
		
	}
}