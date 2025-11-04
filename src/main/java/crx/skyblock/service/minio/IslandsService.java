package crx.skyblock.service.minio;

import cn.nukkit.Player;

public interface IslandsService {

    void connectionPlayer(Player player);

    boolean createIsland(String islandName);

    void downloadIsland(String islandName);

    void uploadIsland(String islandName);

    boolean isHaveIsland(String islandName);
}
