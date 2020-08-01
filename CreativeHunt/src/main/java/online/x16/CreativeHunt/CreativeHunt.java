package online.x16.CreativeHunt;

import java.util.logging.Level;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import online.x16.CreativeHunt.tools.Colorizer;

public class CreativeHunt extends JavaPlugin {

    private CreativeHuntMap map;
    final FileConfiguration config = getConfig();
	
	public void onEnable() {
		config.addDefault("color-log", true);
		config.addDefault("debug", false);
		config.addDefault("creative-seconds", 30);
		config.addDefault("prefix", "&9[CreativeHunt]");
		config.addDefault("number-dropped-items", 5);
		getServer().getPluginManager().registerEvents(new OnRespawnListener(this), this);
		getServer().getPluginManager().registerEvents(new PlayerInteractListener(this), this);
		getServer().getPluginManager().registerEvents(new WorldChangeListener(this), this);
		getServer().getPluginManager().registerEvents(new OnDeathListener(this), this);
        getServer().getPluginManager().registerEvents(new OnJoinListener(this), this);
        getServer().getPluginManager().registerEvents(new OnQuitListener(this), this);
		config.options().copyDefaults(true);
        this.saveDefaultConfig();
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
            getServer().getConsoleSender().sendMessage(Colorizer.colorize("&9[" + getName() + "] &r" + obj));
        } 
        else {
            Bukkit.getLogger().log(Level.INFO, "[" + getName() + "] " + Colorizer.colorize((String) obj).replaceAll("(?)\u00a7([a-f0-9k-or])", ""));
        }
    }

}