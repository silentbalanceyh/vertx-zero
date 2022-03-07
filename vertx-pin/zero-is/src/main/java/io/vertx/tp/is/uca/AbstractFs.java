package io.vertx.tp.is.uca;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
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
    protected Future<JsonArray> syncDirectory(final JsonArray data, final JsonObject config) {
        /*
         * Group by `storePath`
         * Group by `storeParent`
         */
        final ConcurrentMap<String, JsonObject> stored = Ut.elementMap(data, KName.STORE_PATH);
        final ConcurrentMap<String, JsonArray> storeParent = Ut.elementGroup(data, KName.STORE_PARENT);
        /*
         * Fetch by `parent`
         */
        return FsKit.queryDirectory(data, KName.STORE_PARENT).compose(queried -> {
            /*
             * Apply data by storeParent
             */
            final ConcurrentMap<String, IDirectory> storeParentMap = Ut.elementMap(queried, IDirectory::getStorePath);

            /*
             * storeParent:
             * -- parentPath = Data
             * storeParentMap
             * -- parentPath = Data
             */
            final List<IDirectory> inserted = new ArrayList<>();
            storeParent.forEach((pathParent, dataGroup) -> {
                /*
                 * Initialized for queueAdd
                 */
                final IDirectory storeObj = storeParentMap.get(pathParent);
                final JsonObject storeInput = stored.get(pathParent);
                Ut.itJArray(dataGroup).forEach(json -> {
                    final JsonObject dataRecord = json.copy();
                    dataRecord.put(KName.KEY, UUID.randomUUID());
                    inserted.add(this.syncDirectory(dataRecord, storeObj, storeInput));
                });
            });
            return Ux.Jooq.on(IDirectoryDao.class).insertJAsync(inserted);
        });
    }

    protected IDirectory syncDirectory(final JsonObject data, final IDirectory parentD, final JsonObject parentJ) {
        final JsonObject directoryJ = new JsonObject();
        Ut.elementCopy(directoryJ, data,
            // category, name, storePath
            KName.NAME,
            KName.CATEGORY,
            KName.STORE_PATH,
            // active, language, sigma
            KName.ACTIVE,
            KName.LANGUAGE,
            KName.SIGMA
        );
        final String USER_SYSTEM = "zero-environment";
        // updatedAt, updatedBy, createdAt, createdBy, owner
        directoryJ.put(KName.UPDATED_AT, Instant.now());
        directoryJ.put(KName.CREATED_AT, Instant.now());
        directoryJ.put(KName.UPDATED_BY, USER_SYSTEM);
        directoryJ.put(KName.CREATED_BY, USER_SYSTEM);
        directoryJ.put(KName.OWNER, USER_SYSTEM);
        {
            // Calculated by Parent
            if (Objects.nonNull(parentD)) {
                directoryJ.put(KName.TYPE, parentD.getType());
                directoryJ.put(KName.OWNER, parentD.getOwner());
                directoryJ.put(KName.INTEGRATION_ID, parentD.getIntegrationId());
                directoryJ.put(KName.Component.RUN_COMPONENT, parentD.getRunComponent());
                directoryJ.put(KName.PARENT_ID, parentD.getKey());
            } else if (Ut.notNil(parentJ)) {
                directoryJ.put(KName.PARENT_ID, parentJ.getValue(KName.KEY));
            }
        }
        // Visit
        directoryJ.put("visit", Boolean.TRUE);
        directoryJ.put("visitMode", new JsonArray().add("r").add("w").add("x").encode());
        {
            if (Objects.nonNull(parentD)) {
                directoryJ.put("visitRole", parentD.getVisitRole());
                directoryJ.put("visitGroup", parentD.getVisitGroup());
                directoryJ.put("visitComponent", parentD.getVisitComponent());
            }
        }
        final IDirectory directory = Ux.fromJson(directoryJ, IDirectory.class);
        return this.syncDirectory(directory);
    }

    protected abstract IDirectory syncDirectory(final IDirectory directory);
}
