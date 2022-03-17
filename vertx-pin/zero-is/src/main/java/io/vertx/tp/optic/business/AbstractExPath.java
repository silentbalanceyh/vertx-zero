package io.vertx.tp.optic.business;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.uca.command.FsHelper;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractExPath implements ExIo {
    // ---------------------- mkdir -----------------------

    protected Future<JsonArray> commandMkdir(final JsonArray queueAd, final JsonObject config) {
        if (Ut.isNil(queueAd)) {
            return Ux.futureA();
        }
        // Injection `runComponent` to replace the default `runComponent`
        if (config.containsKey(KName.Component.RUN_COMPONENT)) {
            Ut.itJArray(queueAd).forEach(json -> json.put(KName.Component.RUN_COMPONENT, config.getString(KName.Component.RUN_COMPONENT)));
        }
        // Group queueAd, Re-Calculate `directoryId` here.
        return FsHelper.componentRun(queueAd, (fs, dataGroup) -> fs.synchronize(dataGroup, config)).compose(inserted -> {
            /*
             * storePath = key
             */
            Ut.itJArray(inserted).forEach(json -> Ut.ifJCopy(json, KName.KEY, KName.DIRECTORY_ID));
            return Ux.future(inserted);
        });
    }

    protected Future<JsonArray> commandMkdir(final JsonArray queueUp, final List<IDirectory> storeList) {
        if (Ut.isNil(queueUp)) {
            return Ux.futureA();
        }

        final ConcurrentMap<String, IDirectory> storeMap = Ut.elementMap(storeList, IDirectory::getStorePath);
        Ut.itJArray(queueUp).forEach(json -> {
            final String storePath = json.getString(KName.STORE_PATH);
            if (Ut.notNil(storePath)) {
                final IDirectory store = storeMap.get(storePath);
                if (Objects.nonNull(store)) {
                    // directoryId
                    json.put(KName.DIRECTORY_ID, store.getKey());
                    /*
                     * visit information
                     * -- visit ( owner )
                     * -- visitMode
                     * -- visitRole
                     * -- visitGroup
                     */
                    json.put(KName.VISIT_MODE, Ut.toJArray(store.getVisitMode()));
                }
            }
        });
        return Ux.future(queueUp);
    }
}
