package crx.skyblock.module;

import crx.skyblock.module.config.MinioConfig;
import crx.skyblock.service.minio.IslandManagerService;
import crx.skyblock.service.minio.MinioConnectionServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MinioModule {

    @Getter
    private MinioConnectionServiceImpl minioService;
    @Getter
    private IslandManagerService islandManagerService;

    public void initialize() {
        try {
            MinioConfig config = new MinioConfig(
                    "http://172.17.0.2:9000",
                    "minio_root",
                    "minio_password"
            );

            this.minioService = new MinioConnectionServiceImpl(config);
            this.islandManagerService = new IslandManagerService(this.minioService);
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