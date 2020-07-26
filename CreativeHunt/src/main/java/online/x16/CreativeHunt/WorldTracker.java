package online.x16.CreativeHunt;

import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

public class WorldTracker {
	
	private Player player;
	private World curWorld;
	private ArrayList<Location> locations;
	
	public WorldTracker(Player p) {
		player = p;
		locations = new ArrayList<Location>();
		locations.add(p.getLocation());
	}
	/**
	 * Updates a tracked player's current world
	 * Checks if they habe visited that world before, and if not, add that world to their locations ArrayList
	 */
	public void updateCurWorld() {
		curWorld = player.getWorld();
		boolean alreadyVisited = false;
		for (Location playerLoc : locations) {
			if (playerLoc.getWorld().equals(player.getWorld())) alreadyVisited = true;
		}
		if (!alreadyVisited) locations.add(player.getLocation());
	}
	/**
	 * Check to see if a player is in the same world as the curWorld tracker
	 * @param p Player whose world will be compared to curWorld
	 * @return Whether curWorld for the world tracker aw2
	 */
	public boolean inSameWorld(Player p) {
		return p.getWorld().equals(curWorld);
	}
	/**
	 * Return the location for the compass to point to
	 * If in the same world, point at the player
	 * If in a world the player already visited, point at the player's last location in that world
	 * If the player has never visited the world the tracker is in, return null
	 * @param tracker Player whose location will be compared to the tracked player's current location, and then
	 * the list of worlds that the tracked player has visited
	 * @return Location to for the compass to point to 
	 */
	public Location findTrackerLoc(Player tracker) {
		if (curWorld == tracker.getWorld()) return player.getLocation();
		for (Location playerLoc : locations) {
			if (playerLoc.getWorld().equals(tracker.getWorld())) return playerLoc;
		}
		return null;
		
	}
	/**
	 * Update the lastLocation a player has in a world - Should be used when a tracked player changes worlds
	 * @param lastLoc Last location of the tracked player in a world that they teleported out of
	 */
	public void updateWorldLastLoc(Location lastLoc) {
		for (int i = 0; i < locations.size(); i++) {
			if (lastLoc.getWorld().equals(locations.get(i).getWorld())) {
				locations.set(i, lastLoc);
				return;
			}
		}
	}
	
	
}