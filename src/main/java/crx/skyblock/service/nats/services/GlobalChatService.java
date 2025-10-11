package crx.skyblock.service.nats.services;

import cn.nukkit.Server;
import crx.skyblock.service.nats.NatsConfig;
import crx.skyblock.service.nats.NatsConnectionService;
import io.nats.client.Dispatcher;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class GlobalChatService {

    private static final Logger LOGGER = Logger.getLogger(GlobalChatService.class.getName());
    
    private final NatsConnectionService natsService;
    private final NatsConfig config;
    
    public GlobalChatService(NatsConnectionService natsService, NatsConfig config) {
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
        LOGGER.info("Subscribed to global chat: " + config.getChatSubject());
    }
    
    public void sendChatMessage(String playerName, String message) {
        String formattedMessage = String.format("[%s] %s: %s", config.getRealmId(), playerName, message);
        
        natsService.publish(config.getChatSubject(), formattedMessage.getBytes(StandardCharsets.UTF_8));
        
        LOGGER.fine("Sent chat message from " + playerName);
    }
    
    private void broadcast(String message) {
        Server.getInstance().broadcastMessage(message);
    }
}