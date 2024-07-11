package net.ddns.vcccd;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.util.Vector;

public class LazerGunFire implements Listener {
	private final Main main;
	
	public LazerGunFire(Main main) {
		this.main = main;
	}
	
private Mob getTarget(Player player, int BlockArea) {
		
		//Value to be returned from the Function
		Mob returnVal = null;
		
		//Iterates over the array of entities retrieved form the area
		for(Entity IterativeEntity: player.getNearbyEntities(BlockArea, BlockArea, BlockArea)) {
			
			//We want to make sure the target is a Mob
			if(IterativeEntity instanceof Mob) {
				
			//If so, we want to get the directional vector of the eye location
			Vector PlayerDirectionalVector = player.getEyeLocation().getDirection();
			
			//And we also want to get the Local Targets location
			Location TargetLocal = IterativeEntity.getLocation();
			
			//TargetLocal is located at the base of the entity, we want the body
			//I know all mobs aren't two blocks tall, will change this later
			TargetLocal.setY(TargetLocal.getY()+1);
			
			//We then want to subtract the new target location from the eye location
			Vector TargetPositionalVector = TargetLocal.toVector().subtract(player.getEyeLocation().toVector());
			
			//And we take angle Theta of the direction the player is facing and the position of the target
			double Theta = PlayerDirectionalVector.angle(TargetPositionalVector);
			
			//If that Theta value is within a certain threshold, we can assume targeting
			if(Theta < 0.130) {
				
				//We then cast the entity to type mob (Note instanceof keyword...)
				Mob target = (Mob) IterativeEntity;
				
				//We set the return value to the first enemy in that threshold
				returnVal = target;
				
				//And break the loop...
				break;
			}
			}
			
		}
		
		//We then return the value stored in the return value
		return(returnVal);
		
		/*
		 * Please note that if there is no targeted enemy, 
		 * the function will return null. Bukkit does not
		 * like this very much. So any implementation must
		 * be put in a try, except structure.
		 */
	}
	
	@EventHandler
	public void onLazerGunShoot(PlayerInteractEvent event) {
		//Access the file configuration 
		FileConfiguration config = main.getConfig();
		int radius = config.getInt("ExplosionRadius");
		boolean fire = config.getBoolean("FireInExplosion");
		int cost = config.getInt("LevelCost");
		int range = config.getInt("FireRange");
		int damage = config.getInt("Damage");
		
		//Gets the item information in the players main hand
		ItemMeta ItemInPlayerHand = event.getPlayer().getInventory().getItemInMainHand().getItemMeta();

		//If the item in the player hand is the ray gun
		if (ItemInPlayerHand == null
				|| !ItemInPlayerHand.getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', "&f&lLaZer&b&lGun"))) {
			return;
			}

		//If the action is a Right Click then...
		if (event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK) {

			//We get the player information from the event
			Player player = event.getPlayer();

			if (player.getLevel() >= cost) {

				World PlayerWorld = player.getWorld();

				//We get the location and direction of the player from various methods
				Location PlayerLocation = player.getLocation();
				Vector PlayerFacingDirection = PlayerLocation.getDirection();

				//We also want to spawn a stream of particles in the face of the player

				Particle Lazer = Particle.HAPPY_VILLAGER;

				for (int i = 1; i < range * 2; i++) {

					//Ill come back and optimize this again later...
					double[][] VectorTuples = {{0, 1.35, 0}, {0, 1.5, 0}, {0, 1.65, 0},
							{-0.15, 1.5, 0}, {0, 1.5, -0.15}};

					for (int j = 0; j < VectorTuples.length; j++) {
						particle(PlayerLocation, Lazer,
								PlayerFacingDirection, PlayerWorld, i, VectorTuples[j]);
					}

				}

				//We also want to play a sound
				player.playSound(player, Sound.BLOCK_AMETHYST_BLOCK_FALL, 500, 0);

				if (config.getBoolean("GroupTargeting")) {
					try {
						//And kill the entities we look at
						for (Entity e : player.getNearbyEntities(range, range, range)) {

							//Gets the location of the entity and then subtracts the Eye location vector
							Vector check = e.getLocation().toVector().subtract(player.getEyeLocation().toVector());

							//We then normalize and perform the dot product with the player facing direction vector
							double checker = check.normalize().dot(PlayerFacingDirection);

							//If the check meets a threshold
							if (checker > 0.97D) {

								//We want to transform the entity into a Mob object
								if (e instanceof Mob) {

									//i.e right here
									Mob target = (Mob) e;

									//We then store the location in a variable
									Location TargetLocation = e.getLocation();

									//We generate an explosion at the entity
									PlayerWorld.createExplosion(TargetLocation, radius, fire);

									//We add the particle effects
									explodeInStyle(Particle.CAMPFIRE_SIGNAL_SMOKE, TargetLocation, PlayerWorld);

									//and Instakill the enemy
									target.damage(damage);

								}
							}
						}
					} catch (Exception e) {
						assert true;
					}
				} else {
					try {
						Mob target = getTarget(event.getPlayer(), range);
						Location TargetLocation = target.getLocation();
						//We generate an explosion at the entity
						PlayerWorld.createExplosion(TargetLocation, radius, fire);

						//We add the particle effects
						explodeInStyle(Particle.CAMPFIRE_SIGNAL_SMOKE, TargetLocation, PlayerWorld);

						//and Instakill the enemy
						target.damage(damage);
					} catch (Exception e) {
						assert true;
					}
				}

				//Now we gotta take away the levels...
				player.setLevel(player.getLevel() - cost);

			} else {

				//If the player does not have the proper XP we gotta say so
				player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&b&lNot Enough EXP... &r&fYou must have at least " + Integer.toString(cost) + " Levels"));
			}
		}
	}
	
	//Function for summoning Particles in player direction
	private void particle(Location PlayerLocation, Particle particle, 
			Vector PlayerFacingDirection, World PlayerWorld, int i, double[] offset) {
		
		//Inits doubles for the facing Direction
		double[] Direction = {PlayerFacingDirection.getX(), 
				PlayerFacingDirection.getY(), PlayerFacingDirection.getZ()};
		

		//Spawns the particles with the axis offsets 
		PlayerWorld.spawnParticle(particle, PlayerLocation.getX() + offset[0] + i*Direction[0], 
				PlayerLocation.getY()+ offset[1] +  i*Direction[1], PlayerLocation.getZ() + offset[2] + i*Direction[2], 3);
	}
	
	//Function for generating particle explosions as per my egg plugin
	private void explodeInStyle(Particle parameter, Location location, World world) {
		
        //Inits doubles to reference in the argument of the spawn particle method
        double[] Coordinates = {location.getX(), location.getY(), location.getZ()};

        //Spawns the particles
        for (int i = -5; i < 5; i++) {
            world.spawnParticle(parameter, Coordinates[0] + i, 
            		Coordinates[1] + i, Coordinates[2] + i, 10);
            
        }
    }
	
}