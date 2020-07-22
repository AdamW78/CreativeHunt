package online.x16.CreativeHunt;

import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnDeathListener implements Listener {
	
	private CreativeHunt plugin;
	
    public OnDeathListener (CreativeHunt instance) {
		plugin = instance;
	}
	
	public void onPlayerDeath(PlayerDeathEvent e) {
		if(plugin.getMap().contains(e.getEntity())) {
			//what to do if player is in creative
		}
	}

}
