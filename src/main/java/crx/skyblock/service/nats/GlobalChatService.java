package crx.skyblock.service.nats;

public interface GlobalChatService {

    void sendChatMessage(String playerName, String message);
}
