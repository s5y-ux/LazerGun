package net.ddns.vcccd;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LazerGun implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
        	
        	//Instantiates player as the command sender
            Player player = (Player) sender;

            //Creates the Item Lazer gun as diamond horse armor
            ItemStack LazerGun = new ItemStack(Material.DIAMOND_HORSE_ARMOR);

            //Adopts the item meta and changes the display name
            ItemMeta LazerGunData = LazerGun.getItemMeta();
            LazerGunData.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&f&lLaZer&b&lGun"));

            //Sets the item meta
            LazerGun.setItemMeta(LazerGunData);

            //Then puts the Lazer in the main hand
            player.getInventory().setItemInMainHand(LazerGun);

        }

        return false;
    }

}