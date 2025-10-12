package crx.skyblock.service.nats;

import crx.skyblock.module.config.NatsConfig;
import io.nats.client.Dispatcher;
import io.nats.client.MessageHandler;

public interface NatsConnectionService extends AutoCloseable {

    Dispatcher createDispatcher(MessageHandler handler);

    void publish(String subject, byte[] data);

    boolean isConnected();

    NatsConfig getConfig();
}
