package online.x16.CreativeHunt;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class GamemodeRunnable extends BukkitRunnable {

	private Player p;
	private CreativeHunt plugin;
	private boolean debug;
	private boolean hasRun;
	
	public GamemodeRunnable(CreativeHunt instance, Player player) {
		plugin = instance;
		p = player;
		debug = plugin.getConfig().getBoolean("debug");
	}
	
	@Override
	public void run() {
		p.setGameMode(GameMode.SURVIVAL);
		if (debug) plugin.log("Set gamemode to survival for "+p.getName());
		hasRun = true;
	}

	public boolean hasRun() {
		return hasRun;
	}
	

}
