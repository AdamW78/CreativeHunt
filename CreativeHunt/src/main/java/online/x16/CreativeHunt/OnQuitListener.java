package online.x16.CreativeHunt;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnQuitListener implements Listener {
	
	private final CreativeHunt plugin;
	
	public OnQuitListener(CreativeHunt instance) {
		plugin = instance;
	}

	/**
	 * When a player quits, log them off and put them into the offlineMap if they were in CreativeHunt mode as a tracker or a target
	 * @param e PlayerQuitEvent to fetch Player from
	 */
	@EventHandler
	public void onQuit (PlayerQuitEvent e) {
		Player p = e.getPlayer();
		plugin.log("Player who just logged off was being targeted by: "+plugin.getMap().isTargeted(p));
		if (plugin.getMap().contains(p) || plugin.getMap().isTargeted(p) != null) {
			if (plugin.getMap().contains(p)) plugin.log("QUIT EVENT FOR TRACKER "+e.getPlayer().getName());
			plugin.getMap().logOffPlayer(p, p.getLocation());
		}
	}

}
