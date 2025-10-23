package crx.skyblock.service.minio;

import cn.nukkit.Player;
import cn.nukkit.Server;
import crx.skyblock.Loader;
import crx.skyblock.util.GetterInterface;
import io.minio.DownloadObjectArgs;
import io.minio.ListObjectsArgs;
import io.minio.Result;
import io.minio.UploadObjectArgs;
import io.minio.messages.Item;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

@Slf4j
public class IslandsServiceImpl implements IslandsService, GetterInterface {
    private static final File worlds = new File("worlds");
    
    @Getter
    private final MinioConnectionService minioService;
    
    private final Path templateIsland;

    public IslandsServiceImpl(MinioConnectionService minioService, Path templateIsland) {
        this.minioService = minioService;
        this.templateIsland = templateIsland;
    }

    public void connectionPlayer(Player player) {
        if(this.isHaveIsland(player.getName())){
            this.downloadIsland(player.getName());
        }else{
            boolean isCreate = this.createIsland(player.getName());

            if(isCreate){
                Server.getInstance().loadLevel(player.getName());
            }

            log.info("Island {} not found, create new island", player.getName());
        }
    }

    @Override
    public boolean createIsland(String islandName) {
        try {
            Files.copy(this.templateIsland, new File(worlds, islandName).toPath(), StandardCopyOption.REPLACE_EXISTING);
            return true;
        } catch (Exception e) {
            log.error("Island failed to create island {}", islandName, e);
            return false;
        }
    }

    @Override
    public void downloadIsland(String islandName) {
        Path targetFolder = new File(worlds, islandName).toPath();

        try {
            Files.createDirectories(targetFolder);
            Files.createDirectories(targetFolder);

            Iterable<Result<Item>> results = minioService.getConnection().listObjects(
                    ListObjectsArgs.builder()
                            .bucket("island")
                            .prefix(islandName + "/")
                            .recursive(true)
                            .build()
            );

            for (Result<Item> result : results) {
                Item item = result.get();
                String objectName = item.objectName();

                String relativePath = objectName.substring(islandName.length() + 1);
                Path localFile = targetFolder.resolve(relativePath);

                Files.createDirectories(localFile.getParent());

                minioService.getConnection().downloadObject(
                        DownloadObjectArgs.builder()
                                .bucket("island")
                                .object(objectName)
                                .filename(localFile.toString())
                                .build()
                );
            }

            Server.getInstance().loadLevel(islandName);
        } catch (Exception e) {
            log.error("Island failed to download island {}", islandName, e);
        }
    }

    @Override
    public void uploadIsland(String islandName) {
        CompletableFuture.runAsync(() -> {
            Server.getInstance().getLevelByName(islandName).save(true);
            Server.getInstance().getScheduler().scheduleDelayedTask(Loader.getInstance(), () -> {
                File targetFolder = new File(worlds, islandName);

                if (targetFolder.exists() && targetFolder.isDirectory()) {
                    try {
                        Files.walk(targetFolder.toPath())
                                .filter(Files::isRegularFile)
                                .forEach(file -> uploadFileToMinio(file, targetFolder.toPath(), islandName));

                        Files.deleteIfExists(targetFolder.toPath());
                    } catch (IOException e) {
                        log.error("Island failed to upload island {}", islandName, e);
                    }
                } else {
                    log.info("Island {} does not exist", islandName);
                }
            }, 5);
        });

    }

    private void uploadFileToMinio(Path file, Path baseFolder, String islandName) {
        try {
            String objectName = islandName + "/" + baseFolder.relativize(file);

            this.minioService.getConnection().uploadObject(
                    UploadObjectArgs.builder()
                            .bucket("island")
                            .object(objectName)
                            .filename(file.toString())
                            .build()
            );
        } catch (Exception e) {
            log.error("Island failed to upload island {}", islandName, e);
        }
    }

    @Override
    public boolean isHaveIsland(String islandName) {
        String prefix = islandName.endsWith("/") ? islandName : islandName + "/";

        try {
            Iterable<Result<Item>> results = minioService.getConnection().listObjects(
                    ListObjectsArgs.builder()
                            .bucket("island")
                            .prefix(prefix)
                            .maxKeys(1)
                            .build()
            );

            return results.iterator().hasNext();
        } catch (Exception e) {
            log.error("Island failed to download island {}", islandName, e);
            return false;
        }
    }
}
