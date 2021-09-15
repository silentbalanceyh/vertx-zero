package io.vertx.up.commune.exchange;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## DictFabric
 *
 * Combiner of Dict
 * 1. `ConcurrentMap<String, Epsilon>` map stored field -> dict
 * 2. `ConcurrentMap<String, JsonArray>` dictionary data in current channel
 * 3. DualMapping Stored mapping ( from -> to )
 * 4. The `fabric` do not support mapping converting future( Important )
 */
public class DiFabric {

    private static final Annal LOGGER = Annal.get(DiFabric.class);
    /*
     * From field = DictEpsilon
     * This map stored consume information of current usage, the format is as following
     * {
     *     "field": {
     *         "source": "...",
     *         "in": "...",
     *         "out": "..."
     *     }
     * }
     * -- field: The field that defined in final input record
     * -- source: The dict name that has been mapped to `dictData` variable here
     * -- in/out: The translation direction that defined.
     */
    private final transient ConcurrentMap<String, DiConsumer> epsilonMap
        = new ConcurrentHashMap<>();
    /*
     * Each fabric bind
     */
    private final transient DiStore store = new DiStore();
    /*
     *  The mapping in dictionary
     */
    private final transient BiItem mapping;

    /*
     * Data here for dictionary
     */
    private final transient ConcurrentMap<String, BiItem> fromData
        = new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, BiItem> toData
        = new ConcurrentHashMap<>();

    private DiFabric(final BiItem mapping) {
        this.mapping = mapping;
    }

    /*
     * Here are the creation method for `DictFabric`
     * Each api will create new `DictFabric` object
     */
    public static DiFabric create(final BiItem mapping) {
        return new DiFabric(mapping);
    }

    public static DiFabric create() {
        return new DiFabric(null);
    }

    public DiFabric createCopy() {
        return this.createCopy(null);
    }

    public DiFabric createCopy(final BiItem mapping) {
        /*
         * Here are two mapping for copy
         * 1. When `mapping` is null, check whether there exist mapping
         * 2. When `mapping` is not null, the mapping will be overwrite directly
         *
         * Fix issue of : java.lang.NullPointerException
         * when you call `createCopy()` directly.
         */
        final BiItem calculated = Objects.isNull(mapping) ? this.mapping : mapping;
        final DiFabric created = create(calculated);
        created.dictionary(this.store.data());
        created.epsilon(this.epsilonMap);
        return created;
    }

    @Fluent
    public DiFabric epsilon(final ConcurrentMap<String, DiConsumer> epsilonMap) {
        if (Objects.nonNull(epsilonMap) && !epsilonMap.isEmpty()) {
            /*
             * Re-bind
             */
            this.epsilonMap.clear();                        /* Clear Queue */
            epsilonMap.forEach((key, epsilon) -> {
                /*
                 * Only pick up valid configured `epsilon`
                 * Other invalid will be ignored.
                 */
                if (epsilon.isValid()) {
                    this.epsilonMap.put(key, epsilon);
                }
            });
        } else {
            LOGGER.debug("DictFabric got empty epsilonMap ( ConcurrentMap<String, DictEpsilon> ) !");
        }
        this.init();
        return this;
    }

    @Fluent
    public DiFabric dictionary(final ConcurrentMap<String, JsonArray> dictData) {
        // Call store for data replaced
        this.store.data(dictData);
        this.init();
        return this;
    }

    /*
     * The stored data that related to configuration defined here
     */
    public BiItem mapping() {
        return this.mapping;
    }

    public ConcurrentMap<String, DiConsumer> epsilon() {
        return this.epsilonMap;
    }

    public ConcurrentMap<String, JsonArray> dictionary() {
        return this.store.data();
    }

    public JsonArray dictionary(final String dictName) {
        return this.store.item(dictName);
    }

    private void init() {
        if (this.ready()) {
            /*
             * Iterate the epsilonMap
             */
            this.epsilonMap.forEach((fromField, epsilon) -> {
                /*
                 * Get JsonArray from dict
                 */
                final JsonArray dataArray = this.store.item(epsilon.getSource());
                /*
                 * Data Source is dataArray
                 * Build current `DualItem`
                 */
                final JsonObject dataItem = new JsonObject();
                Ut.itJArray(dataArray).forEach(item -> {
                    /*
                     * Data in ( From ) - out ( To )
                     */
                    final String inValue = item.getString(epsilon.getIn());
                    final String outValue = item.getString(epsilon.getOut());
                    if (Ut.notNil(inValue) && Ut.notNil(outValue)) {
                        dataItem.put(inValue, outValue);
                    }
                });
                /*
                 * Fill data in our data structure
                 */
                if (Ut.notNil(dataItem)) {

                    /*
                     * From Data Map processing
                     */
                    final BiItem item = new BiItem(dataItem);
                    this.fromData.put(fromField, item);

                    /*
                     * To Data Map processing
                     */
                    if (Objects.nonNull(this.mapping)) {
                        final String hitField = this.mapping.to(fromField);
                        if (Ut.notNil(hitField)) {
                            this.toData.put(hitField, item);
                        }
                    }
                }
            });
        }
    }

