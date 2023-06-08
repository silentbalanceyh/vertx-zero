package io.vertx.mod.is.uca.command;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.is.refine.Is;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractFs implements Fs {
    /*
     *  storeParent calculating
     *  1. Here the integrationId must be the same
     */
    protected Future<JsonArray> initialize(final JsonArray data, final JsonObject config) {
        final JsonArray formatted = data.copy();
        Ut.itJArray(formatted).forEach(json -> json.put(KName.KEY, UUID.randomUUID().toString()));
        /*
         * Group by `storePath`
         * Group by `storeParent`
         */
        final ConcurrentMap<String, JsonObject> stored = Ut.elementMap(formatted, KName.STORE_PATH);
        final ConcurrentMap<String, JsonArray> storeParent = Ut.elementGroup(formatted, KName.STORE_PARENT);
        /*
         * Fetch by `parent`
         */
        return Is.directoryQr(formatted, KName.STORE_PARENT, true).compose(queried -> {
            /*
             * Apply data by storeParent
             */
            final ConcurrentMap<String, IDirectory> storeMap = Ut.elementMap(queried, IDirectory::getStorePath);

            /*
             * storeParent:
             * -- parentPath = Data
             * storeParentMap
             * -- parentPath = Data
             */
            final List<IDirectory> inserted = new ArrayList<>();
            final JsonObject initialize = config.getJsonObject(KName.INITIALIZE, new JsonObject());
            /*
             * Initialized for queueAdd
             */
            storeParent.forEach((pathParent, dataGroup) -> {
                final IDirectory storeObj = storeMap.get(pathParent);
                final JsonObject storeInput = stored.get(pathParent);
                Ut.itJArray(dataGroup).forEach(json -> {
                    final JsonObject dataRecord = json.copy();
                    /*
                     * sync 2 steps
                     * 1. basic information
                     * 2. parent information
                     */
                    final JsonObject directoryJ = this.initialize(dataRecord, initialize);
                    final IDirectory normalized = this.initialize(directoryJ, storeObj, storeInput);

                    inserted.add(normalized);
                });
            });
            return Ux.Jooq.on(IDirectoryDao.class).insertJAsync(inserted);
        });
    }

    protected JsonObject initialize(final JsonObject data, final JsonObject initialize) {
        final JsonObject directoryJ = new JsonObject();
        Ut.valueCopy(directoryJ, data,
            // key for inserted
            KName.KEY,
            // category, name, storePath
            KName.NAME,
            KName.CATEGORY,
            KName.STORE_PATH,
            // active, language, sigma
            KName.ACTIVE,
            KName.LANGUAGE,
            KName.SIGMA,
            // runComponent
            KName.Component.RUN_COMPONENT
        );
        final String USER_SYSTEM = "zero-environment";
        // updatedAt, updatedBy, createdAt, createdBy, owner
        directoryJ.put(KName.UPDATED_AT, Instant.now());
        directoryJ.put(KName.CREATED_AT, Instant.now());
        directoryJ.put(KName.UPDATED_BY, USER_SYSTEM);
        directoryJ.put(KName.CREATED_BY, USER_SYSTEM);
        directoryJ.put(KName.OWNER, USER_SYSTEM);
        // Visit
        directoryJ.put(KName.VISIT, Boolean.FALSE);
        final JsonArray visitMode;
        if (initialize.containsKey(KName.VISIT_MODE)) {
            visitMode = initialize.getJsonArray(KName.VISIT_MODE);
        } else {
            visitMode = new JsonArray().add("r").add("w").add("x");
        }
        directoryJ.put(KName.VISIT_MODE, visitMode.encode());
        return directoryJ;
    }

    protected IDirectory initialize(final JsonObject directoryJ, final IDirectory parentD, final JsonObject parentJ) {
        // Calculated by Parent
        if (Objects.nonNull(parentD)) {
            directoryJ.put(KName.TYPE, parentD.getType());
            directoryJ.put(KName.OWNER, parentD.getOwner());
            directoryJ.put(KName.INTEGRATION_ID, parentD.getIntegrationId());
            directoryJ.put(KName.Component.RUN_COMPONENT, parentD.getRunComponent());
            directoryJ.put(KName.PARENT_ID, parentD.getKey());

            directoryJ.put(KName.VISIT_ROLE, parentD.getVisitRole());
            directoryJ.put(KName.VISIT_GROUP, parentD.getVisitGroup());
            directoryJ.put(KName.VISIT_COMPONENT, parentD.getVisitComponent());
        } else if (Ut.isNotNil(parentJ)) {
            directoryJ.put(KName.PARENT_ID, parentJ.getValue(KName.KEY));
            if (!directoryJ.containsKey(KName.Component.RUN_COMPONENT)) {
                directoryJ.put(KName.Component.RUN_COMPONENT, parentJ.getString(KName.Component.RUN_COMPONENT));
            }
        }
        return this.initTree(directoryJ);
    }
}
