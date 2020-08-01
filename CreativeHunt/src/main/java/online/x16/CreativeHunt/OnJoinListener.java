package online.x16.CreativeHunt;

import online.x16.CreativeHunt.tools.MessageBuilder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinListener implements Listener {

    private final CreativeHunt plugin;
    private final CreativeHuntMap map;

    public OnJoinListener (CreativeHunt instance) {
        plugin = instance;
        map = plugin.getMap();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        if (map.hasOfflineTarget(p) || map.isOfflineTarget(p) != null) {
            map.logOnPlayer(p);
        }
    }
}
