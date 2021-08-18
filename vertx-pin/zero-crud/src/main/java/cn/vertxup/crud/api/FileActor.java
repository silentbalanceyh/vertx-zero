package cn.vertxup.crud.api;

import io.vertx.core.Future;
import io.vertx.core.Promise;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.actor.IxActor;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.cv.IxMsg;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.error._409ModuleConflictException;
import io.vertx.tp.error._409MultiModuleException;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.tp.plugin.excel.atom.ExRecord;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.exchange.DictFabric;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Queue
@SuppressWarnings("all")
public class FileActor {

    private static final Annal LOGGER = Annal.get(FileActor.class);

    @Plugin
    private transient ExcelClient client;

    @Address(Addr.File.IMPORT)
    public Future<Envelop> importFile(final Envelop request) {
        /* Import data here for result */
        final String actor = Ux.getString(request);
        final String filename = Ux.getString1(request);

        final Promise<Envelop> promise = Promise.promise();
        final File file = new File(filename);
        if (file.exists()) {
            /* IxConfig */
            final IxModule config = IxPin.getActor(actor);
            /* IxDao */
            final UxJooq jooq = IxPin.getDao(config, request.headers());

            Fn.safeJvm(() -> {
                /*
                 * Read file to inputStream
                 */
                final InputStream inputStream = new FileInputStream(file);
                /*
                 * Set<ExTable>
                 */
                final Set<ExTable> tables = this.client.ingest(inputStream, true);
                /*
                 * The different size
                 */
                final ConcurrentMap<String, Set<ExTable>> tableMap = new ConcurrentHashMap<>();
                tables.stream()
                        /*
                         * Filtered the tables that equal `config.getTabel`
                         */
                        .filter(Objects::nonNull)
                        .filter(item -> Objects.nonNull(item.getName()))
                        .forEach(item -> {
                            if (!tableMap.containsKey(item.getName())) {
                                final Set<ExTable> tableSet = new HashSet<>();
                                tableMap.put(item.getName(), tableSet);
                            }
                            tableMap.get(item.getName()).add(item);
                        });
                if (1 == tableMap.size()) {
                    final String tableName = tableMap.keySet().iterator().next();
                    if (!tableName.equals(config.getTable())) {
                        /*
                         * Module exception here
                         */
                        promise.complete(Envelop.failure(new _409ModuleConflictException(this.getClass(), tableName, config.getTable())));
                    } else {
                        final Set<ExTable> filtered = tableMap.get(tableName);

                        /*
                         * Record extracting and calculated
                         */
                        final List<JsonObject> prepared = new ArrayList<>();
                        filtered.forEach(table -> {
                            final List<JsonObject> records = table.get().stream()
                                    .filter(Objects::nonNull)
                                    .map(ExRecord::toJson)
                                    .map(record -> IxActor.uuid().proc(record, config))
                                    .collect(Collectors.toList());
                            /*
                             * Unique fields must contain values
                             */
                            records.forEach(record -> {
                                /* Header, sigma, appId, appKey */
                                IxActor.header().bind(request).proc(record, config);

                                if (Unity.isMatch(record, config)) {
                                    prepared.add(record);
                                }
                            });
                        });
                        /*
                         * Read dict only once
                         */
                        final Future<JsonArray> result = Unity.fetchDict(request, config).compose(dictMap -> {
                            final DictFabric fabric = Unity.fetchFabric(dictMap, config);
                            /*
                             * Apply default value
                             */
                            final List<Future<JsonObject>> futures = new ArrayList<>();
                            prepared.forEach(record -> {
                                /* Active = true */
                                record.put(KName.ACTIVE, Boolean.TRUE);
                                /* Serial */
                                futures.add(IxActor.serial().bind(request).procAsync(record, config)
                                        .compose(fabric::inFrom)
                                        /* Unique Filters */
                                        .compose(normalized -> IxActor.unique().procAsync(normalized, config))
                                        /* Search result */
                                        .compose(filters -> Ix.search(filters, config).apply(jooq))
                                        /* Result confirmed */
                                        .compose(queried -> Ix.isExist(queried) ?
                                                /* Update Flow */
                                                Ix.serializePO(queried, config)
                                                        /* Audit: Update */
                                                        .compose(item -> IxActor.update().bind(request).procAsync(item, config))
                                                        /* Final Update */
                                                        .compose(item -> Ux.future(item.mergeIn(record)))
                                                        .compose(json -> Ix.deserializeT(json, config))
                                                        .compose(jooq::updateAsync)
                                                        .compose(Ux::futureJ)
                                                :
                                                /* Insert UUID */
                                                IxActor.uuid().procAsync(record, config)
                                                        /* Audit: Create / Update */
                                                        .compose(item -> IxActor.create().bind(request).procAsync(item, config))
                                                        .compose(item -> IxActor.update().bind(request).procAsync(item, config))
                                                        /* Final Insert */
                                                        .compose(json -> Ix.deserializeT(json, config))
                                                        .compose(jooq::insertAsync)
                                                        .compose(Ux::futureJ)
                                        )
                                );
                            });
                            return Ux.thenCombine(futures);
                        });
                        result.onComplete(imported -> {
                            Ix.infoDao(LOGGER, IxMsg.FILE_LOADED, filename);
                            promise.complete(Envelop.success(Boolean.TRUE));
                        });
                    }
                } else {
                    /*
                     * The error for table size checking
                     */
                    promise.complete(Envelop.failure(new _409MultiModuleException(this.getClass(), tableMap.size())));
                }
            });
        } else {
            promise.complete(Envelop.success(Boolean.FALSE));
        }
        return promise.future();
    }

