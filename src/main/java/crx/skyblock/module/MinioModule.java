package crx.skyblock.module;

import crx.skyblock.module.config.MinioConfig;
import crx.skyblock.service.minio.MinioConnectionServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MinioModule {

    @Getter
    private MinioConnectionServiceImpl minioService;

    public void initialize() {
        try {
            MinioConfig config = new MinioConfig(
                    "https://172.17.0.2:9000",
                    "minio_root",
                    "minio_password_R6gzFszY$u9@>@:8"
            );

            this.minioService = new MinioConnectionServiceImpl(config);
        } catch (Exception e) {
            log.error("Failed to connect to Minio", e);
        }
    }

    public void shutdown() {
        if (minioService != null) {
            try {
                minioService.close();
            } catch (Exception e) {
                log.warn("Failed to close MinIO module", e);
            }
        }
    }
}