package online.x16.CreativeHunt;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;

public class OnDeathListener implements Listener {
	
	private CreativeHunt plugin;
	private List<ItemStack> droppedItems;
	
	public OnDeathListener(CreativeHunt instance) {
		plugin = instance;
	}
	
	/**
	 * Check if a player is currently hunting - if not, do nothing - if yes, do the following:
	 * Modify drops on player death so that only a random group of a configurable number of items are dropped
	 * If zero items are to be dropped, clear the item drops and return
	 * If a negative amount of items are to be dropped, drop all items
	 * Also - makes sure a tracker compass is never among the item drops
	 * @param e PLayerDeathEvent for which drops are modified
	 */
	@EventHandler
	public void onDeath (PlayerDeathEvent e) {
		if (!plugin.getMap().contains(e.getEntity())) return;
		int numDrops = plugin.getConfig().getInt("number-dropped-items");
		for (int i = 0; i < e.getDrops().size(); i++) {
			if (e.getDrops().get(i).getType().equals(Material.COMPASS)) {
				e.getDrops().remove(i);
				i--;
			}
		}
		if (e.getDrops().size() == 0) return;
		if (numDrops < 0) return;
		else if (numDrops == 0) {
			e.getDrops().clear();
			return;
		}
		else {
			for (int i = 0; i < numDrops; i++) {
				droppedItems.add(e.getDrops().get((int)(e.getDrops().size()*Math.random())));
				
			}
		}
		e.getDrops().clear();
		for (ItemStack item : droppedItems) {
			e.getDrops().add(item);
		}
	}

}
