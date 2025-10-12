package crx.skyblock;

import cn.nukkit.plugin.PluginBase;
import crx.skyblock.listener.ChatListener;
import crx.skyblock.module.MinioModule;
import crx.skyblock.module.NatsModule;
import lombok.Getter;

public class Loader extends PluginBase {

    private NatsModule natsModule;
    private MinioModule minioModule;
    @Getter
    private static Loader instance;

    @Override
    public void onEnable() {
        instance = this;

        this.initNats();
        this.initMinio();
        this.initHandlers();
    }

    private void initNats(){
        this.natsModule = new NatsModule();
        this.natsModule.initialize();
    }

    private void initMinio(){
        this.minioModule = new MinioModule();
        this.minioModule.initialize();
    }

    private void initHandlers(){
        new ChatListener(this.natsModule.getChatService());
    }

    @Override
    public void onDisable() {
        if (natsModule != null) {
            natsModule.shutdown();
        }
        if (minioModule != null) {
            minioModule.shutdown();
        }
    }
}
