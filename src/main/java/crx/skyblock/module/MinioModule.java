package crx.skyblock.module;

import io.minio.MinioClient;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MinioModule {

    public void initialize() {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint("http://172.17.0.2:9000")
                    .credentials("minio_root", "minio_password_R6gzFszY$u9@>@:8")
                    .build();

            minioClient.listBuckets();
            log.info("Connection MinIO successfully");
        } catch (Exception e) {
            log.error("Failed to connect to Minio", e);
        }
    }
}