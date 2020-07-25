package online.x16.CreativeHunt;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import online.x16.CreativeHunt.tools.MessageBuilder;

public class CreativeHuntCommand implements CommandExecutor {
	
	private CreativeHunt plugin;
	private MessageBuilder messageBuilder;
	private TrackerCompass tracker;
	
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
		if (args.length == 0) return false;
		if (args.length == 1 && isOnlinePlayer(args[0])) {
			messageBuilder = new MessageBuilder(plugin);
			//Check if CreativeHunt is on or off for Player p as a result of the toggle
			if (plugin.getMap().toggle(p, Bukkit.getPlayer(args[0]))) {
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
		if (args.length > 2 || !(args[0].equalsIgnoreCase("on") ||args[0].equalsIgnoreCase("off"))) return false;
		//If there are 2 arguments, the first is "on", and the second is an online player's name
		if (args[0].equalsIgnoreCase("on") && isOnlinePlayer(args[1])) {
			if (plugin.getMap().put(p, Bukkit.getPlayer(args[1]))) enable(p);
			else p.spigot().sendMessage(messageBuilder.build("&cError: You were already in CreativeHunt mode!"));
			return true;
		}
		//If the first argument is "off"
		else if (args[0].equalsIgnoreCase("off")){
			if (plugin.getMap().remove(p)) disable(p);
			else p.spigot().sendMessage(messageBuilder.build("&cError: You were not in CreativeHunt mode!"));
			return true;
		}
		//Otherwise - the arguments were bad - send the command usage
		else return false;
	}

	public boolean isOnlinePlayer(String str) {
		for (Player p : plugin.getServer().getOnlinePlayers()) {
			if (p.getName().equalsIgnoreCase(str)) return true;
		}
		return false;
	}
	/**
	 * Sends Player p enable messages for CreativeHunt mode and creates a tracker compass
	 * @param p Player to whom messages are sent 
	 */
	public void enable(Player p) {
		p.spigot().sendMessage(messageBuilder.build("&7CreativeHunt enabled for "+p.getDisplayName()+"&7."));
		tracker = new TrackerCompass(plugin);
		tracker.giveTrackerCompass(p);
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
