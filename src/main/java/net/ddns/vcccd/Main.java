package net.ddns.vcccd;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	//Gets the server console
		ConsoleCommandSender console = getServer().getConsoleSender();
		
	@Override
	public void onEnable() {
		FileConfiguration config = this.getConfig();
		config.addDefault("FireInExplosion", true);
		config.addDefault("ExplosionRadius", 5);
		config.addDefault("LevelCost", 10);
		this.saveDefaultConfig();
		getServer().getPluginManager().registerEvents(new LazerGunFire(this), this);
        this.getCommand("lazergun").setExecutor(new LazerGun());
		
	}
	
	@Override
	public void onDisable() {
		
	}
}