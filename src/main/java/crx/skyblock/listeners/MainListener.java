package crx.skyblock.listeners;

import cn.nukkit.Server;
import cn.nukkit.event.Listener;
import crx.skyblock.GetterInterface;

public class MainListener implements Listener, GetterInterface {

    public MainListener() {
        Server.getInstance().getPluginManager().registerEvents(this, getLoader());
    }
}
