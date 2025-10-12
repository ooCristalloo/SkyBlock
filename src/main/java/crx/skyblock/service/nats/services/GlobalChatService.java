package crx.skyblock.service.nats.services;

public interface GlobalChatService {
    void sendChatMessage(String playerName, String message);
}
