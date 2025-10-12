package crx.skyblock.service.minio;

import cn.nukkit.Server;
import crx.skyblock.module.config.MinioConfig;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MinioConnectionServiceImpl {

    @Getter
    private final MinioClient connection;
    private final MinioConfig config;

    public MinioConnectionServiceImpl(MinioConfig config) {
        this.config = config;
        this.connection = createConnection();
    }

    private MinioClient createConnection() {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(config.getServerUrl())
                    .credentials(config.getAccessKey(), config.getSecretKey())
                    .build();

            log.info("MinIO client is connected");
            minioClient.listBuckets();

            return minioClient;
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            throw new RuntimeException("MinIO connection failed", e);
        }
    }


    public void close() {
        if (connection != null) {
            try {
                connection.close();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Server.getInstance().getLogger().info("Interrupted while closing NATS connection");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }
}
