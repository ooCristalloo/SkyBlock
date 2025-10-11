package crx.skyblock.service.nats;

import java.util.Objects;

public class NatsConfig {

    private final String serverUrl;
    private final String realmId;
    private final String chatSubject;
    
    public NatsConfig(String serverUrl, String realmId, String chatSubject) {
        this.serverUrl = Objects.requireNonNull(serverUrl, "serverUrl cannot be null");
        this.realmId = Objects.requireNonNull(realmId, "realmId cannot be null");
        this.chatSubject = Objects.requireNonNull(chatSubject, "chatSubject cannot be null");
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getRealmId() {
        return realmId;
    }

    public String getChatSubject() {
        return chatSubject;
    }
}