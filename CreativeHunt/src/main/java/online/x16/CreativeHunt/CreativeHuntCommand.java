package online.x16.CreativeHunt;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import online.x16.CreativeHunt.tools.MessageBuilder;

public class CreativeHuntCommand implements CommandExecutor {
	
	private CreativeHunt plugin;
	private MessageBuilder messageBuilder;
	
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
		plugin.log("&cError: Must be a player to toggle CreativeHunt mode");	
		}
		Player p = (Player) sender;
		if (args.length == 0) {
			messageBuilder = new MessageBuilder(plugin);
			if (plugin.getMap().toggle(p)) {
				messageBuilder.build("&7CreativeHunt enabled for "+p.getDisplayName()+"&7.");
				messageBuilder.build("&7You have &7"+plugin.getConfig().getInt("creative-seconds")+"&7 seconds in creative mode.");
			}
			else {
				messageBuilder.build("&7CreativeHunt disabled for "+p.getDisplayName()+"&7.");
			}
		}
		if (args.length > 1 || !(args[0].equalsIgnoreCase("on") ||args[0].equalsIgnoreCase("off"))) return false;
		   
		   return false;
	}
	
}
