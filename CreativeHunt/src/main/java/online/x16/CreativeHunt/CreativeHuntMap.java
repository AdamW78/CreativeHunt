package online.x16.CreativeHunt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.bukkit.entity.Player;
import online.x16.CreativeHunt.tools.MessageBuilder;
import org.bukkit.scheduler.BukkitTask;

public class CreativeHuntMap {

	private CreativeHunt plugin;
	private HashMap<Player, ArrayList<Object>> map;
	private MessageBuilder messageBuilder;
	private boolean debug;
	/**
	 * Constructor for CreativeHuntMap so that config values can be passed in through a CreativeHunt instance
	 * @param instance CreativeHunt instance
	 */
	public CreativeHuntMap(CreativeHunt instance) {
		plugin = instance;
		messageBuilder = new MessageBuilder(plugin);
		map = new HashMap<Player, ArrayList<Object>>();
		debug = plugin.getConfig().getBoolean("debug");
	}
	/**
	 * Puts a Player p into a CreativeHuntMap
	 * @param p Player to be put into CreativeHuntMap
	 * @return boolean Whether or not Player was successfully added
	 */
	public boolean put(Player p, Player target) {
		if (contains(p)) {
			if (debug) plugin.log(p.getName()+"tried to be put in CreativeHunt mode and they were already in it");
			return false;
		}

		else {
			ArrayList<Object> timerTargetList = new ArrayList<Object>();
			timerTargetList.add(new GamemodeRunnable(plugin, p));
			timerTargetList.add(target);
			timerTargetList.add(new WorldTracker(plugin, target));
			map.put(p, timerTargetList);
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
		if (debug) plugin.log(p.getName()+"tried to be removed from CreativeHunt mode while they were NOT in it");
		return false;
	}
	/**
	 * Toggles whether or not Player p is in the CreativeHuntMap
	 * @param p Player to toggle in the CreativeHuntMap
	 * @return boolean Whether or not the player is in CreativeHuntMap after the method call
	 */
	public boolean toggle (Player p, Player target) {
		if (contains(p)) {
			return remove(p);
		}
		return put(p, target);
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
		((BukkitTask) map.get(p).get(3)).cancel();
		map.get(p).remove(3);
	}
	
	/**
	 * Start a Survival Timer for Player p
	 * @param p Player to set a timer for
	 */
	public void startSurvivalTimer(Player p) {
		if (!((GamemodeRunnable) map.get(p).get(0)).hasRun()) {
			map.get(p).add(((GamemodeRunnable) map.get(p).get(0)).runTaskLater(plugin, plugin.getConfig().getInt("creative-seconds")*20));
			if (debug) plugin.log("Countdown until survival mode for"+plugin.getConfig().getInt("creative-seconds")+" seconds started for "+p.getName());
		}
		else {
			GamemodeRunnable newRunnable = new GamemodeRunnable(plugin, p);
			map.get(p).set(0, newRunnable);
			newRunnable.runTaskLater(plugin, plugin.getConfig().getInt("creative-seconds")*20);
			if (debug) plugin.log("Countdown until survival mode for"+plugin.getConfig().getInt("creative-seconds")+" seconds started for "+p.getName()+" - new GamemodeRunnable created");
		}
		p.spigot().sendMessage(messageBuilder.build("&7You have &7"+plugin.getConfig().getInt("creative-seconds")+"&7 seconds in creative mode."));
		
	}
	/**
	 * 
	 * @param p
	 * @return
	 */
	public Player getTarget(Player p) {
		return (Player) map.get(p).get(1);
	}
	/**
	 * Checks if a Player p has been targeted by someone in CreativeHunt mode
	 * @param p Player to check whether or not is targeted
	 * @return Player player who is tracking Player p or null if Player p is not being tracked
	 */
	public Player isTargeted (Player p) {
		for(Player player : map.keySet()) {
			if (map.get(player).get(1).equals(p)) return player;
		}
		return null;
	}
	/**
	 * Returns the WorldTracker object for a given tracker so that the WorldTracker can be used
	 * in order to let the given Player tracker's compass a target
	 * @param tracker Player for whom a a WorldTracker object is returned - will be used to give tracker a compass target
	 * @return WorldTracker object used for tracking Player tracker's current target
	 */
	public WorldTracker getWorldTracker(Player tracker) {
		return (WorldTracker) map.get(tracker).get(2);
	}
}
