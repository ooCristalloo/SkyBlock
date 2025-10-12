package crx.skyblock.service.minio;

import cn.nukkit.Player;
import cn.nukkit.Server;
import crx.skyblock.util.GetterInterface;
import io.minio.ListObjectsArgs;
import io.minio.Result;
import io.minio.UploadObjectArgs;
import io.minio.messages.Item;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Slf4j
public class IslandManagerService implements GetterInterface {

    @Getter
    private final MinioConnectionServiceImpl minioService;

    public IslandManagerService(MinioConnectionServiceImpl minioService) {
        this.minioService = minioService;
    }

    public void connectionPlayer(Player player) {
        if(this.isHaveIsland(player.getName())){
            this.uploadIsland(player.getName());
        }else{
            boolean isCreate = this.createIsland(player.getName());

            if(isCreate){
                Server.getInstance().loadLevel(player.getName());
            }

            log.info("Island " + player.getName() + " not found, create new island");
        }
    }

    public boolean createIsland(String islandName) {
        Path sourceFolder = Paths.get("/home/users/konovalov/skyblock/worldExample/example");
        Path targetFolder = Paths.get("/home/users/konovalov/skyblock/sb-1/worlds/" + islandName);

        try {
            Files.walk(sourceFolder)
                    .forEach(source -> {
                        try {
                            Path destination = targetFolder.resolve(sourceFolder.relativize(source));

                            if (Files.isDirectory(source)) {
                                Files.createDirectories(destination);
                            } else {
                                Files.copy(source, destination, StandardCopyOption.REPLACE_EXISTING);
                            }
                        } catch (IOException e) {
                            throw new RuntimeException("Ошибка копирования: " + e.getMessage(), e);
                        }
                    });

            System.out.println("Папка успешно скопирована и переименована в: " + islandName);
            return true;

        } catch (Exception e) {
            System.err.println("Ошибка при копировании: " + e.getMessage());
            return false;
        }
    }

    public void downloadIsland(String islandName) {
    }

    public void uploadIsland(String islandName) {
        Server.getInstance().getLevelByName(islandName).save();
        Path targetFolder = Paths.get("/home/users/konovalov/skyblock/sb-1/worlds/" + islandName);

        if (Files.exists(targetFolder) && Files.isDirectory(targetFolder)) {
            try {
                Files.walk(targetFolder)
                        .filter(Files::isRegularFile)
                        .forEach(file -> uploadFileToMinio(file, targetFolder, islandName));

                System.out.println("Folder " + islandName + " upload success");
            } catch (IOException e) {
                System.err.println("Error read folder: " + e.getMessage());
            }
        } else {
            System.out.println("Folder " + islandName + " does not exist");
        }
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

            System.out.println("File is upload: " + objectName);

        } catch (Exception e) {
            System.err.println("error " + file + ": " + e.getMessage());
        }
    }
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
            e.printStackTrace();
            return false;
        }
    }
}
