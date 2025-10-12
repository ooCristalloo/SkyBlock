package crx.skyblock.service;

import cn.nukkit.Server;
import crx.skyblock.service.nats.NatsConfig;
import crx.skyblock.service.nats.NatsConnectionService;
import crx.skyblock.service.nats.services.GlobalChatService;
import crx.skyblock.service.nats.services.GlobalChatServiceImpl;
import lombok.Getter;

public class NatsModule {

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
            
            this.natsService = new NatsConnectionService(config);
            this.chatService = new GlobalChatServiceImpl(natsService, config);
                
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize NATS module", e);
        }
    }
    
    public void shutdown() {
        if (natsService != null) {
            natsService.close();
        }
    }
}