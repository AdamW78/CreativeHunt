package online.x16.CreativeHunt;

import org.bukkit.plugin.java.JavaPlugin;

public class CreativeHunt extends JavaPlugin {

    private CreativeHuntMap map;
	
	public void onEnable() {
    	getServer().getPluginManager().registerEvents(new OnRespawnListener(this), this);
    	map = new CreativeHuntMap(this);
    }

    @Override
    public void onDisable() {

    }
    
    public CreativeHuntMap getMap() {
    	return map;
    }

}