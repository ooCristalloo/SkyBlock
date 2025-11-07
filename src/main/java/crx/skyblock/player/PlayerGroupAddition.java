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

    @PlayerGetter(name = "money")
    Object getMoneys();

    @PlayerSetter(name = "money")
    void setMoneys(Integer moneys);

    @PlayerGetter(name = "tokens")
    Object getTokens();

    @PlayerSetter(name = "tokens")
    void setTokens(Integer tokens);

    default ArrayList<Profile> getAllProfiles(){
        Object objectProfiles = this.getProfiles();
        ArrayList<Profile> profiles = new ArrayList<>();

        if(objectProfiles == null){
            return profiles;
        } else {
            String json = (String) objectProfiles;
            Gson gson = new Gson();
            Type type = new TypeToken<Map<UUID, String>>(){}.getType();
            Map<UUID, String> profilesMap = gson.fromJson(json, type);

            for(Map.Entry<UUID, String> entry : profilesMap.entrySet()){
                profiles.add(new Profile(entry.getKey(), entry.getValue()));
            }

            return profiles;
        }
    }

    default void addProfile(Profile profile){
        Object objectProfiles = this.getProfiles();
        String json = (String) objectProfiles;

        Gson gson = new Gson();
        Type type = new TypeToken<Map<UUID, String>>(){}.getType();
        Map<UUID, String> profilesMap = gson.fromJson(json, type);

        if(profilesMap == null) {
            profilesMap = new HashMap<>();
        }

        profilesMap.put(profile.getProfileUuid(), profile.getName());

        String newJson = gson.toJson(profilesMap);
        this.setProfiles(newJson);
    }

    default void removeProfile(Profile profile){
        Object objectProfiles = this.getProfiles();
        String json = (String) objectProfiles;

        Gson gson = new Gson();
        Type type = new TypeToken<Map<UUID, String>>(){}.getType();
        Map<UUID, String> profilesMap = gson.fromJson(json, type);

        if(profilesMap != null) {
            profilesMap.remove(profile.getProfileUuid());
            String newJson = gson.toJson(profilesMap);
            this.setProfiles(newJson);
        }
    }
}