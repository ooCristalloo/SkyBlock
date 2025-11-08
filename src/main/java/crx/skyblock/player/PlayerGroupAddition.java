package crx.skyblock.player;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import crx.sbdata.player.PlayerInterface;
import crx.sbdata.player.annotation.*;
import crx.skyblock.data.profile.Profile;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@PlayerAdditions(
        fields = {
                @PlayerField(
                        name = "profiles",
                        type = String.class,
                        datasource = @PlayerDatasource(
                                datasourceField = "profiles"
                        )
                ),
                @PlayerField(
                        name = "activeProfile",
                        type = String.class
                ),
                @PlayerField(
                        name = "money",
                        type = Integer.class,
                        datasource = @PlayerDatasource(
                                datasourceField = "money"
                        )
                ),
                @PlayerField(
                        name = "tokens",
                        type = Integer.class,
                        datasource = @PlayerDatasource(
                                datasourceField = "tokens"
                        )
                )
        }
)

public interface PlayerGroupAddition extends PlayerInterface {

    @PlayerGetter(name = "profiles")
    Object getProfiles();

    @PlayerSetter(name = "profiles")
    void setProfiles(String profiles);

    @PlayerGetter(name = "activeProfile")
    String getActiveProfile();

    @PlayerSetter(name = "activeProfile")
    void setActiveProfile(String profile);

    @PlayerGetter(name = "money")
    Object getMoneys();

    @PlayerSetter(name = "money")
    void setMoneys(Integer moneys);

    @PlayerGetter(name = "tokens")
    Object getTokens();

    @PlayerSetter(name = "tokens")
    void setTokens(Integer tokens);

    Type UUID_STRING_MAP_TYPE = new TypeToken<Map<UUID, String>>(){}.getType();

    default ArrayList<Profile> getAllPlayerProfiles(){
        Object objectProfiles = this.getProfiles();
        ArrayList<Profile> profiles = new ArrayList<>();

        if(objectProfiles == null){
            return profiles;
        } else {
            String json = (String) objectProfiles;
            Gson gson = new Gson();
            Map<UUID, String> profilesMap = gson.fromJson(json, UUID_STRING_MAP_TYPE);

            for(Map.Entry<UUID, String> entry : profilesMap.entrySet()){
                profiles.add(new Profile(entry.getKey(), entry.getValue()));
            }

            return profiles;
        }
    }

    default void addPlayerProfile(Profile profile){
        Object objectProfiles = this.getProfiles();
        String json = (String) objectProfiles;

        Gson gson = new Gson();
        Map<UUID, String> profilesMap = gson.fromJson(json, UUID_STRING_MAP_TYPE);

        if(profilesMap == null) {
            profilesMap = new HashMap<>();
        }
        profilesMap.put(profile.getProfileUuid(), profile.getName());
        String newJson = gson.toJson(profilesMap);

        this.setProfiles(newJson);
    }

    default void removePlayerProfile(Profile profile){
        Object objectProfiles = this.getProfiles();
        String json = (String) objectProfiles;

        Gson gson = new Gson();
        Map<UUID, String> profilesMap = gson.fromJson(json, UUID_STRING_MAP_TYPE);

        if(profilesMap != null) {
            profilesMap.remove(profile.getProfileUuid());
            String newJson = gson.toJson(profilesMap);
            this.setProfiles(newJson);
        }
    }
}