package crx.skyblock.module;

import cn.nukkit.Server;
import crx.skyblock.service.GlobalChatService;
import crx.skyblock.module.config.NatsConfig;
import crx.skyblock.service.NatsConnectionService;
import crx.skyblock.service.impl.NatsConnectionServiceImpl;
import crx.skyblock.service.impl.GlobalChatServiceImpl;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NatsModule {

    private static final Logger log = LoggerFactory.getLogger(NatsModule.class);
    private NatsConnectionService natsService;
    @Getter
    private GlobalChatService chatService;
    
    public void initialize() {
        try {
            NatsConfig config = new NatsConfig(
                "nats://149.202.89.181:4222",
                    Server.getInstance().getSettings().general().motd(),
                "minecraft.global.chat"
            );
            
            this.natsService = new NatsConnectionServiceImpl(config);
            this.chatService = new GlobalChatServiceImpl(natsService, config);
                
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize NATS module", e);
        }
    }
    
    public void shutdown() {
        if (natsService != null) {
            try {
                natsService.close();
            } catch (Exception e) {
                log.warn("Failed to close NATS module", e);
            }
        }
    }
}