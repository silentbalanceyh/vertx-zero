package io.vertx.tp.is.uca;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class FsKit {

    public static Future<List<IDirectory>> queryDirectory(final JsonArray data, final String storeField) {
        final String sigma = Ut.valueString(data, KName.SIGMA);
        final JsonArray names = Ut.valueJArray(data, storeField);
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.SIGMA, sigma);
        condition.put(KName.STORE_PATH + ",i", names);
        return Ux.Jooq.on(IDirectoryDao.class).fetchAsync(condition);
    }

    public static Future<JsonArray> queryDirectory(final JsonObject condition) {
        return Ux.Jooq.on(IDirectoryDao.class)
            .fetchJAsync(condition)
            .compose(Ut.ifJArray(KName.METADATA, KName.VISIT_GROUP, KName.VISIT_ROLE, KName.VISIT_MODE));
    }

    public static ConcurrentMap<ChangeFlag, JsonArray> compareDirectory(final JsonArray input, final List<IDirectory> directories) {
        /*
         *  IDirectory
         */
        final ConcurrentMap<String, IDirectory> directoryMap = Ut.elementMap(directories, IDirectory::getStorePath);
        final JsonArray queueAD = new JsonArray();
        final JsonArray queueUP = new JsonArray();

        Ut.itJArray(input).forEach(json -> {
            final String path = json.getString(KName.STORE_PATH);
            if (directoryMap.containsKey(path)) {
                // UPDATE Queue
                final JsonObject normalized = Ux.toJson(directoryMap.getOrDefault(path, null));
                queueUP.add(normalized);
            } else {
                // ADD Queue
                queueAD.add(json);
            }
        });
        return new ConcurrentHashMap<>() {
            {
                this.put(ChangeFlag.ADD, queueAD);
                this.put(ChangeFlag.UPDATE, queueUP);
            }
        };
    }
}
