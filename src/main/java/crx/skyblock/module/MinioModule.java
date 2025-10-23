package crx.skyblock.module;

import crx.skyblock.service.minio.IslandsService;
import crx.skyblock.service.minio.IslandsServiceImpl;
import crx.skyblock.service.minio.MinioConnectionService;
import crx.skyblock.service.minio.MinioConnectionServiceImpl;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

@Slf4j
public class MinioModule {

    @Getter
    private MinioConnectionService minioService;
    @Getter
    private IslandsService islandManagerService;

    public void initialize(String url, String username, String password, Path templateIsland) {
        try {
            this.minioService = new MinioConnectionServiceImpl(url, username, password);
            this.islandManagerService = new IslandsServiceImpl(this.minioService, templateIsland);
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