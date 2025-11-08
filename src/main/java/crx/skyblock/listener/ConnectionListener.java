package crx.skyblock.listener;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.event.player.PlayerQuitEvent;
import cn.nukkit.level.Level;
import crx.skyblock.Loader;
import crx.skyblock.forms.ProfileSelectForm;
import crx.skyblock.player.PlayerGroupAddition;
import crx.skyblock.util.GetterInterface;

public class ConnectionListener extends MainListener implements GetterInterface {

    @EventHandler
    private void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        Level level = Server.getInstance().getLevelByName("lobby");
        player.teleport(level.getSpawnLocation());
        player.setGamemode(2);

        this.getServer().getScheduler().scheduleDelayedTask(Loader.getInstance(), () -> ProfileSelectForm.sendForm(player), 20);
    }

    @EventHandler
    private void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();

        PlayerGroupAddition addition = (PlayerGroupAddition) player;
        String activeProfile = addition.getActiveProfile();

        Level level = Server.getInstance().getLevelByName(activeProfile);
        if(level != null) {
            this.getMinioModule().getIslandManagerService().uploadIsland(activeProfile);
        }
    }
}
