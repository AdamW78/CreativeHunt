package online.x16.CreativeHunt;

import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OnRespawnListener implements Listener {
	
	private CreativeHunt plugin;
	private TrackerCompass tracker;
	
    public OnRespawnListener (CreativeHunt instance) {
		plugin = instance;
	}
	
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if(plugin.getMap().contains(e.getPlayer())) {
			plugin.getMap().startSurvivalTimer(e.getPlayer());
			tracker = new TrackerCompass(plugin);
			tracker.giveTrackerCompass(e.getPlayer());
		}
	}

}
