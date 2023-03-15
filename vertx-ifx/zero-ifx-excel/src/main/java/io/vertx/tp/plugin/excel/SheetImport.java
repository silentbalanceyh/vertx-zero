package io.vertx.tp.plugin.excel;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._500ExportingErrorException;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class SheetImport {
    private static final Annal LOGGER = Annal.get(ExcelClientImpl.class);
    private transient final ExcelHelper helper;

    private SheetImport(final ExcelHelper helper) {
        this.helper = helper;
    }

    static SheetImport create(final ExcelHelper helper) {
        return new SheetImport(helper);
    }

    <T> Set<T> saveEntity(final JsonArray data, final ExTable table) {
        final Set<T> resultSet = new HashSet<>();
        if (Objects.nonNull(table.classPojo()) && Objects.nonNull(table.classDao())) {
            try {
                final JsonObject filters = table.whereAncient(data);
                LOGGER.debug("[ Έξοδος ]  Table: {1}, Filters: {0}", filters.encode(), table.getName());
                final List<T> entities = Ux.fromJson(data, table.classPojo(), table.filePojo());
                final UxJooq jooq = this.jooq(table);
                assert null != jooq;
                final List<T> queried = jooq.fetch(filters);
                /*
                 * Compare by unique
                 */
                ConcurrentMap<ChangeFlag, List<T>> compared =
                    Ux.compare(queried, entities, table.ukIn(), table.filePojo());
                final List<T> qUpdate = compared.getOrDefault(ChangeFlag.UPDATE, new ArrayList<>());
                final List<T> qInsert = compared.getOrDefault(ChangeFlag.ADD, new ArrayList<>());
                if (!qInsert.isEmpty()) {
                    /*
                     * Compare by keys
                     */
                    final String entityKey = table.pkIn();
                    if (Objects.nonNull(entityKey)) {
                        final Set<String> keys = new HashSet<>();
                        qInsert.forEach(item -> {
                            final Object value = Ut.field(item, entityKey);
                            if (Objects.nonNull(value)) {
                                keys.add(value.toString());
                            }
                        });
                        final List<T> qKeys = jooq.fetchIn(entityKey, keys);
                        if (!qKeys.isEmpty()) {
                            compared = Ux.compare(qKeys, qInsert, table.ukIn(), table.filePojo());
                            qUpdate.addAll(compared.getOrDefault(ChangeFlag.UPDATE, new ArrayList<>()));
                            // qInsert reset
                            qInsert.clear();
                            qInsert.addAll(compared.getOrDefault(ChangeFlag.ADD, new ArrayList<>()));
                        }
                    }
                }
                /*
                 * Batch operation
                 */
                final List<T> batchInsert = jooq.insert(this.helper.compress(qInsert, table));
                resultSet.addAll(batchInsert);
                final List<T> batchUpdate = jooq.update(qUpdate);
                resultSet.addAll(batchUpdate);
                final int total = batchUpdate.size() + batchInsert.size();
                LOGGER.info("[ Έξοδος ] `{0}` -- ( {1} ), Inserted: {2}, Updated: {3}",
                    table.getName(), String.valueOf(total), String.valueOf(batchInsert.size()), String.valueOf(batchUpdate.size()));
            } catch (final Throwable ex) {
                ex.printStackTrace();
                LOGGER.jvm(ex);
            }
        }
        return resultSet;
    }

    <T> T saveEntity(final JsonObject data, final ExTable table) {
        T reference = null;
        if (Objects.nonNull(table.classPojo()) && Objects.nonNull(table.classDao())) {
            /*
             * First, find the record by unique filters that defined in business here.
             */
            final JsonObject filters = table.whereUnique(data);
            LOGGER.debug("[ Έξοδος ]  Table: {1}, Filters: {0}", filters.encode(), table.getName());
            final T entity = Ux.fromJson(data, table.classPojo(), table.filePojo());
            final UxJooq jooq = this.jooq(table);
            assert null != jooq;
            /*
             * Unique filter to fetch single record database here.
             * Such as code + sigma
             */
            final T queried = jooq.fetchOne(filters);
            if (null == queried) {
                /*
                 * Here are two situations that we could be careful
                 * 1. Unique Condition in source does not change, do insert here.
                 * 2. Key Condition existing in database, do update here.
                 */
                final String key = table.whereKey(data);
                if (Ut.isNil(key)) {
                    /*
                     * No definition of key here, insert directly.
                     */
                    reference = jooq.insert(entity);
                } else {
                    /*
                     * Double check to avoid issue:
                     * java.sql.SQLIntegrityConstraintViolationException: Duplicate entry 'xxx' for key 'PRIMARY'
                     */
                    final T fetched = jooq.fetchById(key);
                    if (null == fetched) {
                        /*
                         * In this situation, it common workflow to do data loading.
                         */
                        reference = jooq.insert(entity);
                    } else {
                        /*
                         * In this situation, it means the old unique filters have been changed.
                         * Such as:
                         * From
                         * id,      code,      sigma
                         * 001,     AB.CODE,   5sLyA90qSo7
                         *
                         * To
                         * id,      code,      sigma
                         * 001,     AB.CODE1,  5sLyA90qSo7
                         *
                         * Above example could show that primary key has not been modified
                         */
                        reference = jooq.update(entity);
                    }
                }
            } else {
                /*
                 * code, sigma did not change and we could identify this record
                 * do update directly to modify old information.
                 */
                reference = jooq.update(entity);
            }
        }
        return reference;
    }

    <T> Future<Set<T>> importAsync(final Set<ExTable> tables) {
        /*
         * Loading data into system
         */
        final List<Future<Set<T>>> futures = new ArrayList<>();
        tables.forEach(table ->
            futures.add(this.helper.extract(table)
                .compose(data -> Ux.future(this.saveEntity(data, table)))
            ));
        /* Set<T> handler */
        return Fn.combineT(futures).compose(result -> {
            final Set<T> entitySet = new HashSet<>();
            result.forEach(entitySet::addAll);
            return Ux.future(entitySet);
        });
    }

    <T> Future<Set<T>> importAsync(final AsyncResult<Set<ExTable>> async) {
        if (async.succeeded()) {
            final Set<ExTable> tables = async.result();
            return this.importAsync(tables);
        } else {
            final Throwable error = async.cause();
            if (Objects.nonNull(error)) {
                return Future.failedFuture(new _500ExportingErrorException(this.getClass(), error.getMessage()));
            } else {
                return Future.failedFuture(new _500InternalServerException(this.getClass(),
                    "Unexpected Error when Importing"));
            }
        }
    }

    private UxJooq jooq(final ExTable table) {
        final UxJooq jooq = Ux.Jooq.on(table.classDao());
        if (null != jooq) {
            final String pojoFile = table.filePojo();
            if (Ut.notNil(pojoFile)) {
                jooq.on(pojoFile);
            }
        }
        return jooq;
    }
}
