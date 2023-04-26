package io.horizon.spi.phantom;

import io.horizon.spi.business.ExIo;
import io.horizon.spi.feature.Arbor;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractArbor implements Arbor {
    /*
     * Here the input object is as following
     * category:
     * {
     *      "key": "primary key",
     *      "parentId": "parent node key here"
     * }
     */
    protected Future<JsonArray> combineArbor(final JsonObject category, final JsonArray children, final JsonObject configuration) {
        /*
         * Extract data to calculate the default `store`
         * The input JsonArray should be as following
         * - arg0, JsonArray of data
         * - arg1, JsonObject of configuration
         */
        final JsonObject store = configuration.getJsonObject(KName.STORE);
        final JsonArray normalized = new JsonArray();
        final ConcurrentMap<String, JsonObject> childMap = Ut.elementMap(children, KName.KEY);
        normalized.add(this.storePathOn(category, null, childMap, store));
        Ut.itJArray(children).map(json -> this.storePathOn(json, category, childMap, store)).forEach(normalized::add);

        return Ux.channel(ExIo.class, () -> children, io -> io.docInitialize(normalized, store)).compose(processed -> {
            /*
             * Filtered by `category` storePath field to exclude the invalid children
             */
            // final String storePath = category.getString(KName.STORE_PATH);
            final JsonArray valid = new JsonArray();
            /*
             * Store Map For normalized for children sort
             * The children node should be sort field `sort`
             */
            final ConcurrentMap<String, Integer> sortMap = Ut.elementMap(normalized, KName.STORE_PATH, KName.SORT);
            Ut.itJArray(processed).forEach(json -> {
                // Copy JsonObject
                json = json.copy();
                final String storePath = json.getString(KName.STORE_PATH);
                if (sortMap.containsKey(storePath)) {
                    /* Add `sort` to root node for root sorting */
                    json.put(KName.SORT, sortMap.get(storePath));
                }
                //                if (storePath.equals(json.getString(KName.STORE_PATH))) {
                //                    json.put(KName.SORT, category.getInteger(KName.SORT));
                //                }
                valid.add(json);
            });
            return Ux.future(valid);
        });
    }

    protected Future<JsonArray> combineArbor(final JsonObject category, final JsonObject configuration) {
        return this.combineArbor(category, null, configuration);
    }


    // ----------------------- Private Method for store path analyzing / building
    /*
     * Complex Method for `storePath` and `storeParent` processing.
     */
    private JsonObject storePathOn(final JsonObject record,
                                   // Root
                                   final JsonObject root,
                                   // Map
                                   final ConcurrentMap<String, JsonObject> parentMap,
                                   final JsonObject store) {
        final JsonObject storeInfo = store.copy();
        record.mergeIn(storeInfo);
        /*
         * Re-Calculate `category`
         */
        final String storePath;
        if (Objects.isNull(root)) {
            record.put(KName.CATEGORY, record.getValue(KName.CODE));
            storePath = store.getString(KName.STORE_PATH);
        } else {
            record.put(KName.CATEGORY, root.getValue(KName.CODE));
            storePath = Ut.ioPath(store.getString(KName.STORE_PATH), root.getString(KName.NAME));
        }
        /*
         * storePath
         * storeParent
         */
        final List<String> parentList = this.storePath(record, parentMap);
        final String storeParent;
        if (parentList.isEmpty()) {
            storeParent = storePath;
        } else {
            storeParent = Ut.ioPath(storePath, Ut.fromJoin(parentList, "/"));
        }
        final String name = record.getString(KName.NAME);
        record.put(KName.STORE_PATH, Ut.ioPath(storeParent, name));
        record.put(KName.STORE_PARENT, storeParent);
        return record;
    }

    private List<String> storePath(final JsonObject record, final ConcurrentMap<String, JsonObject> parentMap) {
        final String parentId = record.getString(KName.PARENT_ID);
        if (Ut.isNil(parentId)) {
            return new ArrayList<>();
        }
        final JsonObject parent = parentMap.get(parentId);
        if (Ut.isNil(parent)) {
            return new ArrayList<>();
        }
        // Loop
        final List<String> parentList = new ArrayList<>(this.storePath(parent, parentMap));
        parentList.add(parent.getString(KName.NAME));
        return parentList;
    }
}
