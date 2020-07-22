package online.x16.CreativeHunt;

import java.util.concurrent.Callable;

import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class GamemodeCallable  implements Callable<GamemodeCallable> {

	private Player p;
	
	public GamemodeCallable(Player player) {
		p = player;
	}
	
	@Override
	public GamemodeCallable call() {
		p.setGameMode(GameMode.SURVIVAL);
		return this;
	}
	

}
