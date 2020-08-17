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
		final boolean debug = plugin.getConfig().getBoolean("debug");
		final CreativeHuntMap map = plugin.getMap();
		Player p = e.getPlayer();
		if (debug) {
			if (map.isTargeted(p) != null) {
				plugin.log("Player who just logged off was being targeted by: " + map.isTargeted(p));
			}
			else if (map.isOfflineTarget(p) != null) {
				plugin.log("Player who just logged off was being targeted by: " + map.isOfflineTarget(p));
			}
		}
		if (map.contains(p) || plugin.getMap().isTargeted(p) != null) {
			map.logOffPlayer(p, p.getLocation());
		}
	}

}
