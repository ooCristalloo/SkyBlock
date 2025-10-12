package crx.skyblock.service.nats.services;

import cn.nukkit.Server;
import crx.skyblock.service.nats.NatsConfig;
import crx.skyblock.service.nats.NatsConnectionService;
import io.nats.client.Dispatcher;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class GlobalChatServiceImpl implements GlobalChatService {
    private final NatsConnectionService natsService;
    private final NatsConfig config;
    
    public GlobalChatServiceImpl(NatsConnectionService natsService, NatsConfig config) {
        this.natsService = natsService;
        this.config = config;
        setupChatSubscription();
    }
    
    private void setupChatSubscription() {
        Dispatcher dispatcher = natsService.createDispatcher((msg) -> {
            String message = new String(msg.getData(), StandardCharsets.UTF_8);
            broadcast(message);
        });
        
        dispatcher.subscribe(config.getChatSubject());
        log.info("Subscribed to global chat: " + config.getChatSubject());
    }

    @Override
    public void sendChatMessage(String playerName, String message) {
        String formattedMessage = String.format("[%s] %s: %s", config.getRealmId(), playerName, message);
        
        natsService.publish(config.getChatSubject(), formattedMessage.getBytes(StandardCharsets.UTF_8));

        log.debug("Sent chat message from " + playerName);
    }
    
    private void broadcast(String message) {
        Server.getInstance().broadcastMessage(message);
    }
}