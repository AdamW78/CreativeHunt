package online.x16.CreativeHunt;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GamemodeRunnable extends BukkitRunnable {

	private final Player p;
	private final CreativeHunt plugin;
	private final boolean debug;
	private boolean hasRun = false;
	
	public GamemodeRunnable(CreativeHunt instance, Player player) {
		plugin = instance;
		p = player;
		debug = plugin.getConfig().getBoolean("debug");
	}
	
	@Override
	public void run() {
		if (!hasRun) {
			p.setGameMode(GameMode.SURVIVAL);
			if (debug) plugin.log("Set gamemode to survival for "+p.getName());
			hasRun = false;
		}
	}

	public boolean hasRun() {
		return hasRun;
	}
	

}
