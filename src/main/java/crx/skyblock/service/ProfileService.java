package crx.skyblock.service;

import cn.nukkit.Player;
import crx.skyblock.data.profile.Profile;
import crx.skyblock.data.profile.ProfileAffiliation;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ProfileService {
    CompletableFuture<Profile> getProfile(UUID profile);

    CompletableFuture<Void> setProfile(Profile profile);

    CompletableFuture<Profile> createProfile(String name, Player player);


    void onJoin(Player player);

    void onLeave(Player player);
}
