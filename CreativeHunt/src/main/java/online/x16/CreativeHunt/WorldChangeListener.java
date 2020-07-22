package online.x16.CreativeHunt;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import online.x16.CreativeHunt.tools.MessageBuilder;

public class WorldChangeListener implements Listener {

	private CreativeHunt plugin;
	private Location lastLoc;
	private MessageBuilder messageBuilder;
	
	public WorldChangeListener(CreativeHunt instance) {
		plugin = instance;
	}
	/**
	 * 
	 * @param e
	 */
	public void onWorldChange(PlayerTeleportEvent e) {
		//Fetch the player's last location before changing worlds
		lastLoc = e.getFrom();
		//Check if the Player p changed world's and if they are currently targeted
		Player targeter = plugin.getMap().isTargeted(e.getPlayer());
		if (!lastLoc.getWorld().equals(e.getTo().getWorld()) && targeter != null) {
			//Check if the Player p teleported from the world his/her targeter is still in
			if(targeter.getWorld().equals(lastLoc.getWorld())) {
				//If so, create a lastLoc in the ArrayList in the CreativeHuntMap
				plugin.getMap().addLastLoc(e.getPlayer(), lastLoc);
			}
			//Check if the Player p teleported into the world his/her targeter is still in
			else if (targeter.getWorld().equals(e.getTo().getWorld())) {
				//If so, delete the lastLoc
				plugin.getMap().removeLastLoc(targeter);
			}
			//if the targeted player is teleporting to a world from a world his hunter wasn't just in, shut off tracker
			else {
				messageBuilder = new MessageBuilder(plugin);
				targeter.spigot().sendMessage(messageBuilder.build("&cError: &cNon-vanilla &cteleportation &cdetected &c- &ctracking &chas &cbeen &cstopped"));
				e.getPlayer().spigot().sendMessage(messageBuilder.build("&cError: &cNon-vanilla &cteleportation &cdetected &c- &ctracking &chas &cbeen &cstopped"));
				plugin.getMap().remove(targeter);
			}
		}
		else if (plugin.getMap().contains(e.getPlayer())) {
			
		}
	}
	
}
