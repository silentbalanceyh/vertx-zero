package cn.vertxup.ambient.service.linkage;

import cn.vertxup.ambient.domain.tables.daos.XLinkageDao;
import cn.vertxup.ambient.domain.tables.pojos.XLinkage;
import io.horizon.eon.VString;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class LinkService implements LinkStub {
    @Override
    public Future<JsonArray> fetchByType(final String type, final String sigma) {
        Objects.requireNonNull(sigma);
        final JsonObject criteria = Ux.whereAnd();
        criteria.put(KName.SIGMA, sigma);
        if (Ut.isNotNil(type)) {
            criteria.put(KName.TYPE, type);
        }
        return Ux.Jooq.on(XLinkageDao.class).fetchJAsync(criteria);
    }

    @Override
    public Future<JsonArray> fetchNorm(final String sourceKey, final String targetKey) {
        if (Ut.isNil(sourceKey) && Ut.isNil(targetKey)) {
            return Ux.futureA();
        } else {
            final JsonObject criteria = Ux.whereOr();
            if (Ut.isNotNil(sourceKey)) {
                criteria.put(KName.SOURCE_KEY, sourceKey);
            }
            if (Ut.isNotNil(targetKey)) {
                criteria.put(KName.TARGET_KEY, targetKey);
            }
            return Ux.Jooq.on(XLinkageDao.class).fetchJAsync(criteria);
        }
    }

    @Override
    public Future<JsonArray> saving(final JsonArray batchData, final boolean vector) {
        final List<XLinkage> queueA = new ArrayList<>();
        final List<XLinkage> queueU = new ArrayList<>();
        Ut.itJArray(batchData).forEach(json -> {
            // Cannot deserialize value of type `java.lang.String` from Object value (token `JsonToken.START_OBJECT`)
            this.calcData(json, KName.SOURCE_DATA);
            this.calcData(json, KName.TARGET_DATA);
            if (json.containsKey("linkKey")) {
                // Update Directly
                // json.remove(KName.KEY);
                queueU.add(Ux.fromJson(json, XLinkage.class));
            } else {
                json.remove(KName.KEY);
                this.calcKey(json, vector);
                queueA.add(Ux.fromJson(json, XLinkage.class));
            }
        });
        final UxJooq jooq = Ux.Jooq.on(XLinkageDao.class);
        final List<Future<List<XLinkage>>> futures = new ArrayList<>();
        futures.add(jooq.insertAsync(queueA));
        futures.add(jooq.updateAsync(queueU));
        return Fn.compressL(futures)
            .compose(Ux::futureA)
            .compose(Fn.ofJArray(KName.SOURCE_DATA, KName.TARGET_DATA));
    }

    @Override
    public Future<JsonArray> syncB(final JsonArray data, final JsonArray removed) {
        // Deleting
        final JsonObject condition = new JsonObject();
        condition.put("key,i", removed);
        return Ux.Jooq.on(XLinkageDao.class).deleteByAsync(condition)
            // Saving
            .compose(deleted -> this.saving(data, false));
    }

    @Override
    public Future<JsonObject> create(final JsonObject data, final boolean vector) {
        this.calcKey(data, vector);
        return Ux.Jooq.on(XLinkageDao.class).insertJAsync(data);
    }

    private void calcData(final JsonObject json, final String field) {
        if (json.containsKey(field)) {
            final Object value = json.getValue(field);
            if (value instanceof JsonObject) {
                json.put(field, ((JsonObject) value).encode());
            } else if (value instanceof JsonArray) {
                json.put(field, ((JsonArray) value).encode());
            }
        }
    }

    private void calcKey(final JsonObject json, final boolean vector) {
        final String sourceKey = json.getString(KName.SOURCE_KEY);
        final String targetKey = json.getString(KName.TARGET_KEY);
        final String seed;
        if (vector) {
            // Vector ( Un-Sorted )
            final List<String> keys = new ArrayList<>();
            keys.add(sourceKey);
            keys.add(targetKey);
            seed = Ut.fromJoin(keys, VString.DASH);
        } else {
            // Sorted ( No vector )
            final Set<String> keys = new TreeSet<>();
            keys.add(sourceKey);
            keys.add(targetKey);
            seed = Ut.fromJoin(keys, VString.DASH);
        }
        final String linkKey = Ut.encryptMD5(seed);
        json.put("linkKey", linkKey);
    }
}
