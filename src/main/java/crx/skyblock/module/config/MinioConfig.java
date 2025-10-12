package crx.skyblock.module.config;

import java.util.Objects;

public class MinioConfig {

    private final String serverUrl;
    private final String accessKey;
    private final String secretKey;

    public MinioConfig(String serverUrl, String accessKey, String secretKey) {
        this.serverUrl = Objects.requireNonNull(serverUrl, "serverUrl cannot be null");
        this.accessKey = Objects.requireNonNull(accessKey, "accessKey cannot be null");
        this.secretKey = Objects.requireNonNull(secretKey, "secretKey cannot be null");
    }

    public String getServerUrl() {
        return serverUrl;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public String getAccessKey() {
        return accessKey;
    }
}
