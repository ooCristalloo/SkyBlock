package crx.skyblock.util;

import cn.nukkit.Server;
import crx.skyblock.Loader;

public interface GetterInterface {

    default Loader getLoader() {
        return Loader.getInstance();
    }

    default Server getServer() {
        return this.getLoader().getServer();
    }
}
