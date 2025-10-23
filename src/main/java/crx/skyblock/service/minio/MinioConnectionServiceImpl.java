package crx.skyblock.service.minio;

import cn.nukkit.Server;
import io.minio.MinioClient;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class MinioConnectionServiceImpl implements MinioConnectionService {

    private final MinioClient connection;

    public MinioConnectionServiceImpl(String serverUrl, String accessKey, String secretKey) {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint(serverUrl)
                    .credentials(accessKey, secretKey)
                    .build();

            log.info("MinIO client is connected");
            minioClient.listBuckets();

            this.connection = minioClient;
        } catch (IOException | ErrorResponseException | InsufficientDataException | InternalException | InvalidKeyException | InvalidResponseException | NoSuchAlgorithmException | ServerException | XmlParserException e) {
            throw new RuntimeException("MinIO connection failed", e);
        }
    }

    @Override
    public MinioClient getConnection() {
        return connection;
    }

    @Override
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
