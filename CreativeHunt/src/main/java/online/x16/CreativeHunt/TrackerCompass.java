package online.x16.CreativeHunt;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import net.md_5.bungee.api.ChatColor;

public class TrackerCompass {
	
	private CreativeHunt plugin;
	
	public TrackerCompass(CreativeHunt instance) {
		plugin = instance;
	}
	
	public void giveTrackerCompass(Player p) {
		ItemStack trackerCompass = new ItemStack(Material.COMPASS);
		ItemMeta trackerMeta = trackerCompass.getItemMeta();
		trackerMeta.setDisplayName(ChatColor.BLUE + "" + ChatColor.BOLD + "Tracker Compass");
		List<String> trackerLore = new ArrayList<String>();
		trackerLore.add(ChatColor.GRAY + "Hunt down your enemies!");
		trackerLore.add(ChatColor.GRAY + "Right-click to update your target");
		trackerMeta.setLore(trackerLore);
		trackerCompass.setItemMeta(trackerMeta);
		p.getInventory().addItem(trackerCompass);
		if (plugin.getMap().getWorldTracker(p).findTrackerLoc(p) == null) return;
		p.setCompassTarget(plugin.getMap().getWorldTracker(p).findTrackerLoc(p));
	}

}