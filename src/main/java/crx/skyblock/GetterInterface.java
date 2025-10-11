package crx.skyblock;

import cn.nukkit.Server;

public interface GetterInterface {

    default Loader getLoader() {
        return Loader.getInstance();
    }

    default Server getServer() {
        return this.getLoader().getServer();
    }
}
