package crx.skyblock.util;

import cn.nukkit.Server;
import crx.skyblock.Loader;
import crx.skyblock.module.MinioModule;
import crx.skyblock.module.NatsModule;

public interface GetterInterface {

    default Loader getLoader() {
        return Loader.getInstance();
    }

    default Server getServer() {
        return this.getLoader().getServer();
    }

    default MinioModule getMinioModule() {
        return this.getLoader().getMinioModule();
    }

    default NatsModule getNatsModule() {
        return this.getLoader().getNatsModule();
    }
}
