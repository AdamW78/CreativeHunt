package online.x16.CreativeHunt;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteractListener implements Listener {

	private CreativeHunt plugin;
	
	public PlayerInteractListener(CreativeHunt instance) {
		plugin = instance;
	}
	
	public void onPlayerInteract(PlayerInteractEvent e) {
		//Double check that the Player who interacted is a Player
		if(e.getPlayer() instanceof Player) {
			//Store Player
			Player p = e.getPlayer();
			//Check if the Player p is in CreativeHunt mode - if not, stop everything here
			if (!plugin.getMap().contains(p)) return;
			//Check if the item the player just interacted with (in their main hand) was a compass
			if (p.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
				p.setCompassTarget(plugin.getMap().);
			}
		}
	}
	
}
