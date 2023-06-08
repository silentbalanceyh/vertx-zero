package io.vertx.mod.is.refine;

import cn.vertxup.integration.domain.tables.daos.IDirectoryDao;
import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.horizon.atom.common.Kv;
import io.horizon.eon.VString;
import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.is.cv.IsFolder;
import io.vertx.mod.is.uca.command.FsDefault;
import io.vertx.mod.is.uca.command.FsReadOnly;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
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
        Ut.valueToString(directoryJ,
            KName.METADATA,
            KName.VISIT_GROUP,
            KName.VISIT_ROLE,
            KName.VISIT_MODE
        );
        return directoryJ;
    }

    static JsonArray input(JsonArray directoryJ) {
        directoryJ = directoryJ.copy();
        Ut.valueToString(directoryJ,
            KName.METADATA,
            KName.VISIT_GROUP,
            KName.VISIT_ROLE,
            KName.VISIT_MODE
        );
        return directoryJ;
    }

    static Future<JsonObject> output(final JsonObject response) {
        return Fn.ofJObject(
            KName.METADATA,
            KName.VISIT_GROUP,
            KName.VISIT_ROLE,
            KName.VISIT_MODE
        ).apply(response).compose(directory -> {
            directory.put(KName.DIRECTORY, Boolean.TRUE);
            Ut.valueCopy(directory, KName.KEY, KName.DIRECTORY_ID);
            return Ux.future(directory);
        });
    }

    static Future<JsonArray> output(final JsonArray response) {
        return Fn.ofJArray(
            KName.METADATA,
            KName.VISIT_GROUP,
            KName.VISIT_ROLE,
            KName.VISIT_MODE
        ).apply(response).compose(directory -> {
            Ut.itJArray(directory).forEach(each -> {
                each.put(KName.DIRECTORY, Boolean.TRUE);
                Ut.valueCopy(each, KName.KEY, KName.DIRECTORY_ID);
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
            if (Ut.isNotNil(found)) {
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

    /*
     * Update Tree for loop directory here
     * A -> B -> C
     * When you update C,
     * The updatedAt/updatedBy of A/B must be updated at the same time
     * Here the timestamp should be impact to track all the operation came from
     * user for updating
     *
     * 1) Upload File
     * 2) Rename File
     * 3) Create Directory
     * 4) Rename Directory
     * 5) Trash File
     * 6) Trash Directory
     */
    static Future<IDirectory> updateBranch(final String key, final String updatedBy) {
        final UxJooq jq = Ux.Jooq.on(IDirectoryDao.class);
        return jq.<IDirectory>fetchByIdAsync(key).compose(queried -> {
            if (Objects.isNull(queried)) {
                return Ux.future();
            }
            queried.setUpdatedAt(LocalDateTime.now());
            queried.setUpdatedBy(updatedBy);
            return jq.updateAsync(queried)
                .compose(updated -> updateBranch(updated.getParentId(), updatedBy))
                .compose(updated -> Ux.future(queried));
        });
    }

    @SuppressWarnings("all")
    static Future<IDirectory> updateLeaf(final JsonArray directoryA, final JsonObject params) {
        // Query all directory here;
        final List<IDirectory> directories = Ux.fromJson(directoryA, IDirectory.class);
        final List<String> storePath = Ut.toList(params.getJsonArray(KName.STORE_PATH));
        /*
         * The storePath data structure is as following:
         * /xc
         * /xc/catalog/
         * /xc/catalog/name
         *
         * Find the first non-existing directory, Here should be some situations such as:
         * When the directories is empty, it means that non directory related, we could
         * not create any directory because of critical information missing
         */
        if (directories.isEmpty()) {
            return Ux.future();
        }

        /*
         * Map zip the directory list by storePath, the final map should be
         *
         * - storePath = IDirectory
         */
        final ConcurrentMap<String, IDirectory> dirMap = Ut.elementMap(directories, IDirectory::getStorePath);

        /*
         * Get and build root future for parent directory fetch, the first
         * root directory should be root directory and this directory must be
         * existing in your environment.
         * Because of checking on queried list in before step, here the root
         * directory must not be null.
         */
        IDirectory root = null;
        int idxStart = 1;
        for (int idx = 0; idx < storePath.size(); idx++) {
            final String keyRoot = storePath.get(idx);
            root = dirMap.getOrDefault(keyRoot, null);
            if (Objects.nonNull(root)) {
                idxStart = idx + 1;
                break;
            }
        }
        Objects.requireNonNull(root);

        Future<IDirectory> future = Ux.future(root);
        for (int idx = idxStart; idx < storePath.size(); idx++) {
            final String keyPath = storePath.get(idx);
            final IDirectory dirNow = dirMap.getOrDefault(keyPath, null);
            // Parent should not be null
            final JsonObject inputParams = params.copy();
            inputParams.put(KName.STORE_PATH, keyPath);
            if (Objects.isNull(dirNow)) {
                // Add
                future = createChild(future, dirNow, inputParams)
                    .compose(created -> {
                        dirMap.put(created.getStorePath(), created);
                        return Ux.future(created);
                    });
            } else {
                // Update
                future = createChild(future, dirNow, inputParams);
            }
        }
        return future.compose(finished -> {
            final String path = storePath.get(storePath.size() - 1);        // The Last One
            return Ux.future(dirMap.getOrDefault(path, null));
        });
    }

    private static Future<IDirectory> createChild(final Future<IDirectory> futureParent,
                                                  final IDirectory child,
                                                  final JsonObject params) {
        final UxJooq jq = Ux.Jooq.on(IDirectoryDao.class);
        final String updatedBy = params.getString(KName.UPDATED_BY);
        if (Objects.isNull(child)) {
            return futureParent.compose(parent -> {
                final JsonObject parentJ = Ux.toJson(parent);
                final IDirectory created = Ux.fromJson(parentJ, IDirectory.class);


                // key modification
                created.setKey(UUID.randomUUID().toString());
                created.setParentId(parent.getKey());


                // Auditor Processing
                created.setCreatedAt(LocalDateTime.now());
                created.setCreatedBy(updatedBy);
                created.setUpdatedAt(LocalDateTime.now());
                created.setUpdatedBy(updatedBy);

                // ACL Modification Rule
                final JsonArray visitMode = Ut.toJArray(parent.getVisitMode());
                if (!visitMode.contains(KName.Attachment.W)) {
                    visitMode.add(KName.Attachment.W);
                    created.setVisitMode(visitMode.encode());
                }
                if (visitMode.contains(KName.Attachment.W)) {
                    final String componentCls = parent.getRunComponent();
                    if (componentCls.equals(FsReadOnly.class.getName())) {
                        created.setRunComponent(FsDefault.class.getName());
                    }
                }
                created.setMetadata(new JsonObject().encode());

                // name / code / storePath
                final String storePath = params.getString(KName.STORE_PATH);
                String name = storePath.replace(parent.getStorePath(), VString.EMPTY);
                if (name.startsWith("/")) {
                    // Adjustment for name to avoid `/xxxx` format
                    name = name.substring(1);
                }
                created.setStorePath(storePath);
                created.setName(name);
                created.setCode(Ut.encryptMD5(storePath));
                return jq.insertAsync(created).compose(inserted -> {
                    final JsonObject serialized = Ut.toJObject(inserted);
                    return IsFs.run(serialized, fs -> fs.mkdir(serialized))
                        .compose(nil -> Ux.future(inserted));
                });
            });
        } else {
            child.setUpdatedBy(updatedBy);
            child.setUpdatedAt(LocalDateTime.now());
            return jq.updateAsync(child);
        }
    }

}
