package online.x16.CreativeHunt;

import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.bukkit.entity.Player;

public class CreativeHuntMap {

	private CreativeHunt plugin;
	private HashMap<Player, ScheduledThreadPoolExecutor> map;
	/**
	 * Constructor for CreativeHuntMap so that config values can be passed in through a CreativeHunt instance
	 * @param instance CreativeHunt instance
	 */
	public CreativeHuntMap(CreativeHunt instance) {
		plugin = instance;
	}
	/**
	 * Puts a Player p into a CreativeHuntMap
	 * @param p Player to be put into CreativeHuntMap
	 */
	public void put(Player p) {
		if (!contains(p)) {
			
		}
	}
	public boolean contains(Player p) {
		return map.containsKey(p);
	}
}
