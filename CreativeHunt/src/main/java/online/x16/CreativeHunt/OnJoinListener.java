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

    /**
     * Fetch the Player from teh event
     * @param e
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        final CreativeHuntMap map = plugin.getMap();
        if (map.hasOfflineTarget(p) || map.isOfflineTarget(p) != null) {
            plugin.log(p.getName()+" has just logged back in after either being hunted and logging off " +
                    "or hunting and logging off");
            map.logOnPlayer(p);
        }
    }
}
