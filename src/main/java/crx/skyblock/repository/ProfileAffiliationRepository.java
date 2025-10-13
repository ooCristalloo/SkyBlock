package crx.skyblock.repository;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import crx.skyblock.data.profile.ProfileAffiliation;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ProfileAffiliationRepository {
    CompletableFuture<List<ProfileAffiliation>> getProfiles(Player player);

    CompletableFuture<List<ProfileAffiliation>> getPlayers(UUID profile);
}
