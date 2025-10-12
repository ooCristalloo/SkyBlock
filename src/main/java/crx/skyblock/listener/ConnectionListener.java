package crx.skyblock.listener;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.player.PlayerPreLoginEvent;

public class ConnectionListener extends MainListener{

    @EventHandler
    private void preLogin(PlayerPreLoginEvent event) {
        Player player = event.getPlayer();
        String playerName = player.getName();
    }
}
