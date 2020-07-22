package online.x16.CreativeHunt.tools;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;
import online.x16.CreativeHunt.CreativeHunt;

public class MessageBuilder {
	
	private CreativeHunt plugin;
	private static String prefix;
	
	public MessageBuilder (CreativeHunt instance) {
		plugin = instance;
		prefix = plugin.getConfig().getString("prefix");
	}

	public TextComponent build(String str) {
		return new TextComponent(ChatColor.translateAlternateColorCodes('&', prefix+" "+str));
	}
}
