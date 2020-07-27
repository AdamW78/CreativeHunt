package online.x16.CreativeHunt;

import java.util.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.CompassMeta;
import net.md_5.bungee.api.ChatColor;

public class TrackerCompass {
	
	private final CreativeHunt plugin;
	private final boolean debug;
	
	public TrackerCompass(CreativeHunt instance) {
		plugin = instance;
		debug = plugin.getConfig().getBoolean("debug");
	}
	
	public void giveTrackerCompass(Player p) {
		p.getInventory().addItem(createTrackerCompass(p));
	}

	public ItemStack createTrackerCompass(Player p) {
		ItemStack trackerCompass = new ItemStack(Material.COMPASS);
		if (trackerCompass.getItemMeta() instanceof CompassMeta) {
			CompassMeta trackerMeta = (CompassMeta) trackerCompass.getItemMeta();
			trackerMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Tracker Compass");
			ArrayList<String> trackerLore = new ArrayList<String>();
			trackerLore.add(ChatColor.GRAY + "Hunt down your enemies!");
			trackerLore.add(ChatColor.GRAY + "Right-click to update your target");
			trackerMeta.setLore(trackerLore);
			if (plugin.getMap().getWorldTracker(p).findTrackerLoc(p) == null) return null;
			trackerMeta.setLodestoneTracked(false);
			trackerMeta.setLodestone(plugin.getMap().getWorldTracker(p).findTrackerLoc(p));
			trackerCompass.setItemMeta(trackerMeta);
			if (debug) plugin.log("Tracker compass object created");
		}
		return trackerCompass;
	}

}