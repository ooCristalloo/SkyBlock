package crx.skyblock.service.minio;

import cn.nukkit.Server;
import crx.skyblock.module.config.MinioConfig;
import io.minio.MinioClient;
import io.minio.errors.*;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class MinioConnectionServiceImpl {

    private final MinioClient connection;
    private final MinioConfig config;

    public MinioConnectionServiceImpl(MinioConfig config) {
        this.config = config;
        this.connection = createConnection();
    }

    private MinioClient createConnection() {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint("http://172.17.0.2:9000")
                    .credentials("minio_root", "minio_password_R6gzFszY$u9@>@:8")
                    .build();

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
