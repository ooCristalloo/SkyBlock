package crx.skyblock.service;

import cn.nukkit.Player;
import crx.skyblock.data.profile.Profile;
import crx.skyblock.player.PlayerGroupAddition;
import crx.skyblock.repository.ProfileRepository;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileServiceImpl implements ProfileService {

    private final ProfileRepository profileRepository;

    private final Map<UUID, Profile> profilesCache = new ConcurrentHashMap<>();

    public ProfileServiceImpl(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
    }

    @Override
    public CompletableFuture<Profile> getProfile(UUID profile) {
        return CompletableFuture.supplyAsync(() -> {
            if(profilesCache.containsKey(profile)) {
                return profilesCache.get(profile);
            }
            return profileRepository.getProfileForUUID(profile).join();
        });
    }

    @Override
    public CompletableFuture<Void> setProfile(Profile profile) {
        return CompletableFuture.runAsync(() -> {
            if(profilesCache.containsKey(profile.getProfileUuid())) {
                profilesCache.put(profile.getProfileUuid(), profile);
            } else {
                profileRepository.setProfile(profile).join();
            }
        });
    }

    @Override
    public CompletableFuture<Profile> createProfile(String name, Player player) {
        return CompletableFuture.supplyAsync(() -> {
            final Profile profile = new Profile(UUID.randomUUID(), name);

            profilesCache.put(profile.getProfileUuid(), profile);
            profileRepository.setProfile(profile).join();

            return profile;
        });
    }

    @Override
    public void onJoin(Player player) {
        final PlayerGroupAddition playerGroupAddition = (PlayerGroupAddition) player;
        for(Profile profile : playerGroupAddition.getAllProfiles()) {
            profilesCache.put(profile.getProfileUuid(), profile);
        }
    }

    @Override
    public void onLeave(Player player) {
        final PlayerGroupAddition playerGroupAddition = (PlayerGroupAddition) player;
        for(Profile profile : playerGroupAddition.getAllProfiles()) {
            profilesCache.remove(profile.getProfileUuid());
        }
    }
}
