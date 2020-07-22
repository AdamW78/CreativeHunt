package online.x16.CreativeHunt;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import online.x16.X16AutoShop.tools.Colorizer;

public class CreativeHunt extends JavaPlugin {

    private CreativeHuntMap map;
	
	public void onEnable() {
    	getServer().getPluginManager().registerEvents(new OnRespawnListener(this), this);
    	this.getCommand("creativehunt").setExecutor(new CreativeHuntCommand(this));
    	map = new CreativeHuntMap(this);
    }

    @Override
    public void onDisable() {

    }
    
    public CreativeHuntMap getMap() {
    	return map;
    }
    
	public void log(Object obj) {
        if(getConfig().getBoolean("color-logs", true)){
            getServer().getConsoleSender().sendMessage(Colorizer.colorize("&3[&d" + getName() + "&3] &r" + obj));
        } 
        else {
            Bukkit.getLogger().log(Level.INFO, "[" + getName() + "] " + Colorizer.colorize((String) obj).replaceAll("(?)\u00a7([a-f0-9k-or])", ""));
        }
    }

}