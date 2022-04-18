package io.vertx.tp.optic;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.optic.feature.Atom;
import io.vertx.up.commune.Record;
import io.vertx.up.experiment.meld.HDao;
import io.vertx.up.unity.Ux;

import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AtomAction implements Atom {
    @Override
    public Future<JsonObject> createAsync(final String identifier, final JsonObject data) {
        final Record record = Ao.toRecord(identifier, data);
        final HDao dao = Ao.toDao(identifier);
        return dao.insertAsync(record).compose(Ux::futureJ);
    }

    @Override
    public Future<JsonObject> updateAsync(final String identifier, final String key, final JsonObject data) {
        return this.fetchRecord(identifier, key).compose(queried -> {
            if (Objects.isNull(queried)) {
                return Ux.futureJ();
            } else {
                final HDao dao = Ao.toDao(identifier);
                queried.set(data);
                return dao.updateAsync(queried).compose(Ux::futureJ);
            }
        });
    }

    @Override
    public Future<JsonObject> fetchAsync(final String identifier, final String key) {
        return this.fetchRecord(identifier, key).compose(Ux::futureJ);
    }

    private Future<Record> fetchRecord(final String identifier, final String key) {
        Objects.requireNonNull(key);
        final HDao dao = Ao.toDao(identifier);
        return dao.fetchByIdAsync(key);
    }

    @Override
    public Future<JsonArray> createAsync(final String identifier, final JsonArray data) {
        final Record[] record = Ao.toRecord(identifier, data);
        final HDao dao = Ao.toDao(identifier);
        return dao.insertAsync(record).compose(Ux::futureA);
    }

    @Override
    public Future<JsonArray> updateAsync(final String identifier, final Set<String> keys, final JsonArray data) {
        return this.fetchRecord(identifier, keys).compose(records -> {
            // Updated
            final Record[] recordList = Ux.updateR(records, data);
            final HDao dao = Ao.toDao(identifier);
            return dao.updateAsync(recordList).compose(Ux::futureA);
        });
    }

    @Override
    public Future<JsonArray> fetchAsync(final String identifier, final Set<String> keys) {
        return this.fetchRecord(identifier, keys).compose(Ux::futureA);
    }

    private Future<Record[]> fetchRecord(final String identifier, final Set<String> key) {
        Objects.requireNonNull(key);
        final HDao dao = Ao.toDao(identifier);
        return dao.fetchByIdAsync(key.toArray(new String[]{}));
    }
}
