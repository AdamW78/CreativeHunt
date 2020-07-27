package online.x16.CreativeHunt;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class WorldChangeListener implements Listener {

	private CreativeHunt plugin;
	private Location lastLoc;
	
	public WorldChangeListener(CreativeHunt instance) {
		plugin = instance;
	}
	/**
	 * 
	 * @param e
	 */
	@EventHandler
	public void onWorldChange(PlayerTeleportEvent e) {
		
		//Fetch the player's last location before changing worlds
		lastLoc = e.getFrom();
		//Check if the Player p changed world's and if they are currently targeted
		Player tracker = plugin.getMap().isTargeted(e.getPlayer());
		if (!lastLoc.getWorld().equals(e.getTo().getWorld()) && tracker != null) {
			WorldTracker t = plugin.getMap().getWorldTracker(tracker);
			t.updateWorldLastLoc(lastLoc);
			t.updateCurWorld(e.getTo());
		}
	}
	
}
