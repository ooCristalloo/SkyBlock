package crx.skyblock.forms;

import cn.nukkit.Player;
import com.formconstructor.form.CustomForm;
import com.formconstructor.form.element.custom.Input;
import crx.skyblock.Loader;
import crx.skyblock.data.profile.Profile;
import crx.skyblock.player.PlayerGroupAddition;
import crx.skyblock.util.GetterInterface;
import java.util.UUID;

public class ProfileCreateForm implements GetterInterface {

    //TODO сделать ограничение на имя острова
    public static void sendForm(Player player) {
        CustomForm form = new CustomForm("Создать новый остров");

        form.addElement("input", new Input("Название острова (3-15 символов, английские буквы / цифры, одним словом)")
                        .setPlaceholder("Text")
                        .setDefaultValue(""));

        form.setSubmitButton("создать");

        form.setCloseHandler(pl -> {
            ProfileSelectForm.sendForm(player);
        });

        form.setHandler((pl, response) -> {
            String input = response.getInput("input").getValue();
            if(isValidString(input)) {
                createIsland(player, input);
                player.sendMessage("Вы успешно создали остров " + input);
                ProfileSelectForm.sendForm(player);
            }else{
                form.send(player);
            }
        });

        form.send(player);
    }

    private static void createIsland(Player player, String name) {
        Profile profile = new Profile(UUID.randomUUID(), name);
        PlayerGroupAddition playerData = (PlayerGroupAddition) player;
        playerData.addPlayerProfile(profile);
        Loader.getInstance().getProfileService().setProfile(profile);
    }

    public static boolean isValidString(String input) {
        if (input == null) {
            return false;
        }

        if (input.length() < 3 || input.length() > 15) {
            return false;
        }

        if (input.contains(" ")) {
            return false;
        }

        return input.matches("^[a-zA-Z0-9]+$");
    }
}
