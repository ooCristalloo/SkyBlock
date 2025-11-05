package crx.skyblock.data.profile;

import cn.nukkit.IPlayer;
import lombok.Getter;

import java.util.UUID;


@Getter
public class ProfileAffiliation {

    private final UUID profile;
    private final StatusPlayerProfile statusPlayerProfile;
    private final IPlayer player;

    public ProfileAffiliation(UUID profile, StatusPlayerProfile statusPlayerProfile, IPlayer player) {
        this.profile = profile;
        this.statusPlayerProfile = statusPlayerProfile;
        this.player = player;
    }

}
