package online.x16.CreativeHunt;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import online.x16.CreativeHunt.tools.MessageBuilder;

public class PlayerInteractListener implements Listener {

	private CreativeHunt plugin;
	private MessageBuilder messageBuilder;
	private boolean debug;
	
	public PlayerInteractListener(CreativeHunt instance) {
		plugin = instance;
		debug = plugin.getConfig().getBoolean("debug");
	}
	
	public void onPlayerInteract(PlayerInteractEvent e) {
		//Double check that the Player who interacted is a Player
		if(e.getPlayer() instanceof Player) {
			if (debug) plugin.log("First interact check passed");
			//Store Player
			Player tracker = e.getPlayer();
			//Check if the Player p is in CreativeHunt mode - if not, stop everything here
			if (!plugin.getMap().contains(tracker)) return;
			//Check if Player tracker has just interacted with a compass in their main hand
			if (tracker.getInventory().getItemInMainHand().getType().equals(Material.COMPASS)) {
				if (debug) plugin.log(tracker.getName()+" interacted with a compass in hand");
				//Instantiate a MessageBuilder - we have to send the player a message guaranteed from this point on
				messageBuilder = new MessageBuilder(plugin);
				//Fetch the WorldTracker object to use to fetch a compass target
				WorldTracker t = plugin.getMap().getWorldTracker(tracker);
				//If the WorldTracker returns null for a location to send the player to, send an error message to 
				//the Player tracker notifying them they need to enter the same world as their target
				if (t.findTrackerLoc(tracker) == null) {
					if (debug) plugin.log(tracker.getName()+" tried to track someone who has never been in the same world as them");
					tracker.spigot().sendMessage(messageBuilder.build("&cError: &cYou &care &ccurrently &ctracking &ca "
															+ "&cplayer &cyou &chave &cnever &cbeen &cin &cthe &csame &cworld"
															+ " &cwith &c- &cgo &cto &cthe &csame &cworld &cas &c"
															+e.getPlayer().getName()+" &cfor &ctracking &cto &cwork"
															+ "&cproperly."));
				}
				//If the WorldTracker does NOT return null - update the player's compass target and notify the player
				//If the target is in another world, notify their tracker of this
				else {
					tracker.setCompassTarget(t.findTrackerLoc(tracker));
					tracker.spigot().sendMessage(messageBuilder.build("&7Compass target updated"));
					if (debug) plugin.log(tracker.getName()+" had their compass target successfully updated");
					if (!t.findTrackerLoc(tracker).getWorld().equals(tracker.getWorld())) {
						tracker.spigot().sendMessage(messageBuilder.build("&7Pointing towards your target's last location"
																		+ " in this world"));
					}
				}
			}
		}
	}
	
}
