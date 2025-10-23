package crx.skyblock.service.minio;

import io.minio.MinioClient;

public interface MinioConnectionService extends AutoCloseable {
    MinioClient getConnection();
}
