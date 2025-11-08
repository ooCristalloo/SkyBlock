package crx.skyblock.service.nats;

import cn.nukkit.Server;
import io.nats.client.Dispatcher;
import lombok.extern.slf4j.Slf4j;

import java.nio.charset.StandardCharsets;

@Slf4j
public class GlobalChatServiceImpl implements GlobalChatService {

    private static final String NATS_SUBSCRIBE_ID = "skyblock.global.chat";

    private final NatsConnectionService natsService;
    
    public GlobalChatServiceImpl(NatsConnectionService natsService) {
        this.natsService = natsService;
        setupChatSubscription();
    }
    
    private void setupChatSubscription() {
        Dispatcher dispatcher = natsService.createDispatcher((msg) -> {
            String message = new String(msg.getData(), StandardCharsets.UTF_8);
            broadcast(message);
        });
        
        dispatcher.subscribe(NATS_SUBSCRIBE_ID);
        log.info("Subscribed to global chat: " + NATS_SUBSCRIBE_ID);
    }

    @Override
    public void sendChatMessage(String playerName, String message) {
        String formattedMessage = String.format("[%s] %s: %s", natsService.getRealmId(), playerName, message);
        
        natsService.publish(NATS_SUBSCRIBE_ID, formattedMessage.getBytes(StandardCharsets.UTF_8));

        log.debug("Sent chat message from " + playerName);
    }
    
    private void broadcast(String message) {
        Server.getInstance().broadcastMessage(message);
    }
}