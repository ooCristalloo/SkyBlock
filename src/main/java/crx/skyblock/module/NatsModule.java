package crx.skyblock.module;

import crx.skyblock.service.nats.GlobalChatService;
import crx.skyblock.service.nats.NatsConnectionService;
import crx.skyblock.service.nats.NatsConnectionServiceImpl;
import crx.skyblock.service.nats.GlobalChatServiceImpl;
import lombok.Getter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NatsModule {

    private static final Logger log = LoggerFactory.getLogger(NatsModule.class);
    @Getter
    private NatsConnectionService natsService;
    @Getter
    private GlobalChatService chatService;
    
    public void initialize(String url) {
        try {
            this.natsService = new NatsConnectionServiceImpl(url);
            this.chatService = new GlobalChatServiceImpl(natsService);
                
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