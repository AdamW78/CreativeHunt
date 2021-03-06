package online.x16.CreativeHunt;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import online.x16.CreativeHunt.tools.MessageBuilder;

public class CreativeHuntCommand implements CommandExecutor {
	
	private final CreativeHunt plugin;
	private MessageBuilder messageBuilder;
	private final boolean debug;
	
	/**
	 * 
	 * @param instance CreativeHunt to be passed into command on instantiation
	 */
	public CreativeHuntCommand(CreativeHunt instance) {
		plugin = instance;
		debug = plugin.getConfig().getBoolean("debug");
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
		//Check if CreativeHunt is just being turned off
		if (args.length == 0) {
			//Check to see if Player p was already in CreativeHuntMode with either an online or an offline target
			if (plugin.getMap().contains(p) || plugin.getMap().hasOfflineTarget(p)) {
				//Send Player p a message alerting them that CreativeHuntMode was successfully disabled
				if (debug) plugin.log("CreativeHunt successfully disabled for "+sender.getName());
				messageBuilder = new MessageBuilder(plugin);
				disable(p);
				return true;
			}
			else return false;
		}
		else if (args.length == 1 && isOnlinePlayer(args[0])) {
			messageBuilder = new MessageBuilder(plugin);
			if (debug) plugin.log(sender.getName()+" has just used command and targeted a valid online player");
			//Check if CreativeHunt is on or off for Player p as a result of the toggle
			if (plugin.getMap().toggle(p, Bukkit.getPlayer(args[0]))) {
				//It is turned on - send player messages letting them know
				enable(p);
				if (debug) plugin.log("CreativeHunt successfully enabled for "+sender.getName());
			}
			else {
				//It is turned off - if the player was in creative, put them in survival (only if they have a timer going to set them to survival)
				//Send Player p messages letting them know CreativeHunt mode was disabled
				disable(p);
				if (debug) plugin.log("CreativeHunt successfully disabled for "+sender.getName());
			}
			return true;
		}
		//If there are too many arguments or the first argument is anything other than "on" or "off", return false and send command usage
		else if (args.length > 2 || !(args[0].equalsIgnoreCase("on") ||args[0].equalsIgnoreCase("off"))) return false;
		//If there are 2 arguments, the first is "on", and the second is an online player's name
		else if (args[0].equalsIgnoreCase("on") && isOnlinePlayer(args[1])) {
			messageBuilder = new MessageBuilder(plugin);
			if (plugin.getMap().put(p, Bukkit.getPlayer(args[1]))) {
				enable(p);
				if (debug) plugin.log("CreativeHunt successfully enabled for "+sender.getName());
			}
			else p.spigot().sendMessage(messageBuilder.build("&cError: You were already in CreativeHunt mode!"));
			return true;
		}
		//If the first argument is "off"
		else if (args[0].equalsIgnoreCase("off")){
			messageBuilder = new MessageBuilder(plugin);
			if (plugin.getMap().contains(plugin.getServer().getPlayer(args[1]))
					|| plugin.getMap().hasOfflineTarget(plugin.getServer().getPlayer(args[1]))) {
				plugin.getMap().remove(p);
				disable(p);
				if (debug) plugin.log("CreativeHunt successfully disabled for "+sender.getName());
			}
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
		p.setGameMode(GameMode.CREATIVE);
		plugin.getMap().startSurvivalTimer(p);
		TrackerCompass tracker = new TrackerCompass(plugin);
		tracker.giveTrackerCompass(p);
	}
	/**
	 * Sends Player p disable messages for CreativeHunt mode plus shuts down any ScheduledThreadPoolExecutors and puts them in survival
	 * @param p Player for whom to disable CreativeHunt mode
	 */
	public void disable(Player p) {
		plugin.getMap().remove(p);
		if (p.getGameMode().equals(GameMode.CREATIVE)) plugin.getMap().cancelSurvivalTimer(p);
		p.spigot().sendMessage(messageBuilder.build("&7CreativeHunt disabled for "+p.getDisplayName()+"&7."));
	}
	
}
