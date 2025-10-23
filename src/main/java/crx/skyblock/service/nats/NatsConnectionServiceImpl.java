package crx.skyblock.service.nats;

import cn.nukkit.Server;
import io.nats.client.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
public class NatsConnectionServiceImpl implements NatsConnectionService {

    private final Connection connection;
    
    public NatsConnectionServiceImpl(String natsUrl) {

        try {
            Options options = new Options.Builder()
                    .server(natsUrl)
                    .connectionName(Server.getInstance().getSettings().general().motd())
                    .maxReconnects(-1)
                    .connectionListener((conn, event) -> log.info("NATS connection event: " + event))
                    .build();

            this.connection = Nats.connect(options);
        } catch (IOException | InterruptedException e) {
            log.error("Failed to connect to NATS: " + e.getMessage());
            throw new RuntimeException("NATS connection failed", e);
        }
    }

    @Override
    public void publish(String subject, byte[] data) {
        try {
            connection.publish(subject, data);
        } catch (Exception e) {
            log.warn("Failed to publish message to " + subject + ": " + e.getMessage());
        }
    }

    @Override
    public Dispatcher createDispatcher(MessageHandler handler) {
        return connection.createDispatcher(handler);
    }
    
    @Override
    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                log.warn("Interrupted while closing NATS connection");
            }
        }
    }

    @Override
    public boolean isConnected() {
        return connection != null && Connection.Status.CONNECTED.equals(connection.getStatus());
    }

    @Override
    public String getRealmId() {
        return "";
    }
}