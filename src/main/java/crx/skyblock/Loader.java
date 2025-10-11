package crx.skyblock;

import cn.nukkit.plugin.PluginBase;
import crx.skyblock.listeners.ChatListener;
import crx.skyblock.service.NatsModule;
import lombok.Getter;

public class Loader extends PluginBase {

    private NatsModule natsModule;
    @Getter
    private static Loader instance;

    @Override
    public void onEnable() {
        instance = this;

        this.initNats();
        this.initHandlers();
    }

    private void initNats(){
        this.natsModule = new NatsModule();
        this.natsModule.initialize();
    }

    private void initHandlers(){
        new ChatListener(this.natsModule.getChatService());
    }

    @Override
    public void onDisable() {
        if (natsModule != null) {
            natsModule.shutdown();
        }
    }
}
