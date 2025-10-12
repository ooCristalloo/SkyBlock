package crx.skyblock.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerPreLoginEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Level;
import crx.skyblock.util.GetterInterface;

public class ConnectionListener extends MainListener implements GetterInterface {

    @EventHandler
    private void preLogin(PlayerPreLoginEvent event) {
        Player player = event.getPlayer();
        this.getMinioModule().getIslandManagerService().connectionPlayer(player);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        Level level = Server.getInstance().getLevelByName(player.getName());
        player.teleport(level.getSpawnLocation());
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        this.getMinioModule().getIslandManagerService().uploadIsland(player.getName());
    }
}
