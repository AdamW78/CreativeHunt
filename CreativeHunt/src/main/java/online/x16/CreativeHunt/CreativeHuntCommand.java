package online.x16.CreativeHunt;

import org.bukkit.GameMode;
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
		//Check if sender is a player, if not send an error message to the console and return true
		if (!(sender instanceof Player)) { 
			plugin.log("&cError: Must be a player to toggle CreativeHunt mode");
			return true;
		}
		//Create a player object from a cast of the CommandSender sender
		Player p = (Player) sender;
		//Check if CreativeHunt is just being toggled
		if (args.length == 0) {
			messageBuilder = new MessageBuilder(plugin);
			//Check if CreativeHunt is on or off for Player p as a result of the toggle
			if (plugin.getMap().toggle(p)) {
				//It is turned on - send player messages letting them know
				enable(p);
			}
			else {
				//It is turned off - if the player was in creative, put them in survival (only if they have a timer going to set them to survival)
				//Send Player p messages letting them know CreativeHunt mode was disabled
				disable(p);
			}
		}
		//If there are too many arguments or the first argument is anything other than "on" or "off", return false and send command usage
		if (args.length > 1 || !(args[0].equalsIgnoreCase("on") ||args[0].equalsIgnoreCase("off"))) return false;
		if (args[0].equalsIgnoreCase("on")) {
			if (plugin.getMap().put(p)) enable(p);
			else p.spigot().sendMessage(messageBuilder.build("&cError: You were already in CreativeHunt mode!"));
			return true;
		}
		else {
			if (plugin.getMap().remove(p)) disable(p);
			else p.spigot().sendMessage(messageBuilder.build("&cError: You were not in CreativeHunt mode!"));
			return true;
		}
	}
	/**
	 * Sends Player p enable messages for CreativeHunt mode
	 * @param p Player to whom messages are sent 
	 */
	public void enable(Player p) {
		p.spigot().sendMessage(messageBuilder.build("&7CreativeHunt enabled for "+p.getDisplayName()+"&7."));
	}
	/**
	 * Sends Player p disable messages for CreativeHunt mode plus shuts down any ScheduledThreadPoolExecutors and puts them in survival
	 * @param p Player for whom to disable CreativeHunt mode
	 */
	public void disable(Player p) {
		if (p.getGameMode().equals(GameMode.CREATIVE)) plugin.getMap().cancelSurvivalTimer(p);
		p.spigot().sendMessage(messageBuilder.build("&7CreativeHunt disabled for "+p.getDisplayName()+"&7."));
	}
	
}
