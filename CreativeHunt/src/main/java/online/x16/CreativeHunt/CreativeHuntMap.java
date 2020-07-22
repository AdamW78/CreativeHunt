package online.x16.CreativeHunt;

import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.bukkit.entity.Player;

import online.x16.CreativeHunt.tools.MessageBuilder;

public class CreativeHuntMap {

	private CreativeHunt plugin;
	private HashMap<Player, ScheduledThreadPoolExecutor> map;
	private MessageBuilder messageBuilder;
	/**
	 * Constructor for CreativeHuntMap so that config values can be passed in through a CreativeHunt instance
	 * @param instance CreativeHunt instance
	 */
	public CreativeHuntMap(CreativeHunt instance) {
		plugin = instance;
		messageBuilder = new MessageBuilder(plugin);
	}
	/**
	 * Puts a Player p into a CreativeHuntMap
	 * @param p Player to be put into CreativeHuntMap
	 * @return boolean Whether or not Player was successfully added
	 */
	public boolean put(Player p) {
		if (contains(p)) {
			return false;
		}
		else {
			map.put(p, new ScheduledThreadPoolExecutor(1));
			startSurvivalTimer(p);
			return true;
		}
	}
	/**
	 * Removes a Player p from a CreativeHuntMap
	 * @param p Player to remove from the CreativeHuntMap
	 * @return boolean Whether or not Player was successfully removed
	 */
	public boolean remove(Player p) {
		if (map.remove(p) != null) {
			return true;
		}
		return false;
	}
	/**
	 * Toggles whether or not Player p is in the CreativeHuntMap
	 * @param p Player to toggle in the CreativeHuntMap
	 * @return boolean Whether or not the player is in CreativeHuntMap after the method call
	 */
	public boolean toggle (Player p) {
		if (contains(p)) {
			return remove(p);
		}
		return put(p);
	}
	/**
	 * Checks whether a player is in a CreativeHuntMap
	 * @param p Player to check in the CreativeHuntMap
	 * @return boolean Whether or not the player was in the CreativeHuntMap
	 */
	public boolean contains(Player p) {
		return map.containsKey(p);
	}
	/**
	 * Puts a Player p into survival before the end of their timer
	 * @param p Player to put into survival
	 */
	public void cancelSurvivalTimer(Player p) {
		map.get(p).shutdown();
	}
	
	/**
	 * Start a Survival Timer for Player p
	 * @param p PLayer to set a timer for
	 */
	public void startSurvivalTimer(Player p) {
		map.get(p).schedule(new GamemodeCallable(p), plugin.getConfig().getInt("creative-seconds"), TimeUnit.SECONDS);
		p.spigot().sendMessage(messageBuilder.build("&7You have &7"+plugin.getConfig().getInt("creative-seconds")+"&7 seconds in creative mode."));
		
	}
}
