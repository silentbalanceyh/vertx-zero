package io.vertx.up.commune.config;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * ## DictFabric
 *
 * Combiner of Dict
 * 1. `ConcurrentMap<String, Epsilon>` map stored field -> dict
 * 2. `ConcurrentMap<String, JsonArray>` dictionary data in current channel
 * 3. DualMapping Stored mapping ( from -> to )
 * 4. The `fabric` do not support mapping converting future( Important )
 */
public class DictFabric {

    private static final Annal LOGGER = Annal.get(DictFabric.class);
    /*
     * From field = DictEpsilon
     */
    private final transient ConcurrentMap<String, DictEpsilon> epsilonMap
            = new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, JsonArray> dictData
            = new ConcurrentHashMap<>();
    private final transient DualItem mapping;
    /*
     * Data here for dictionary
     */
    private final transient ConcurrentMap<String, DualItem> fromData
            = new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, DualItem> toData
            = new ConcurrentHashMap<>();

    private DictFabric(final DualItem mapping) {
        this.mapping = mapping;
    }

    public static DictFabric create(final DualItem mapping) {
        return new DictFabric(mapping);
    }

    public static DictFabric create() {
        return new DictFabric(null);
    }

    public DictFabric createCopy() {
        return this.createCopy(null);
    }

    public DictFabric createCopy(final DualItem mapping) {
        /*
         * Here are two mapping for copy
         * 1. When `mapping` is null, check whether there exist mapping
         * 2. When `mapping` is not null, the mapping will be overwrite directly
         *
         * Fix issue of : java.lang.NullPointerException
         * when you call `createCopy()` directly.
         */
        final DualItem calculated = Objects.isNull(mapping) ? this.mapping : mapping;
        final DictFabric created = create(calculated);
        created.dictionary(this.dictData);
        created.epsilon(this.epsilonMap);
        return created;
    }

    public DictFabric epsilon(final ConcurrentMap<String, DictEpsilon> epsilonMap) {
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

    public DictFabric dictionary(final ConcurrentMap<String, JsonArray> dictData) {
        if (Objects.nonNull(dictData) && !dictData.isEmpty()) {
            this.dictData.clear();                          /* Clear Queue */
            this.dictData.putAll(dictData);
        } else {
            LOGGER.debug("DictFabric got empty dictData ( ConcurrentMap<String, JsonArray> ) !");
        }
        this.init();
        return this;
    }

    // -------- Get data
    public DualItem mapping() {
        return this.mapping;
    }

    public ConcurrentMap<String, DictEpsilon> epsilon() {
        return this.epsilonMap;
    }

    public ConcurrentMap<String, JsonArray> dictionary() {
        return this.dictData;
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
                JsonArray dataArray = this.dictData.get(epsilon.getSource());
                if (Objects.isNull(dataArray)) {
                    dataArray = new JsonArray();
                }
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
                    final DualItem item = new DualItem(dataItem);
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
        return !this.epsilonMap.isEmpty() && !this.dictData.isEmpty();
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
        return this.process(this.fromData, input, DualItem::from);
    }

    public JsonArray inToS(final JsonArray input) {
        return this.process(input, this::inToS);
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
        return this.process(this.fromData, input, DualItem::to);
    }

    public JsonArray inFromS(final JsonArray input) {
        return this.process(input, this::inFromS);
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
        return this.process(this.toData, output, DualItem::from);
    }

    public JsonArray outToS(final JsonArray output) {
        return this.process(output, this::outToS);
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
        return this.process(this.toData, output, DualItem::to);
    }

    public JsonArray outFromS(final JsonArray output) {
        return this.process(output, this::outFromS);
    }

    public Future<JsonObject> outFrom(final JsonObject input) {
        return Future.succeededFuture(this.outFromS(input));
    }

    public Future<JsonArray> outFrom(final JsonArray input) {
        return Future.succeededFuture(this.outFromS(input));
    }

    private JsonArray process(final JsonArray process,
                              final Function<JsonObject, JsonObject> function) {
        final JsonArray normalized = new JsonArray();
        Ut.itJArray(process).map(function).forEach(normalized::add);
        return normalized;
    }

    private JsonObject process(final ConcurrentMap<String, DualItem> dataMap,
                               final JsonObject input,
                               final BiFunction<DualItem, String, String> applier) {
        final JsonObject normalized = Objects.isNull(input) ? new JsonObject() : input.copy();
        dataMap.forEach((field, item) -> {
            final Object fromValue = input.getValue(field);
            if (Objects.nonNull(fromValue) && fromValue instanceof String) {
                final String toValue = applier.apply(item, fromValue.toString());
                normalized.put(field, toValue);
            }
        });
        return normalized;
    }

    public String report() {
        final StringBuilder builder = new StringBuilder();
        builder.append("\n\t[ Epsilon ]: ");
        this.epsilonMap.forEach((key, epsilon) -> builder.append("\n\t\t").
                append(key).append(" = ").append(epsilon));
        builder.append("\n\t[ Dict Data ]: ");
        this.dictData.forEach((key, dictData) -> builder.append("\n\t\t").
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
