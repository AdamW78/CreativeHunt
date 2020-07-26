package online.x16.CreativeHunt;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import online.x16.CreativeHunt.tools.MessageBuilder;

public class OnQuitListener implements Listener {
	
	private CreativeHunt plugin;
	private boolean debug;
	private MessageBuilder messageBuilder;
	
	public OnQuitListener(CreativeHunt instance) {
		plugin = instance;
		debug = plugin.getConfig().getBoolean("debug");
	}
	
	@EventHandler
	public void onQuit (PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (plugin.getMap().contains(p)) {
			plugin.getMap().remove(p);
			if (debug) plugin.log(p.getName()+" has quit and was removed from the CreativeHuntMap - they were hunting.");
		}
		else if (plugin.getMap().isTargeted(p) != null) {
			Player targeter = plugin.getMap().isTargeted(p);
			plugin.getMap().remove(targeter);
			if (debug) plugin.log(p.getName()+" has quit and was removed from the CreativeHuntMap - they were being hunted.");
			p.spigot().sendMessage(messageBuilder.build("&7"+targeter.getName()+" &7quit &7while &7you &7were &7hunting &7them "
																		+ "&7- &7you &7have &7been &7removed &7from &7CreativeHunt &7mode."));
		}
	}

}