    private boolean ready() {
        return !this.epsilonMap.isEmpty() && this.store.ready();
    }

    /*
     * DualItem ->
     *     in    ->   out
     *  ( name ) -> ( key )
     *
     * Api: to ( in -> out )
     * Api: from ( out -> in )
     */

    /*
     * inTo
     * 1) The field is Ox field
     * 2) uuid -> ( out -> in )
     * 3) The output structure are Ox field
     */
    public JsonObject inToS(final JsonObject input) {
        return DictTool.process(this.fromData, input, BiItem::from);
    }

    public JsonArray inToS(final JsonArray input) {
        return DictTool.process(input, this::inToS);
    }

    public Future<JsonObject> inTo(final JsonObject input) {
        return Future.succeededFuture(this.inToS(input));
    }

    public Future<JsonArray> inTo(final JsonArray input) {
        return Future.succeededFuture(this.inToS(input));
    }

    /*
     * inFrom
     * 1) The field is Ox field
     * 2) display -> ( in -> out )
     * 3) The output structure are Ox field
     */
    public JsonObject inFromS(final JsonObject input) {
        return DictTool.process(this.fromData, input, BiItem::to);
    }

    public JsonArray inFromS(final JsonArray input) {
        return DictTool.process(input, this::inFromS);
    }

    public Future<JsonObject> inFrom(final JsonObject input) {
        return Future.succeededFuture(this.inFromS(input));
    }

    public Future<JsonArray> inFrom(final JsonArray input) {
        return Future.succeededFuture(this.inFromS(input));
    }

    /*
     * outTo
     * 1) The field is Tp field
     * 2) uuid -> ( out -> in )
     * 3) The output structure are Tp field
     */
    public JsonObject outToS(final JsonObject output) {
        return DictTool.process(this.toData, output, BiItem::from);
    }

    public JsonArray outToS(final JsonArray output) {
        return DictTool.process(output, this::outToS);
    }

    public Future<JsonObject> outTo(final JsonObject input) {
        return Future.succeededFuture(this.outToS(input));
    }

    public Future<JsonArray> outTo(final JsonArray input) {
        return Future.succeededFuture(this.outToS(input));
    }

    /*
     * outFrom
     * 1) The field is Tp field
     * 2) display -> ( in -> out )
     * 3) The output structure are Tp field
     */
    public JsonObject outFromS(final JsonObject output) {
        return DictTool.process(this.toData, output, BiItem::to);
    }

    public JsonArray outFromS(final JsonArray output) {
        return DictTool.process(output, this::outFromS);
    }

    public Future<JsonObject> outFrom(final JsonObject input) {
        return Future.succeededFuture(this.outFromS(input));
    }

    public Future<JsonArray> outFrom(final JsonArray input) {
        return Future.succeededFuture(this.outFromS(input));
    }

    // ----------------------- Operation Method -----------------------
    /*
     * Update single dictionary by `dictName` and `keyField`
     * - dictName: the dictionary name that stored in `dictData`
     *
     * this.init() is required when `dictData` have been changed here
     */
    public void itemUpdate(final String dictName, final JsonObject input) {
        this.itemUpdate(dictName, input, "key");
    }

    public void itemUpdate(final String dictName, final JsonArray input) {
        this.itemUpdate(dictName, input, "key");
    }

    public void itemUpdate(final String dictName, final JsonArray input, final String keyField) {
        this.store.itemUpdate(dictName, input, keyField);
        this.init();
    }

    public void itemUpdate(final String dictName, final JsonObject input, final String keyField) {
        this.store.itemUpdate(dictName, input, keyField);
        this.init();
    }

    /*
     * Check whether there existing the `keyField` = value
     * record in fixed dictName of our DictFabric
     */
    public boolean itemExist(final String dictName, final String value) {
        return this.store.itemExist(dictName, value, "key");
    }

    public boolean itemExist(final String dictName, final String value, final String keyField) {
        return this.store.itemExist(dictName, value, keyField);
    }

    public JsonObject itemFind(final String dictName, final String value) {
        return this.store.itemFind(dictName, value, "key");
    }

    public JsonObject itemFind(final String dictName, final String value, final String keyField) {
        return this.store.itemFind(dictName, value, keyField);
    }

    /*
     * Debug method for output information of current dict only
     */
    public String report() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\n\t[ Epsilon ]: ");
        this.epsilonMap.forEach((key, epsilon) -> builder.append("\n\t\t").
            append(key).append(" = ").append(epsilon));
        builder.append("\n\t[ Dict Data ]: ");
        this.store.data().forEach((key, dictData) -> builder.append("\n\t\t").
            append(key).append(" = ").append(dictData.encode()));
        if (Objects.nonNull(this.mapping)) {
            builder.append("\n\t[ Mapping ]: ").append(this.mapping.toString());
        }
        builder.append("\n\t[ From Data ]: ");
        this.fromData.forEach((field, json) -> builder.append("\n\t\t")
            .append(field).append(" = ").append(json.toString()));
        builder.append("\n\t[ To Data ]: ");
        this.toData.forEach((field, json) -> builder.append("\n\t\t")
            .append(field).append(" = ").append(json.toString()));
        return builder.toString();
    }
}
