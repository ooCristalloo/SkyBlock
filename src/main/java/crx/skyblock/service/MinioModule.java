package crx.skyblock.service;

import io.minio.MinioClient;

public class MinioModule {

    public void initialize() {
        try {
            MinioClient minioClient = MinioClient.builder()
                    .endpoint("http://172.17.0.2:9000")
                    .credentials("minio_root", "minio_password_R6gzFszY$u9@>@:8")
                    .build();

            minioClient.listBuckets();
            System.out.println("Connection MinIO successfully");

        } catch (Exception e) {
            System.out.println("Failed connection MinIO: " + e.getMessage());
        }
    }
}