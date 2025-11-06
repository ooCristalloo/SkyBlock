package crx.skyblock.data.profile;

import lombok.Getter;

import java.util.UUID;

@Getter
public class Profile {

    private final UUID profileUuid;
    private final String name;

    public Profile(UUID profileUuid, String name) {
        this.name = name;
        this.profileUuid = profileUuid;
    }
}
