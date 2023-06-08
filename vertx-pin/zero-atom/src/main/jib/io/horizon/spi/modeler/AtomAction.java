package io.horizon.spi.modeler;

import io.modello.specification.HRecord;
import io.modello.specification.action.HDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.refine.Ao;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AtomAction implements Atom {

    @Override
    public Future<JsonObject> createAsync(final String identifier, final JsonObject data) {
        final HRecord record = Ao.toRecord(identifier, data);
        final HDao dao = Ao.toDao(identifier);
        return dao.insertAsync(record).compose(Ux::futureJ)
            // Normalized Data
            .compose(inserted -> Ux.futureN(data, null, inserted));
    }

    @Override
    public Future<JsonObject> updateAsync(final String identifier, final String key, final JsonObject data) {
        return this.fetchRecord(identifier, key).compose(queried -> {
            if (Objects.isNull(queried)) {
                return Ux.futureJ();
            } else {
                final JsonObject original = queried.toJson();
                final HDao dao = Ao.toDao(identifier);
                queried.set(data);
                return dao.updateAsync(queried).compose(Ux::futureJ)
                    // Normalized Data
                    .compose(updated -> Ux.futureN(data, original, updated));
            }
        });
    }

    @Override
    public Future<JsonObject> fetchAsync(final String identifier, final String key) {
        return this.fetchRecord(identifier, key).compose(Ux::futureJ);
    }

    private Future<HRecord> fetchRecord(final String identifier, final String key) {
        Objects.requireNonNull(key);
        final HDao dao = Ao.toDao(identifier);
        return dao.fetchByIdAsync(key);
    }

    @Override
    public Future<JsonArray> createAsync(final String identifier, final JsonArray data) {
        final HRecord[] record = Ao.toRecord(identifier, data);
        final HDao dao = Ao.toDao(identifier);
        return dao.insertAsync(record).compose(Ux::futureA)
            // Normalized Data
            .compose(inserted -> Ux.futureN(data, inserted));
    }

    @Override
    public Future<JsonArray> updateAsync(final String identifier, final Set<String> keys, final JsonArray data) {
        return this.fetchRecord(identifier, keys).compose(records -> {
            // Updated
            final JsonArray original = Ut.toJArray(records);
            final HRecord[] recordList = Ux.updateR(records, data);
            final HDao dao = Ao.toDao(identifier);
            return dao.updateAsync(recordList).compose(Ux::futureA)
                // Normalized Data
                .compose(updated -> Ux.futureN(original, updated));
        });
    }

    @Override
    public Future<JsonArray> fetchAsync(final String identifier, final Set<String> keys) {
        return this.fetchRecord(identifier, keys).compose(Ux::futureA);
    }

    private Future<HRecord[]> fetchRecord(final String identifier, final Set<String> key) {
        Objects.requireNonNull(key);
        final HDao dao = Ao.toDao(identifier);
        return dao.fetchByIdAsync(key.toArray(new String[]{}));
    }
}
