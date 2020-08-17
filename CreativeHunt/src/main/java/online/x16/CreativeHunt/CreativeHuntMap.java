package online.x16.CreativeHunt;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import online.x16.CreativeHunt.tools.MessageBuilder;

public class CreativeHuntMap {

	private final CreativeHunt plugin;
	private final HashMap<Player, ArrayList<Object>> map;
	private final HashMap<UUID, ArrayList<Object>> offlineMap;
	private MessageBuilder messageBuilder;
	private final boolean debug;
	/**
	 * Constructor for CreativeHuntMap so that config values can be passed in through a CreativeHunt instance
	 * @param instance CreativeHunt instance
	 */
	public CreativeHuntMap(CreativeHunt instance) {
		plugin = instance;
		messageBuilder = new MessageBuilder(plugin);
		map = new HashMap<>();
		offlineMap = new HashMap<>();
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
			ArrayList<Object> timerTargetList = new ArrayList<>();
			timerTargetList.add(new GamemodeRunnable(plugin, p));
			timerTargetList.add(target);
			timerTargetList.add(new WorldTracker(plugin, target));
			map.put(p, timerTargetList);
			return true;
		}
	}
	/**
	 * Removes a Player p from a CreativeHuntMap
	 * @param p Player to remove from the CreativeHuntMap
	 * @return boolean Whether or not Player was successfully removed
	 */
	public boolean remove(Player p) {
		if (map.remove(p) != null || offlineMap.remove(p.getUniqueId()) != null) return true;
		if (debug) plugin.log(p.getName()+"tried to be removed from CreativeHunt mode while they were NOT in it");
		return false;
	}
	/**
	 * Toggles whether or not Player p is in the CreativeHuntMap
	 * @param p Player to toggle in the CreativeHuntMap
	 * @return boolean Whether or not the player is in CreativeHuntMap after the method call
	 */
	public boolean toggle (Player p, Player target) {
		if (contains(p) || hasOfflineTarget(p)) return !remove(p);
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
	 * This gets run on the disabling of CreativeHunt mode
	 * If the GamemodeRunnable has NOT already run, run it - otherwise, do nothing
	 * @param p Player to put into survival
	 */
	public void cancelSurvivalTimer(Player p) {
		GamemodeRunnable runnable = (GamemodeRunnable)(map.get(p).get(0));
		if (runnable.hasRun()) {
			((GamemodeRunnable) map.get(p).get(0)).runTask(plugin);
		}
	}
	
	/**
	 * Start a Survival Timer for Player p
	 * @param p Player to set a timer for
	 */
	public void startSurvivalTimer(Player p) {
		//Check if the GamemodeRunnable has NOT already run for Player p
		if (((GamemodeRunnable) map.get(p).get(0)).hasRun()) {
			//Schedule GamemodeRunnable to be run in creative-seconds seconds
			((GamemodeRunnable) map.get(p).get(0)).runTaskLater(plugin, plugin.getConfig().getInt("creative-seconds")*20);
			if (debug) plugin.log("Countdown until survival mode for"
					+plugin.getConfig().getInt("creative-seconds")+" seconds started for "+p.getName());
		}
		//This runs is the GamemodeRunnable HAS already run for Player p
		else {
			//Instantiate a new GamemodeRunnable
			GamemodeRunnable newRunnable = new GamemodeRunnable(plugin, p);
			map.get(p).set(0, newRunnable);
			newRunnable.runTaskLater(plugin, plugin.getConfig().getInt("creative-seconds")*20);
			if (debug) plugin.log("Countdown until survival mode for"+
					plugin.getConfig().getInt("creative-seconds")
					+" seconds started for "+p.getName()+" - new GamemodeRunnable created");
		}
		p.spigot().sendMessage(messageBuilder.build("&7You have &7"+
				plugin.getConfig().getInt("creative-seconds")+"&7 seconds in creative mode."));
		
	}
	/**
	 * Fetches the target of Player p if p is currently tracking someone
	 * @param p Player to find current target for
	 * @return Player currently being targeted by the supplied Player p
	 */
	public Player getTarget(Player p) {
		if (contains(p)) return (Player) map.get(p).get(1);
		return null;
	}

	/**
	 * Fetches the UUID of the target for player with UUID uuid
	 * @param uuid UUID of the player for whom a target will be returned
	 * @return UUID of Player with UUID uuid's target or null if they have no target
	 */
	public UUID getTarget(UUID uuid) {
		if (hasOfflineTarget(uuid)) {
			if (debug) plugin.log("Searching for target of UUID: "+uuid+" in map of "+offlineMap.size()+" players");
			return (UUID) offlineMap.get(uuid).get(1);
		}
		return null;
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
		if (contains(tracker)) {
			return (WorldTracker) map.get(tracker).get(2);
		}
		else if (hasOfflineTarget(tracker)) {
			return (WorldTracker) offlineMap.get(tracker.getUniqueId()).get(2);
		}
		return null;
	}

	/**
	 * Store a player who has logged off while in CreativeHunt mode - either as tracker or the Player being hunted
	 * Put the player and their corresponding ArrayList<Object> in the offlineMap
	 * Update the offline player's last location (if they were being tracked) so that the tracker's compass works
	 * @param p Player to log off and put in the offlineMap
	 * @param lastLoc Location where the Player p logged off - only used if p was being tracked
	 */
	public void logOffPlayer(Player p, Location lastLoc) {
		//Storing Player tracker - isTargeted method either returns null or the player targeting p, so use this to check if p is a target or a tracker
		Player tracker = isTargeted(p);
		//ArrayList that will be fetched from map - This has to be modified to ONLY contain UUIDs and no Player objects
		ArrayList<Object> offlineArrayList;
		//If tracker is NOT null, we know p was being targeted, so we put tracker's UUID as the key in the offlineMap with p's UUID as an object in tracker's ArrayList
		if (tracker != null) {
			//Instantiate messageBuilder to notify tracker that their target p has just logged off
			messageBuilder = new MessageBuilder(plugin);
			tracker.spigot().sendMessage(messageBuilder.build("&7The &7player &7you &7were &7tracking " +
					"&7has &7logged &7off. &7Tracking &7will &7show &7you &7their &7last &7location &7while &7online."));
			//Update tracker's WorldTracker - the lastLoc p had in their current world should be stored as the target for tracker
			getWorldTracker(tracker).updateWorldLastLoc(lastLoc);
			//Remove the entry from map associated with the key tracker, and then store the ArrayList returned and replace p with p.getUniqueID()
			offlineArrayList = map.remove(tracker);
			offlineArrayList.set(1, p.getUniqueId());
			//Put the tracker's UUID in the offlineMap as the key for the modified ArrayList
			offlineMap.put(tracker.getUniqueId(), offlineArrayList);
			if (debug) plugin.log(p.getName()+" has quit and was placed into the offline players map - they were being hunted.");
		}
		//Tracker is null - check that the player being logged off is currently tracking - if not, do nothing
		else if (contains(p)) {
			//Fetch the p's current target and store it - instantiate messageBuilder in order to notify p's target that their tracker p has just logged off
			Player target = getTarget(p);
			plugin.log("Target for player who just logged off was "+target.getName());
			messageBuilder = new MessageBuilder(plugin);
			target.spigot().sendMessage(messageBuilder.build("&7The &7player &7who &7was &7tracking &7you " +
					"&7has &7logged &7off. &7Tracking &7will &7resume &7if &7they &7log &7back &7on."));
			//Remove the entry from map with p as the tracker - store it to replace p's target with the UniqueID of p's target just like above and put p and
			// offlineArrayList into the offlineMap
			offlineArrayList = map.remove(p);
			offlineArrayList.set(1, target.getUniqueId());
			offlineMap.put(p.getUniqueId(), offlineArrayList);
			if (debug) plugin.log(p.getName()+" has quit and was  - they were hunting.");
		}
		//If p was already in the offlineMap when the logOffPLayer call was made, remove p and their target/tracker from the CreativeHuntMap entirely
		else if (hasOfflineTarget(p) || isOfflineTarget(p) != null) {
			if (offlineMap.remove(p.getUniqueId()) == null) {
				offlineMap.remove(isOfflineTarget(p));
				if (debug) plugin.log(p.getName()+" was already in the offlineMap as a target when they logged off " +
						"- they have been completely removed from CreativeHunt mode");
			}
			else if (debug) plugin.log(p.getName()+" was already in the offlineMap as a tracker when they logged off "+
					"- they have been completely removed from CreativeHunt mode");
		}
	}

	/**
	 * Checks if a player went offline while tracking someone in CreativeHunt mode
	 * @param p Player to check to see if they went offline while tracking
	 * @return Boolean for whether Player p went offline while tracking
	 */
	public boolean hasOfflineTarget(Player p) {
		return hasOfflineTarget(p.getUniqueId());
	}

	/**
	 * Checks if a player went offline while tracking someone in CreativeHunt mode
	 * @param uuid UUID of player to check for having gone offline while tracking
	 * @return Boolean for whether a player with UUID uuid went offline while tracking
	 */
	public boolean hasOfflineTarget(UUID uuid) {
		//Run through for-each loop of every single UUID in the offline map - if one matches uuid, then return true - otherwise, return false
		for (UUID playerUUID : offlineMap.keySet()) {
			if (playerUUID.equals(uuid)) return true;
		}
		return false;
	}

	/**
	 * Checks if a player went offline while being tracked by someone in CreativeHunt mode
	 * @param p Player to check to see if they went offline while being tracked
	 * @return Player targeting the inputted player, or null if Player p did not log off while in CreativeHunt mode
	 */
	public UUID isOfflineTarget(Player p) {
		for (UUID tracker : offlineMap.keySet()) {
			if (debug) plugin.log("Comparing offline tracked player with UUID "+((UUID) (offlineMap.get(tracker).get(1)))
					+" to new player logging on "+p.getName()+" with UUID "+ p.getUniqueId());
			if (((UUID) (offlineMap.get(tracker).get(1))).equals(p.getUniqueId())) {
				return tracker;
			}
		}
		return null;
	}

	/**
	 * Move a recently logged on player from offlineMap to map - converts UUIDs to Player objects, puts them into map, and updates WorldTracker locations
	 * @param p Player logging on to be put into map from offlineMap as either a tracker or a target
	 * @throws NullPointerException Thrown when fetching a Player object from the server using a UUID returns null
	 */
	public void logOnPlayer(Player p) throws NullPointerException {
		//Instantiate a messageBuilder to notify players of the new login - store the UUID of the tracker - this will be used to check if p was being tracked or tracking
		messageBuilder = new MessageBuilder(plugin);
		UUID trackerUUID = isOfflineTarget(p);
		//A UUID was found from isOfflineTarget
		ArrayList<Object> onlineArrayList;
		if (trackerUUID != null) {
			//Fetch a player object from the tracker's UUID - we need this to add a Player object to map
			Player tracker = plugin.getServer().getPlayer(trackerUUID);
			//Throw a NullPointerException if fetching tracker from trackerUUID results in tracker being null
			if (tracker == null) throw new NullPointerException("Unable to fetch online player from UUID of recently logged in player's trackerUUID");
			//Notify tracker that their target has logged back on
			tracker.spigot().sendMessage(messageBuilder.build(ChatColor.GRAY + "Your target has logged back on - live tracking resumed."));
			//Fetch the ArrayList for trackerUUID from offlineMap by removing the entry - store the ArrayList and change the UUID to a Player object
			onlineArrayList = offlineMap.remove(trackerUUID);
			onlineArrayList.set(1, p);
			//Put the newly modified ArrayList into map as the value for key tracker - then, update the WorldTracker for tracker
			map.put(tracker, onlineArrayList);
			getWorldTracker(tracker).updateWorldLastLoc(p.getLocation());
		}
		//trackerUUID is null - check if p has a target in offlineMap
		else if (hasOfflineTarget(p)) {
			//p has a target in offlineMap - notify p's target that their tracker p has just logged back on
			if (debug) plugin.log("Begun log on process for "+p.getName()+" - they were tracking and then logged off");
			//Fetch the ArrayList for p's UUID from offlineMap by removing the entry - store the ArrayList and change the UUID at index 1 to a Player created from it
			onlineArrayList = offlineMap.remove(p.getUniqueId());
			onlineArrayList.set(1, plugin.getServer().getPlayer((UUID) onlineArrayList.get(1)));
			//Put the modified ArrayList into map as the value for the key p - then, update the WorldTracker for p
			map.put(p, onlineArrayList);
			getTarget(p).spigot().sendMessage(messageBuilder.build(ChatColor.GRAY + "Your tracker has logged back on - they are now tracking you live."));
			getWorldTracker(p).updateWorldLastLoc((getTarget(p)).getLocation());

		}
	}
}
