package online.x16.CreativeHunt;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.entity.Player;

public class CreativeModeMap {

	private HashMap<Player, ArrayList<Object>> map;
	
	public CreativeModeMap() {
		map = new HashMap<Player, ArrayList<Object>>();
	}
}
