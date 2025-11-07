package crx.skyblock.repository;

import crx.skyblock.data.profile.Profile;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface ProfileRepository {

    CompletableFuture<Profile> getProfileForUUID(UUID uuid);

    CompletableFuture<Void> setProfile(Profile profile);
}
