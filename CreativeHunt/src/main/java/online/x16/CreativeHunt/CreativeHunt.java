package online.x16.CreativeHunt;

import org.bukkit.plugin.java.JavaPlugin;

public class CreativeHunt extends JavaPlugin {

    private CreativeModeMap map;
	
	public void onEnable() {
    	getServer().getPluginManager().registerEvents(new OnDeathListener(this), this);
    	map = new CreativeModeMap(this);
    }

    @Override
    public void onDisable() {

    }
    
    public CreativeModeMap getMap() {
    	return map;
    }

}