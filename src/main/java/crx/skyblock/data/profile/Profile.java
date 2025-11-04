package crx.skyblock.data.profile;

import lombok.Getter;

import java.util.UUID;

//открые чанки
@Getter
public class Profile {
    private final String name;
    private final UUID uuid;

    public Profile(String name, UUID uuid) {
        this.name = name;
        this.uuid = uuid;
    }
}
