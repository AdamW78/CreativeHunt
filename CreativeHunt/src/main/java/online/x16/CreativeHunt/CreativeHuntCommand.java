package online.x16.CreativeHunt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CreativeHuntCommand implements CommandExecutor {
	
	private CreativeHunt plugin;
	
	/**
	 * 
	 * @param instance
	 */
	public CreativeHuntCommand(CreativeHunt instance) {
		plugin = instance;
	}
	
	@Override   
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (!(sender instanceof Player)) {
			
		}
		if (args.length == 0) {
			
		}
		if (args.length > 1 || args.) return false;
		   
		   return false;
	}
	
}