    @Address(Addr.File.EXPORT)
    public Future<Envelop> exportFile(final Envelop request) {
        /* Headers */
        final ConcurrentMap<String, String> exportedHeaders = new ConcurrentHashMap<>();
        /* Removed */
        final JsonArray removed = new JsonArray();
        /* Column sequence */
        final List<String> columnList = new ArrayList<>();
        /* Search full column and it will be used in another method */
        return Ix.create(this.getClass()).input(request).envelop((dao, config) -> Unity.fetchFull(dao, request, config)

                /* Column initialization */
                .compose(columns -> {
                    final Set<String> columnSet = new HashSet<>();
                    Ut.itJArray(columns, (column, index) -> {
                        /* Key */
                        final String columnKey = column.getString(IxPin.getColumnKey());
                        /* Name */
                        final String columnLabel = column.getString(IxPin.getColumnLabel());
                        if (Ut.notNil(columnKey) && Ut.notNil(columnLabel)) {
                            exportedHeaders.put(columnKey, columnLabel);
                            /* All columns */
                            columnSet.add(columnKey);
                        }
                    });
                    return Ux.future(columnSet);
                })

                /* Column calculation */
                .compose(columnSet -> {
                    /* Expected columns */
                    final JsonArray expected = Ux.getArray2(request);
                    columnSet.removeAll(expected.stream()
                            .filter(Objects::nonNull)
                            .map(column -> (String) column)
                            .collect(Collectors.toSet()));
                    /* Column sequence */
                    expected.stream()
                            .filter(Objects::nonNull)
                            .map(column -> (String) column)
                            .forEach(columnList::add);
                    /* projection calculation */
                    return Ux.future(Ut.toJArray(columnSet));
                })

                /* Projection calculation */
                .compose(projection -> {
                    {
                        /* Removed will be used in future */
                        removed.addAll(projection);
                    }
                    /* Parameters Extraction */
                    final JsonObject body = new JsonObject();
                    final JsonObject criteria = Ux.getJson1(request);
                    body.put(Qr.KEY_CRITERIA, criteria);
                    body.put(Qr.KEY_PROJECTION, projection);
                    /* Calculation for projection here */
                    return Ux.future(body);
                })

                /* Verify */
                .compose(input -> IxActor.verify().bind(request).procAsync(input, config))

                /* Execution */
                .compose(params -> Ix.query(params, config).apply(dao))

                /* Dict */
                .compose(response -> {
                    /* Data for ExTable */
                    JsonArray data = Ux.pageData(response);
                    /*
                     * To avoid final in lambda expression
                     */
                    final JsonArray inputData = data.copy();
                    return Unity.fetchDict(request, config).compose(Ut.ifNil(
                            () -> inputData,
                            dictMap -> Unity.fetchFabric(dictMap, config).inTo(inputData))
                    );
                })
                /* Data Exporting */
                .compose(data -> {
                    /* Left columns, removed useless column */
                    removed.stream().map(item -> (String) item).forEach(exportedHeaders::remove);

                    /* Combine and build data of excel */
                    return Ke.combineAsync(data, exportedHeaders, columnList);
                })

                /* Final exporting her for excel download */
                .compose(data -> {
                    final String actor = Ux.getString(request);
                    return this.client.exportAsync(actor, data);
                })
                .compose(buffer -> Ux.future(Envelop.success(buffer)))
        );
    }

}
