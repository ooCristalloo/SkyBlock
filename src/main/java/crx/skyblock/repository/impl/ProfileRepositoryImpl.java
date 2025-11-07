package crx.skyblock.repository.impl;

import com.mefrreex.jooq.database.IDatabase;
import crx.skyblock.data.profile.Profile;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import crx.skyblock.repository.ProfileRepository;
import org.jooq.Record;
import org.jooq.Result;
import org.jooq.Table;
import org.jooq.impl.DSL;
import org.jooq.impl.SQLDataType;

public class ProfileRepositoryImpl implements ProfileRepository {

    private final IDatabase database;
    private final Table<?> table;

    public ProfileRepositoryImpl(IDatabase database) {
        this.database = database;
        this.table = DSL.table("sb_profile");

        database.getConnection().thenAcceptAsync(connection -> {
            DSL.using(connection)
                    .createTableIfNotExists(table)
                    .column("name", SQLDataType.VARCHAR)
                    .column("uuid", SQLDataType.UUID)
                    .execute();
        }).join();
    }

    private Profile getProfile(Record record) {
        return new Profile(UUID.fromString(record.getValue("uuid", String.class)), record.getValue("name", String.class));
    }

    @Override
    public CompletableFuture<Profile> getProfileForUUID(UUID uuid) {
        return database.getConnection().thenApplyAsync(connection -> {
            Result<Record> result = DSL.using(connection)
                    .select()
                    .from(table)
                    .where(DSL.field("uuid").eq(uuid))
                    .fetch();

            if (!result.isEmpty()) {
                return this.getProfile(result.get(0));
            }

            return null;
        });
    }

    @Override
    public CompletableFuture<Void> setProfile(Profile profile) {
        return database.getConnection().thenAcceptAsync(connection -> {
            int updatedRows = DSL.using(connection).update(table)
                    .set(DSL.field("name"), profile.getName())
                    .where(DSL.field("uuid").eq(profile.getProfileUuid()))
                    .execute();
            if (updatedRows == 0) {
                DSL.using(connection).insertInto(table)
                        .set(DSL.field("name"), profile.getName())
                        .set(DSL.field("uuid"), profile.getProfileUuid())
                        .execute();
            }
        });
    }
}
