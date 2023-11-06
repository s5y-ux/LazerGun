package net.ddns.vcccd;

import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin{

	//Gets the server console
		ConsoleCommandSender console = getServer().getConsoleSender();
		
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(new LazerGunFire(), this);
        this.getCommand("lazergun").setExecutor(new LazerGun());
		
	}
	
	@Override
	public void onDisable() {
		
	}
}