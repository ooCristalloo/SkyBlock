package crx.skyblock.forms;

import cn.nukkit.Player;
import com.formconstructor.form.SimpleForm;
import crx.skyblock.player.PlayerGroupAddition;
import crx.skyblock.player.ProfilesData;

import java.util.ArrayList;
import java.util.UUID;

public class ProfileSelectForm {

    public static void sendForm(Player player) {
        SimpleForm form = new SimpleForm();

        PlayerGroupAddition playerData = (PlayerGroupAddition) player;
        ArrayList<ProfilesData> profilesData =  playerData.getAllProfiles();
        form.addContent("§t§f Выберите профиль для игры§7 [у вас " + profilesData.size() + " профилей]");

        profilesData.forEach(profile -> {
            form.addButton(profile.getName(), (pl, bt) -> {
                UUID idProfile = profile.getProfileId();
            });
        });
    }
}
