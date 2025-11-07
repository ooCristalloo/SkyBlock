package crx.skyblock;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.ConfigSection;
import com.mefrreex.jooq.database.IDatabase;
import com.mefrreex.jooq.database.MySQLDatabase;
import com.mefrreex.jooq.database.SQLiteDatabase;
import crx.sbdata.player.manager.PlayerAdditionManager;
import crx.skyblock.listener.ChatListener;
import crx.skyblock.listener.ConnectionListener;
import crx.skyblock.module.MinioModule;
import crx.skyblock.module.NatsModule;
import crx.skyblock.player.PlayerGroupAddition;
import crx.skyblock.repository.impl.ProfileRepositoryImpl;
import crx.skyblock.service.ProfileService;
import crx.skyblock.service.ProfileServiceImpl;
import lombok.Getter;

import java.io.File;

public class Loader extends PluginBase {

    @Getter
    private NatsModule natsModule;
    @Getter
    private MinioModule minioModule;
    @Getter
    private ProfileService profileService;
    @Getter
    private static Loader instance;

    @Override
    public void onLoad() {
        this.saveDefaultConfig();
    }

    @Override
    public void onEnable() {
        instance = this;

        this.initNats();
        this.initMinio();
        this.initHandlers();

        this.profileService = new ProfileServiceImpl(new ProfileRepositoryImpl(getDatabase(this.getConfig().getSection("profile"))));

        PlayerAdditionManager.registerPlayerAddition(PlayerGroupAddition.class, this);
    }

    @Override
    public void onDisable() {
        if (natsModule != null) {
            natsModule.shutdown();
        }
        if (minioModule != null) {
            minioModule.shutdown();
        }
    }

    private void initNats(){
        this.natsModule = new NatsModule();
        this.natsModule.initialize(this.getConfig().getString("nats-url"));
    }

    private void initMinio(){
        final ConfigSection section = this.getConfig().getSection("minio");

        this.minioModule = new MinioModule();
        this.minioModule.initialize(
                section.getString("url"),  section.getString("username"), section.getString("password"),
                new File(this.getConfig().getString("template-island"))
        );
    }

    private void initHandlers(){
        new ChatListener(this.natsModule.getChatService());
        new ConnectionListener();
    }

    private IDatabase getDatabase(ConfigSection section) {
        final String type = section.getString("type");

        if(type.equals("mysql")) {
            return new MySQLDatabase(section.getString("host"), section.getString("database"), section.getString("username"), section.getString("password"));
        } else if(type.equals("sqlite")) {
            return new SQLiteDatabase(new File(getDataFolder(), section.getString("database")));
        }

        throw new RuntimeException("Unknown database type: " + type);
    }
}
