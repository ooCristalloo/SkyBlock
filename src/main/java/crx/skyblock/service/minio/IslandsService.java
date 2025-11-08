package crx.skyblock.service.minio;

import cn.nukkit.Player;

import java.util.UUID;

public interface IslandsService {

    void connectionPlayer(Player player, UUID uuid);

    boolean createIsland(String islandName);

    void downloadIsland(String islandName);

    void uploadIsland(String islandName);

    boolean isHaveIsland(String islandName);
}
