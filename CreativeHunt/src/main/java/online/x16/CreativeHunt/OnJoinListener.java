package online.x16.CreativeHunt;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class OnJoinListener implements Listener {

    private final CreativeHunt plugin;

    public OnJoinListener (CreativeHunt instance) {
        plugin = instance;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        final CreativeHuntMap map = plugin.getMap();
        System.out.println(map.hasOfflineTarget(p));
        System.out.println(map.isOfflineTarget(p));
        if (map.hasOfflineTarget(p) || map.isOfflineTarget(p) != null) {
            map.logOnPlayer(p);
        }
    }
}
