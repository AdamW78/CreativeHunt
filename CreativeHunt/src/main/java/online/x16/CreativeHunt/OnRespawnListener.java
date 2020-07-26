package online.x16.CreativeHunt;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

public class OnRespawnListener implements Listener {
	
	private CreativeHunt plugin;
	private TrackerCompass tracker;
	
    public OnRespawnListener (CreativeHunt instance) {
		plugin = instance;
	}
	
    @EventHandler
	public void onPlayerRespawn(PlayerRespawnEvent e) {
		if(plugin.getMap().contains(e.getPlayer())) {
			e.getPlayer().setGameMode(GameMode.CREATIVE);
			plugin.getMap().startSurvivalTimer(e.getPlayer());
			tracker = new TrackerCompass(plugin);
			tracker.giveTrackerCompass(e.getPlayer());
		}
	}

}
