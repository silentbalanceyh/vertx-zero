package io.vertx.tp.is.refine;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.cv.IsFolder;
import io.vertx.up.atom.Kv;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class IsDir {

    static Kv<String, String> trash(final String path) {
        Objects.requireNonNull(path);
        final String trashTo = Ut.ioPath(IsFolder.TRASH_FOLDER, path);
        return Kv.create(path, trashTo);
    }

    static ConcurrentMap<String, String> trash(final Set<String> pathSet) {
        final ConcurrentMap<String, String> trashMap = new ConcurrentHashMap<>();
        pathSet.forEach(path -> {
            final String trashTo = Ut.ioPath(IsFolder.TRASH_FOLDER, path);
            trashMap.put(path, trashTo);
        });
        return trashMap;
    }

    static Kv<String, String> rollback(final String path) {
        Objects.requireNonNull(path);
        final String trashFrom = Ut.ioPath(IsFolder.TRASH_FOLDER, path);
        return Kv.create(trashFrom, path);
    }

    static ConcurrentMap<String, String> rollback(final Set<String> pathSet) {
        final ConcurrentMap<String, String> trashMap = new ConcurrentHashMap<>();
        pathSet.forEach(path -> {
            final String trashFrom = Ut.ioPath(IsFolder.TRASH_FOLDER, path);
            trashMap.put(trashFrom, path);
        });
        return trashMap;
    }

    static JsonObject input(JsonObject directoryJ) {
        // Cannot deserialize value of type `java.lang.String` from Array value (token `JsonToken.START_ARRAY`)
        directoryJ = directoryJ.copy();
        Ut.ifString(directoryJ,
            KName.METADATA,
            KName.VISIT_GROUP,
            KName.VISIT_ROLE,
            KName.VISIT_MODE
        );
        return directoryJ;
    }

    static JsonArray input(JsonArray directoryJ) {
        directoryJ = directoryJ.copy();
        Ut.ifStrings(directoryJ,
            KName.METADATA,
            KName.VISIT_GROUP,
            KName.VISIT_ROLE,
            KName.VISIT_MODE
        );
        return directoryJ;
    }

    static Future<JsonObject> output(final JsonObject response) {
        return Ut.ifJObject(
            KName.METADATA,
            KName.VISIT_GROUP,
            KName.VISIT_ROLE,
            KName.VISIT_MODE
        ).apply(response).compose(directory -> {
            directory.put(KName.DIRECTORY, Boolean.TRUE);
            Ut.ifJCopy(directory, KName.KEY, KName.DIRECTORY_ID);
            return Ux.future(directory);
        });
    }

    static Future<JsonArray> output(final JsonArray response) {
        return Ut.ifJArray(
            KName.METADATA,
            KName.VISIT_GROUP,
            KName.VISIT_ROLE,
            KName.VISIT_MODE
        ).apply(response).compose(directory -> {
            Ut.itJArray(directory).forEach(each -> {
                each.put(KName.DIRECTORY, Boolean.TRUE);
                Ut.ifJCopy(each, KName.KEY, KName.DIRECTORY_ID);
            });
            return Ux.future(directory);
        });
    }

    static Future<List<IDirectory>> query(final JsonObject condition) {
        return Ux.Jooq.on(IDirectoryDao.class).fetchAsync(condition);
    }

    static Future<List<IDirectory>> query(final IDirectory directory) {
        if (Objects.isNull(directory)) {
            return Ux.futureL();
        } else {
            final JsonObject condition = Ux.whereAnd();
            condition.put(KName.SIGMA, directory.getSigma());
            condition.put(KName.STORE_PATH + ",s", directory.getStorePath());
            return Ux.Jooq.on(IDirectoryDao.class).fetchAsync(condition);
        }
    }

    static Future<List<IDirectory>> query(final JsonArray data, final String storeField, final boolean strict) {
        final String sigma = Ut.valueString(data, KName.SIGMA);
        final JsonArray names = Ut.valueJArray(data, storeField);
        final JsonObject condition = Ux.whereAnd();
        /*
         * sigma and active = true
         */
        condition.put(KName.SIGMA, sigma);
        condition.put(KName.ACTIVE, Boolean.TRUE);
        if (strict) {
            /*
             * strict mode
             * storePath in [?,?,?]
             */
            condition.put(KName.STORE_PATH + ",i", names);
        } else {
            /*
             * non-strict mode
             * storePath start with the shortest
             */
            final String found = names.stream()
                .map(item -> (String) item)
                .reduce((left, right) -> {
                    if (left.length() < right.length()) {
                        return left;
                    } else {
                        return right;
                    }
                }).orElse(null);
            if (Ut.notNil(found)) {
                condition.put(KName.STORE_PATH + ",s", found);
            }
        }
        return Ux.Jooq.on(IDirectoryDao.class).fetchAsync(condition);
    }

    static ConcurrentMap<ChangeFlag, JsonArray> diff(final JsonArray input, final List<IDirectory> directories) {
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
                directoryMap.remove(path);
            } else {
                // ADD Queue
                queueAD.add(json);
            }
        });
        final JsonArray queueDft = new JsonArray();
        if (!directoryMap.isEmpty()) {
            directoryMap.values().forEach(item -> {
                final JsonObject record = Ux.toJson(item);
                record.put(KName.DIRECTORY_ID, item.getKey());
                queueDft.add(record);
            });
        }
        return new ConcurrentHashMap<>() {
            {
                this.put(ChangeFlag.ADD, queueAD);
                this.put(ChangeFlag.UPDATE, queueUP);
                this.put(ChangeFlag.NONE, queueDft);
            }
        };
    }

    public static Future<IDirectory> updateBranch(final String key, final IDirectory directory) {
        final UxJooq jq = Ux.Jooq.on(IDirectoryDao.class);
        return jq.<IDirectory>fetchByIdAsync(key).compose(queried -> {
            if (Objects.isNull(queried)) {
                return Ux.future();
            }
            queried.setUpdatedAt(LocalDateTime.now());
            queried.setUpdatedBy(directory.getUpdatedBy());
            return jq.updateAsync(queried)
                .compose(updated -> updateBranch(updated.getParentId(), directory));
        });
    }
}
