package crx.skyblock.player;

import crx.sbdata.player.PlayerInterface;
import crx.sbdata.player.annotation.*;

@PlayerAdditions(
        fields = {
                @PlayerField(
                        name = "profiles",
                        type = String.class,
                        datasource = @PlayerDatasource(
                                datasourceField = "profiles"
                        )
                )
        }
)

public interface PlayerGroupAddition extends PlayerInterface {

    @PlayerGetter(name = "profiles")
    Object getProfiles();

    @PlayerSetter(name = "profiles")
    void setProfiles(String profiles);

}