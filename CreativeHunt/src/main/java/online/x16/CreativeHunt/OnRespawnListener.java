package online.x16.CreativeHunt;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OnRespawnListener implements Listener {
	
	private CreativeHunt plugin;
	
    public OnRespawnListener (CreativeHunt instance) {
		plugin = instance;
	}
	
	public void onPlayerDeath(PlayerRespawnEvent e) {
		if(plugin.getMap().contains(e.getPlayer())) {
			//what to do if player is in creative hunt map
			
		}
	}

}
