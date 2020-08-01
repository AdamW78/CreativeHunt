package online.x16.CreativeHunt;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

import online.x16.CreativeHunt.tools.MessageBuilder;
import org.bukkit.inventory.ItemStack;

public class PlayerInteractListener implements Listener {

	private final CreativeHunt plugin;
	private final boolean debug;

	public PlayerInteractListener(CreativeHunt instance) {
		plugin = instance;
		debug = plugin.getConfig().getBoolean("debug");
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent e) {
		//Store Player
		Player tracker = e.getPlayer();
		//Check if the Player p is in CreativeHunt mode - if not, stop everything here
		if (!plugin.getMap().contains(tracker) || plugin.getMap().hasOfflineTarget(tracker)) return;
		//Check if Player tracker has just interacted with a compass in their main hand
		if (e.getItem() != null && e.getItem().getType().equals(Material.COMPASS)) {
			//Instantiate a MessageBuilder - we have to send the player a message guaranteed from this point on
			MessageBuilder messageBuilder = new MessageBuilder(plugin);
			//Fetch the WorldTracker object to use to fetch a compass target
			WorldTracker t = plugin.getMap().getWorldTracker(tracker);
			//If the WorldTracker returns null for a location to send the player to, send an error message to
			//the Player tracker notifying them they need to enter the same world as their target
			if (t.findTrackerLoc(tracker) == null) {
				if (debug) plugin.log(tracker.getName()+" tried to track someone who has never been in the same world as them");
				tracker.spigot().sendMessage(messageBuilder.build("&cError: &cYou &care &ccurrently &ctracking &ca "
														+ "&cplayer &cyou &chave &cnever &cbeen &cin &cthe &csame &cworld"
														+ " &cwith &c- &cgo &cto &cthe &csame &cworld &cas &c"
														+plugin.getMap().getTarget(tracker).getName()+" &cfor &ctracking &cto &cwork"
														+ "&cproperly."));
			}
			//If the WorldTracker does NOT return null - update the player's compass target and notify the player
			//If the target is in another world, notify their tracker of this
			else {
				TrackerCompass trackerCompass = new TrackerCompass(plugin);
				ItemStack trackerCompassStack = trackerCompass.createTrackerCompass(tracker);
				tracker.getInventory().setItemInMainHand(trackerCompassStack);
				tracker.spigot().sendMessage(messageBuilder.build("&7Compass target updated"));
				if (debug) plugin.log(tracker.getName()+" had their compass target successfully updated");
				if (!plugin.getMap().getTarget(tracker).getWorld().equals(tracker.getWorld())) {
					tracker.spigot().sendMessage(messageBuilder.build("&7Pointing towards your target's last location"
																	+ " in this world"));
				}
				if (plugin.getMap().hasOfflineTarget(tracker)) {
					tracker.spigot().sendMessage(messageBuilder.build(ChatColor.GRAY+plugin.getMap().getTarget(tracker).getName()+" is offline - Pointing you towards " +
							"their last online location."));
				}
			}
		}
	}
	
}
