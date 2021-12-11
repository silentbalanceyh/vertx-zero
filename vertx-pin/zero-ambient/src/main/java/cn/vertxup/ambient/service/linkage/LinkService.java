package cn.vertxup.ambient.service.linkage;

import cn.vertxup.ambient.domain.tables.daos.XLinkageDao;
import cn.vertxup.ambient.domain.tables.pojos.XLinkage;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
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
        if (Ut.notNil(type)) {
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
            if (Ut.notNil(sourceKey)) {
                criteria.put("sourceKey", sourceKey);
            }
            if (Ut.notNil(targetKey)) {
                criteria.put("targetKey", targetKey);
            }
            return Ux.Jooq.on(XLinkageDao.class).fetchJAsync(criteria);
        }
    }

    @Override
    public Future<JsonArray> saving(final JsonArray batchData, final boolean vector) {
        final List<XLinkage> queueA = new ArrayList<>();
        final List<XLinkage> queueU = new ArrayList<>();
        Ut.itJArray(batchData).forEach(json -> {
            if (json.containsKey("linkKey")) {
                json.remove(KName.KEY);
                queueA.add(Ux.fromJson(json, XLinkage.class));
            } else {
                this.calcKey(json, vector);
                queueU.add(Ux.fromJson(json, XLinkage.class));
            }
        });
        final UxJooq jooq = Ux.Jooq.on(XLinkageDao.class);
        final List<Future<List<XLinkage>>> futures = new ArrayList<>();
        futures.add(jooq.insertAsync(queueA));
        futures.add(jooq.updateAsync(queueU));
        return Ux.thenCombineArrayT(futures).compose(Ux::futureA);
    }

    @Override
    public Future<JsonObject> create(final JsonObject data, final boolean vector) {
        this.calcKey(data, vector);
        return Ux.Jooq.on(XLinkageDao.class).insertJAsync(data);
    }

    private void calcKey(final JsonObject json, final boolean vector) {
        final String sourceKey = json.getString("sourceKey");
        final String targetKey = json.getString("targetKey");
        final String seed;
        if (vector) {
            // Vector ( Un-Sorted )
            final List<String> keys = new ArrayList<>();
            keys.add(sourceKey);
            keys.add(targetKey);
            seed = Ut.fromJoin(keys, Strings.DASH);
        } else {
            // Sorted ( No vector )
            final Set<String> keys = new TreeSet<>();
            keys.add(sourceKey);
            keys.add(targetKey);
            seed = Ut.fromJoin(keys, Strings.DASH);
        }
        final String linkKey = Ut.encryptMD5(seed);
        json.put("linkKey", linkKey);
    }
}
