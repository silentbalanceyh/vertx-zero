package io.vertx.tp.plugin.excel;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._500ExportingErrorException;
import io.vertx.tp.plugin.excel.atom.ExRecord;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.jq.UxJooq;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
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

    <T> T saveEntity(final JsonObject data, final ExTable table) {
        T reference = null;
        if (Objects.nonNull(table.getPojo()) && Objects.nonNull(table.getDao())) {
            /*
             * First, find the record by unique filters that defined in business here.
             */
            final JsonObject filters = table.whereUnique(data);
            LOGGER.debug("[ Έξοδος ] Filters: {0}, Table: {1}", filters.encode(), table.getName());
            final T entity = Ux.fromJson(data, table.getPojo(), table.getPojoFile());
            final UxJooq jooq = Ux.Jooq.on(table.getDao());
            if (null != jooq) {
                final String pojoFile = table.getPojoFile();
                if (Ut.notNil(pojoFile)) {
                    jooq.on(pojoFile);
                }
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
                        final T fetched = jooq.findById(key);
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
        }
        return reference;
    }

    <T> Future<Set<T>> importAsync(final AsyncResult<Set<ExTable>> async) {
        if (async.succeeded()) {
            final Set<ExTable> tables = async.result();
            /*
             * Loading data into system
             */
            final Set<T> entitySet = new HashSet<>();
            tables.forEach(table -> this.extract(table).forEach(json -> {
                /* Filters building */
                final T entity = this.saveEntity(json, table);
                if (Objects.nonNull(entity)) {
                    entitySet.add(entity);
                }
            }));
            /* Set<T> handler */
            return Future.succeededFuture(entitySet);
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

    private List<JsonObject> extract(final ExTable table) {
        /* Records extracting */
        final List<ExRecord> records = table.get();
        LOGGER.info("[ Έξοδος ] Table: {0}, Data Size: {1}", table.getName(), records.size());
        /* Pojo Processing */
        return records.stream().filter(Objects::nonNull)
                .map(ExRecord::toJson)
                .collect(Collectors.toList());
    }
}
