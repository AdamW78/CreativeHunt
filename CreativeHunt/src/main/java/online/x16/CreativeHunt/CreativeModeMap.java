package online.x16.CreativeHunt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;

import org.bukkit.entity.Player;

public class CreativeModeMap {

	private CreativeHunt plugin;
	private HashMap<Player, ScheduledThreadPoolExecutor> map;
	
	public CreativeModeMap(CreativeHunt instance) {
		plugin = instance;
	}
	
	public boolean contains(Player p) {
		return map.containsKey(p);
	}
}
