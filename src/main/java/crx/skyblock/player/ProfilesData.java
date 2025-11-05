package crx.skyblock.player;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

public class ProfilesData {

    @Getter
    @Setter
    private UUID profileId;
    @Getter
    @Setter
    private String name;

    public ProfilesData(UUID profileId, String name) {
        this.profileId = profileId;
        this.name = name;
    }
}