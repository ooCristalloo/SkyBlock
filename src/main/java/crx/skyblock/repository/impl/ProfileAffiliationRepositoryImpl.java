package crx.skyblock.repository.impl;

import cn.nukkit.IPlayer;
import cn.nukkit.Player;
import cn.nukkit.Server;
import com.mefrreex.jooq.database.IDatabase;
import crx.skyblock.data.profile.Profile;
import crx.skyblock.data.profile.ProfileAffiliation;
import crx.skyblock.data.profile.StatusPlayerProfile;
import crx.skyblock.repository.ProfileAffiliationRepository;
import crx.skyblock.repository.ProfileRepository;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class ProfileAffiliationRepositoryImpl implements ProfileAffiliationRepository {
    private final IDatabase database;
    private final Table<?> table;

    public ProfileAffiliationRepositoryImpl(IDatabase database) {
        this.database = database;
        this.table = DSL.table("sb_profile_affiliation");

        database.getConnection().thenAcceptAsync(connection -> {
            DSL.using(connection)
                    .createTableIfNotExists(table)
                    .column("xuid", SQLDataType.UUID)
                    .column("profile", SQLDataType.UUID)
                    .column("role", SQLDataType.VARCHAR)
                    .execute();
        }).join();
    }

    private ProfileAffiliation getProfileAffiliation(Record record) {
        final UUID xuid = UUID.fromString(record.getValue("xuid", String.class));
        final Optional<Player> optionalPlayer = Server.getInstance().getPlayer(xuid);

        return new ProfileAffiliation(
                UUID.fromString(record.getValue("profile", String.class)),
                StatusPlayerProfile.valueOf(record.getValue("role", String.class)),
                optionalPlayer.isPresent() ? optionalPlayer.get() : Server.getInstance().getOfflinePlayer(xuid)
        );
    }

    @Override
    public CompletableFuture<List<ProfileAffiliation>> getProfiles(Player player) {
        return database.getConnection().thenApplyAsync(connection -> {
            Result<Record> result = DSL.using(connection)
                    .select()
                    .from(table)
                    .where(DSL.field("xuid").eq(player.getUniqueId()))
                    .fetch();
            List<ProfileAffiliation> profileAffiliations = new ArrayList<>();

            for(Record record : result) {
                profileAffiliations.add(getProfileAffiliation(record));
            }

            return profileAffiliations;
        });
    }

    @Override
    public CompletableFuture<List<ProfileAffiliation>> getPlayers(UUID profile) {
        return database.getConnection().thenApplyAsync(connection -> {
            Result<Record> result = DSL.using(connection)
                    .select()
                    .from(table)
                    .where(DSL.field("profile").eq(profile))
                    .fetch();
            List<ProfileAffiliation> profileAffiliations = new ArrayList<>();

            for(Record record : result) {
                profileAffiliations.add(getProfileAffiliation(record));
            }

            return profileAffiliations;
        });
    }
}
