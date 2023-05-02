package io.vertx.up.uca.jooq;

import io.horizon.eon.em.typed.ChangeFlag;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiPredicate;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ActionUpsert extends AbstractAction {
    /*
     * Fetch and Update
     */
    private final transient ActionFetch fetch;
    private final transient ActionUpdate update;
    private final transient ActionInsert insert;

    ActionUpsert(final JqAnalyzer analyzer) {
        super(analyzer);
        /*
         * Operation Combine
         */
        this.fetch = new ActionFetch(analyzer);
        this.update = new ActionUpdate(analyzer);
        this.insert = new ActionInsert(analyzer);

    }

    /* Future<T> */
    <T, ID> Future<T> upsertAsync(final ID id, final T updated) {
        return this.execAsync(this.fetch.fetchByIdAsync(id), updated);
    }

    <T> Future<T> upsertAsync(final JsonObject criteria, final T updated) {
        return this.execAsync(this.fetch.fetchOneAsync(criteria), updated);
    }

    /* T */
    <T, ID> T upsert(final ID id, final T updated) {
        return this.exec(this.fetch.fetchById(id), updated);
    }

    <T> T upsert(final JsonObject criteria, final T updated) {
        return this.exec(this.fetch.fetchOne(criteria), updated);
    }

    <T> List<T> upsert(final JsonObject criteria, final List<T> updated, final BiPredicate<T, T> finder) {
        /*
         * Find the original T list
         */
        final List<T> original = this.fetch.fetch(criteria);
        /*
         * Query data by filters
         */
        final ConcurrentMap<ChangeFlag, List<T>> compared = this.compared(original, updated, finder);
        /*
         * Insert & Update
         */
        final List<T> resultList = new ArrayList<>();
        resultList.addAll(this.insert.insert(compared.get(ChangeFlag.ADD)));
        resultList.addAll(this.update.update(compared.get(ChangeFlag.UPDATE)));
        return resultList;
    }

    <T> Future<List<T>> upsertAsync(final JsonObject criteria, final List<T> updated, final BiPredicate<T, T> finder) {
        /*
         * Query data by filters ( This filters should fetch all condition list as List<T> )
         * original
         */
        return this.fetch.<T>fetchAsync(criteria).compose(original -> {
            /*
             * Compared original / and last list
             */
            final ConcurrentMap<ChangeFlag, List<T>> compared = this.compared(original, updated, finder);
            /*
             * Insert & Update
             */
            final List<Future<List<T>>> futures = new ArrayList<>();
            futures.add(this.insert.insertAsync(compared.get(ChangeFlag.ADD)));
            futures.add(this.update.updateAsync(compared.get(ChangeFlag.UPDATE)));
            return Fn.compressL(futures);
        });
    }

    /*
     *  Function compare for batch processing
     */
    <T> ConcurrentMap<ChangeFlag, List<T>> compared(final List<T> original, final List<T> updated, final BiPredicate<T, T> finder) {
        /*
         * Combine original / updated
         * list to get result for
         * INSERT / UPDATE
         */
        final List<T> addQueue = new ArrayList<>();
        final List<T> updateQueue = new ArrayList<>();
        /*
         * Only get `ADD` & `UPDATE`
         * Iterate original list
         * 1) If the entity is missing in original, ADD
         * 2) If the entity is existing in original, UPDATE
         */
        updated.forEach(newRecord -> {
            /*
             * New record found in `original`
             */
            final T found = Ut.elementFind(original, oldRecord -> finder.test(oldRecord, newRecord));
            if (Objects.isNull(found)) {
                addQueue.add(newRecord);
            } else {
                final T combine = this.analyzer.copyEntity(found, newRecord);
                updateQueue.add(combine);
            }
        });
        return new ConcurrentHashMap<ChangeFlag, List<T>>() {
            {
                this.put(ChangeFlag.ADD, addQueue);
                this.put(ChangeFlag.UPDATE, updateQueue);
            }
        };
    }

    /*
     * Combine for `Saving` here, it could help Jooq to execute
     * Saving operation.
     *
     * Insert / Update merged
     * 1 Existing: do updating
     * 2 Missing: do missing
     */
    private <T> Future<T> execAsync(final Future<T> queried, final T updated) {
        return queried.compose(previous -> {
            if (Objects.isNull(previous)) {
                return this.insert.insertAsync(updated);
            } else {
                final T combine = this.analyzer.copyEntity(previous, updated);
                return this.update.updateAsync(combine);
            }
        });
    }

    private <T> T exec(final T queried, final T updated) {
        if (Objects.isNull(queried)) {
            return this.insert.insert(updated);
        } else {
            final T combine = this.analyzer.copyEntity(queried, updated);
            return this.update.update(combine);
        }
    }
}
