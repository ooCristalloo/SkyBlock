package crx.skyblock.service;

import cn.nukkit.Player;
import crx.skyblock.data.profile.Profile;
import crx.skyblock.data.profile.ProfileAffiliation;
import crx.skyblock.repository.ProfileAffiliationRepository;
import crx.skyblock.repository.ProfileRepository;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class ProfileServiceImpl implements ProfileService {
    private final ProfileRepository profileRepository;
    private final ProfileAffiliationRepository profileAffiliationRepository;

    private final Map<UUID, Profile> profilesCache = new ConcurrentHashMap<>();
    private final Map<UUID, List<ProfileAffiliation>> profileAffiliationCache = new ConcurrentHashMap<>();

    public ProfileServiceImpl(ProfileRepository profileRepository, ProfileAffiliationRepository profileAffiliationRepository) {
        this.profileRepository = profileRepository;
        this.profileAffiliationRepository = profileAffiliationRepository;
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
            if(profilesCache.containsKey(profile.getUuid())) {
                profilesCache.put(profile.getUuid(), profile);
            } else {
                profileRepository.setProfile(profile).join();
            }
        });
    }

    @Override
    public CompletableFuture<Profile> createProfile(String name, Player player) {
        return CompletableFuture.supplyAsync(() -> {
            final Profile profile = new Profile(name, UUID.randomUUID());

            profilesCache.put(profile.getUuid(), profile);
            profileRepository.setProfile(profile).join();

            return profile;
        });
    }

    @Override
    public CompletableFuture<List<ProfileAffiliation>> getProfiles(Player player) {
        return CompletableFuture.supplyAsync(() -> {
            if(profileAffiliationCache.containsKey(player.getUniqueId())) {
                return profileAffiliationCache.get(player.getUniqueId());
            }

            return profileAffiliationRepository.getPlayers(player.getUniqueId()).join();
        });
    }

    @Override
    public void onJoin(Player player) {
        CompletableFuture.runAsync(() -> {
           final List<ProfileAffiliation> affiliations = profileAffiliationRepository.getPlayers(player.getUniqueId()).join();
           profileAffiliationCache.put(player.getUniqueId(), affiliations);

           for(ProfileAffiliation affiliation : affiliations) {
               profilesCache.put(affiliation.getProfile(), profileRepository.getProfileForUUID(affiliation.getProfile()).join());
           }
        });
    }

    @Override
    public void onLeave(Player player) {
        final List<ProfileAffiliation> affiliations = profileAffiliationCache.get(player.getUniqueId());

        for(ProfileAffiliation affiliation : affiliations) {
            profilesCache.remove(affiliation.getProfile());
        }

        profileAffiliationCache.remove(player.getUniqueId());
    }
}
