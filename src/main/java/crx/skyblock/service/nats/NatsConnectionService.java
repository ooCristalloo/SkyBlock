package crx.skyblock.service.nats;

import io.nats.client.*;

import java.io.IOException;
import java.util.logging.Logger;

public class NatsConnectionService implements AutoCloseable {

    private static final Logger LOGGER = Logger.getLogger(NatsConnectionService.class.getName());
    
    private final Connection connection;
    private final NatsConfig config;
    
    public NatsConnectionService(NatsConfig config) {
        this.config = config;
        this.connection = createConnection();
    }
    
    private Connection createConnection() {
        try {
            Options options = new Options.Builder()
                .server(config.getServerUrl())
                .connectionName(config.getRealmId())
                .maxReconnects(-1)
                .connectionListener((conn, event) -> LOGGER.info("NATS connection event: " + event))
                .build();
                
            return Nats.connect(options);
        } catch (IOException | InterruptedException e) {
            LOGGER.severe("Failed to connect to NATS: " + e.getMessage());
            throw new RuntimeException("NATS connection failed", e);
        }
    }
    
    public void publish(String subject, byte[] data) {
        try {
            connection.publish(subject, data);
        } catch (Exception e) {
            LOGGER.warning("Failed to publish message to " + subject + ": " + e.getMessage());
        }
    }
    
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
                LOGGER.warning("Interrupted while closing NATS connection");
            }
        }
    }
    
    public boolean isConnected() {
        return connection != null && Connection.Status.CONNECTED.equals(connection.getStatus());
    }
}