package io.vertx.tp.modular.jooq;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.atom.query.engine.QrItem;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.experiment.mixture.HReference;
import io.vertx.up.uca.cache.Cc;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class JQPre {
    /**
     * Process for `REFERENCE` condition based on DataAtom
     *
     * @param atom     {@link DataAtom} The model definition.
     * @param criteria {@link Criteria} the criteria object to stored query condition.
     *
     * @return The new modified criteria
     */
    @SuppressWarnings("all")
    static Criteria prepare(final DataAtom atom, final Criteria criteria) {
        /*
         * Reference collection
         * 1. Attribute must be Elementary
         * 2. Execute query processing
         *
         * Example
         *
         * supportGroup = xxx
         * supportSection = xxx
         *
         * 1. AND
         * 2. OR
         *
         * Hash code identify the qr item is in the same level
         */
        final Cc<Integer, ConcurrentMap<String, Set<QrItem>>> ccRemoved = Cc.open();
        final Cc<Integer, ConcurrentMap<String, QrItem>> ccReplaced = Cc.open();
        final ConcurrentMap<Integer, JsonObject> referenceData = new ConcurrentHashMap<>();
        final HReference reference = atom.reference();
        reference.refQr().forEach((field, query) -> criteria.match(field, (qr, json) -> {
            /*
             * There exist Elementary field based on RQuote, and the condition contains
             * value here.
             * The enhancement version should contain `op` input from criteria condition
             *
             * The old version contains `=` and `i` only, the latest version:
             *
             * 1) The default operator is `=`.
             * 2) The operator came from `qr.op()` result here.
             */
            final JsonArray data = query.fetchBy(qr.op(), qr.value());
            /*
             * Calculate query map
             */
            final ConcurrentMap<String, JsonArray> item = query.fetchQuery(data);
            if (!item.isEmpty()) {
                if (Values.ONE == item.size()) {
                    final Boolean isAnd = json.getBoolean(Strings.EMPTY, Boolean.FALSE);
                    final String fieldReplaced = item.keySet().iterator().next();
                    /*
                     * Get the same level processing
                     */
                    final ConcurrentMap<String, QrItem> calculated = ccReplaced.pick(ConcurrentHashMap::new, json.hashCode());
                    // Fn.po?l(replaced, json.hashCode(), ConcurrentHashMap::new);
                    final QrItem itemReplaced = Cc.pool(calculated, fieldReplaced, () -> new QrItem(fieldReplaced + ",i"));
                    if (Objects.isNull(itemReplaced.value())) {
                        // The first time
                        itemReplaced.value(item.get(fieldReplaced));
                    } else {
                        // The second time
                        itemReplaced.add(item.get(fieldReplaced), isAnd);
                    }
                    calculated.put(fieldReplaced, itemReplaced);
                    /*
                     * Removed processing
                     */
                    final ConcurrentMap<String, Set<QrItem>> removedMap = ccRemoved.pick(ConcurrentHashMap::new, json.hashCode());
                    // Fn.po?l(removed, json.hashCode(), ConcurrentHashMap::new);
                    final Set<QrItem> removedSet = Cc.pool(removedMap, fieldReplaced, HashSet::new);
                    removedSet.add(qr);
                    referenceData.put(json.hashCode(), json);
                } else {
                    // TODO:
                    //     Multi Join Happen, Current Version Does not contain this situation
                    //     Develop in future cross multi fields to do join instead of single field join
                }
            }
        }));
        /*
         * Replace qrItem
         */
        ccRemoved.store().forEach((levelKey, removedMap) -> {
            final ConcurrentMap<String, QrItem> replacedMap = ccReplaced.store(levelKey);
            final JsonObject jsonRef = referenceData.get(levelKey);
            if (Objects.nonNull(replacedMap) && Objects.nonNull(removedMap)) {
                /*
                 * Remove with multi fields.
                 */
                removedMap.forEach((field, removedSingle) -> {
                    final QrItem replacedQr = replacedMap.get(field);
                    /*
                     *  1. removed from jsonRef of all removedSingle.
                     *  2. add new to jsonRef with replacedQr.
                     */
                    removedSingle.forEach(removedItem -> {
                        jsonRef.remove(removedItem.qrKey());
                        if (Qr.Op.EQ.equals(removedItem.op())) {
                            jsonRef.remove(removedItem.field());
                        }
                    });
                    jsonRef.put(replacedQr.qrKey(), replacedQr.value());
                });
            }
        });
        return criteria;
    }
}
