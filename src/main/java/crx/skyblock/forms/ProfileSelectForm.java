package crx.skyblock.forms;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Level;
import com.formconstructor.form.SimpleForm;
import crx.skyblock.Loader;
import crx.skyblock.data.profile.Profile;
import crx.skyblock.player.PlayerGroupAddition;
import crx.skyblock.service.minio.IslandsService;
import crx.skyblock.util.GetterInterface;

import java.util.ArrayList;
import java.util.UUID;

public class ProfileSelectForm implements GetterInterface {

    public static void sendForm(Player player) {
        SimpleForm form = new SimpleForm();

        PlayerGroupAddition playerData = (PlayerGroupAddition) player;
        ArrayList<Profile> profilesData =  playerData.getAllPlayerProfiles();
        form.addContent("§t§f Выберите остров для игры§7 [у вас " + profilesData.size() + " остров(ов)]");

        IslandsService islandsService = Loader.getInstance().getMinioModule().getIslandManagerService();

        profilesData.forEach(profile -> {
            form.addButton(profile.getName(), (pl, bt) -> {
                UUID idProfile = profile.getProfileUuid();
                islandsService.connectionPlayer(pl, idProfile);

                PlayerGroupAddition playerGroup = (PlayerGroupAddition) pl;
                playerGroup.setActiveProfile(idProfile.toString());
                Level level = Server.getInstance().getLevelByName(idProfile.toString());
                player.teleport(level.getSpawnLocation());
            });
        });

        form.addButton("добавить новый остров [+]", (pl, bt) -> {
            ProfileCreateForm.sendForm(player);
        });

        form.setCloseHandler(pl -> {
            sendForm(player);
        });

        form.send(player);
    }
}