package io.vertx.up.commune.record;

import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.fn.Actuator;
import io.horizon.uca.log.Annal;
import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.internal.AptParameterException;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

/**
 * ## Compared Apt Tool
 *
 * ### 1. Intro
 *
 * This tool is for calculation of two data source ( original & current ).
 *
 * ### 2. Critical Api
 *
 * 1. original: The old data
 * 2. current: The new data
 *
 * ### 3. Upgrade Operation
 *
 * 1. Connect to HAtom Part here
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("unchecked")
public class Apt {
    private final static Annal LOGGER = Annal.get(Apt.class);

    private final static String MSG_APT_BATCH = "Current api does not support `isBatch = false`. Method = {0}";

    private final transient boolean isBatch;
    private transient final ConcurrentMap<ChangeFlag, JsonArray> compared = new ConcurrentHashMap<>();
    private transient AptOp<JsonObject> single;
    private transient AptOp<JsonArray> batch;

    // -------------  Private Construct Function --------------
    private Apt(final JsonObject original, final JsonObject current) {
        this.single = new AptSingle(original, current);
        this.isBatch = false;
    }

    private Apt(final JsonArray original, final JsonArray current, final String field) {
        this.batch = new AptBatch(original, current, field);
        this.isBatch = true;
    }

    // -------------  Static Method for creation here --------------
    /* Ok for update only */
    public static Apt create(final JsonArray original, final JsonArray current, final String field) {
        if (Ut.isNil(original) && Ut.isNil(current)) {
            throw new AptParameterException(Apt.class);
        }
        return new Apt(original, current, field);
    }

    public static Apt create(final JsonObject original, final JsonObject current) {
        if (Ut.isNil(original) && Ut.isNil(current)) {
            throw new AptParameterException(Apt.class);
        }
        return new Apt(original, current);
    }

    public static Apt create(final JsonArray original, final JsonArray current) {
        return create(original, current, null);
    }

    public static Apt create(final JsonArray original) {
        return create(original, original.copy(), null);
    }

    // -------------  Async Method for `create` method --------------
    public static Future<Apt> inEdit(final JsonArray original) {
        return Future.succeededFuture(create(original));
    }

    public static Future<Apt> inDelete(final JsonObject original) {
        return Future.succeededFuture(create(original, null));
    }

    public static Future<Apt> inDelete(final JsonArray original) {
        return Future.succeededFuture(create(original, null));
    }

    public static Future<Apt> inAdd(final JsonObject current) {
        return Future.succeededFuture(create(null, current));
    }

    public static Future<Apt> inAdd(final JsonArray current) {
        return Future.succeededFuture(create(null, current));
    }

    // =============================================================

    // -------------  Extract data here --------------
    /*
     * Flag Meaning
     * 1. O - Old
     * 2. N - New
     * 3. I - Initialize ( Default Value )
     * 4. S - Save,     Add + Replaced
     * 5. A - Save,     Add + Appended
     * 6. K - Kill,     Replaced + Deleted
     * 7. D - Delete,   Appended + Deleted
     */
    /*
     * 「Old Data」
     * JsonObject / JsonArray Return to original data T
     **/
    @SuppressWarnings("unchecked")
    public <T> T dataO() {
        return this.isBatch ? (T) this.batch.dataO() : (T) this.single.dataO();
    }

    public <T> Future<T> dataOAsync() {
        return Future.succeededFuture(this.dataO());
    }

    /*
     * 「New Data」
     * JsonObject / JsonArray Read current data T
     * */
    public <T> T dataN() {
        return this.isBatch ? (T) this.batch.dataN() : (T) this.single.dataN();
    }

    public <T> Future<T> dataNAsync() {
        return Future.succeededFuture(this.dataN());
    }

    /*  `io` method is for current data updating, it will replace the latest data ( JsonObject / JsonArray ) */
    public <T> T dataN(final T updated) {
        return this.isBatch ? (T) this.batch.set((JsonArray) updated) : (T) this.single.set((JsonObject) updated);
    }

    public <T> Future<T> dataNAsync(final T updated) {
        return Future.succeededFuture(this.dataN(updated));
    }

    /*
     * 「Initialize Data」
     * Old Refer, default value, I - Initialized
     * */
    public <T> T dataI() {
        return this.isBatch ? (T) this.batch.dataI() : (T) this.single.dataI();
    }

    public <T> Future<T> dataIAsync() {
        return Future.succeededFuture(this.dataI());
    }

    // -------------  Data Array Part Extraction --------------
    /*
     * 「Save Data」
     * Append = false, the mode is overwritten.
     * - dataS, Overwrite mode
     * - dataA, Append mode
     */

    /*
     *      ADD     UPDATE (R + A)    DELETE
     *     -----   ----- ( Replaced )
     */
    public JsonArray dataS() {
        return this.dataS(false);
    }

    public Future<JsonArray> dataSAsync() {
        return Future.succeededFuture(this.dataS());
    }

    /*
     *      ADD     UPDATE (R + A)    DELETE
     *     -----   ----- ( Appended )
     */
    public JsonArray dataA() {
        return this.dataS(true);
    }

    public Future<JsonArray> dataAAsync() {
        return Future.succeededFuture(this.dataA());
    }

    /*
     * 「Kill Data of Delete」
     * Append = false, the mode is overwritten.
     */

    /*
     *      ADD     UPDATE (R + A)    DELETE
     *             ----- ( Replaced ) -----
     */
    public JsonArray dataK() {
        return this.dataK(false);
    }

    public Future<JsonArray> dataKAsync() {
        return Future.succeededFuture(this.dataK());
    }

    /*
     *      ADD     UPDATE (R + A)    DELETE
     *             ----- ( Appended ) -----
     */
    public JsonArray dataD() {
        return this.dataK(true);
    }

    public Future<JsonArray> dataDAsync() {
        return Future.succeededFuture(this.dataD());
    }


    // -------------  Compared Api Here --------------

    public JsonArray comparedA() {
        return this.compared.get(ChangeFlag.ADD);
    }

    public JsonArray comparedU() {
        return this.compared.get(ChangeFlag.UPDATE);
    }

    public ConcurrentMap<ChangeFlag, JsonArray> compared() {
        return this.doBatch(() -> this.compared, ConcurrentHashMap::new, "compared()");
    }

    /* ChangeFlag, Return to change flag here, ADD / UPDATE / DELETE */
    public ChangeFlag type() {
        return this.isBatch ? this.batch.type() : this.single.type();
    }

    @Fluent
    public Apt compared(final ConcurrentMap<ChangeFlag, JsonArray> compared) {
        this.doBatch(() -> {
            final ConcurrentMap<ChangeFlag, JsonArray> mapRef = this.compared();
            mapRef.clear();
            mapRef.putAll(compared);
        }, "compared(ConcurrentMap)");
        return this;
    }

    public Future<Apt> comparedAsync(final ConcurrentMap<ChangeFlag, JsonArray> compared) {
        return Future.succeededFuture(this.compared(compared));
    }

    @Fluent
    public Apt comparedA(final JsonArray inserted) {
        this.doBatch(() -> {
            final JsonArray normalized = Ut.valueJArray(inserted);
            this.compared.put(ChangeFlag.ADD, normalized);
        }, "add(JsonArray)");
        return this;
    }

    public Future<Apt> comparedAAsync(final JsonArray inserted) {
        return Future.succeededFuture(this.comparedA(inserted));
    }

    @Fluent
    public Apt comparedU(final JsonArray updated) {
        this.doBatch(() -> {
            final JsonArray normalized = Ut.valueJArray(updated);
            this.compared.put(ChangeFlag.UPDATE, normalized);
        }, "edit(JsonArray)");
        return this;
    }

    public Future<Apt> comparedUAsync(final JsonArray updated) {
        return Future.succeededFuture(this.comparedU(updated));
    }

    // -------------  Update Data --------------
    @Fluent
    public Apt update(final JsonObject updated) {
        if (this.isBatch) {
            this.batch.update(updated);
        } else {
            this.single.update(updated);
        }
        return this;
    }

    public Future<Apt> updateAsync(final JsonObject updated) {
        return Future.succeededFuture(this.update(updated));
    }

    @Fluent
    public <T> Apt set(final T updated) {
        this.dataN(updated);
        return this;
    }

    public <T> Future<Apt> setAsync(final T updated) {
        this.dataN(updated);
        return Future.succeededFuture(this);
    }

    // --------------------- Private Method --------------------

    private void doBatch(final Actuator actuator, final String method) {
        this.doBatch(() -> {
            actuator.execute();
            return null;
        }, null, method);
    }

    private <T> T doBatch(final Supplier<T> supplier, final Supplier<T> defaultSupplier, final String method) {
        final Supplier<T> valueSupplier = Objects.isNull(defaultSupplier) ? () -> null : defaultSupplier;
        if (this.isBatch) {
            return supplier.get();
        } else {
            if (Ut.isNil(method)) {
                LOGGER.warn(MSG_APT_BATCH, method);
            }
            return valueSupplier.get();
        }
    }

    /*
     * 1. Get data from current ( New Data ) array first.
     * 2. Operation on JsonArray
     * - 1). append = true ( add + appended )
     * - 2). append = false ( add + replaced )
     * 3. The data range are as following:
     *      ADD     UPDATE (R + A)    DELETE
     *             ----- ( Appended ) ------
     *             ----- ( Replaced ) ------
     */
    private JsonArray dataK(final Boolean append) {
        return this.doBatch(() -> {
            final JsonArray deleted = ((AptBatch) this.batch).dataDelete();
            final JsonArray appended = this.batch.dataA();
            final JsonArray replaced = this.batch.dataS();
            if (append) {
                return new JsonArray().addAll(deleted).addAll(appended);
            } else {
                return new JsonArray().addAll(deleted).addAll(replaced);
            }
        }, JsonArray::new, "dataK(Boolean)");
    }

    /*
     * 1. Get data from current ( New Data ) array first.
     * 2. Operation on JsonArray
     * - 1). append = true ( add + appended )
     * - 2). append = false ( add + replaced )
     * 3. The data range are as following:
     *      ADD     UPDATE (R + A)    DELETE
     *     -----   ----- ( Appended )
     *     -----   ----- ( Replaced )
     */
    private JsonArray dataS(final Boolean append) {
        return this.doBatch(() -> {
            final JsonArray add = ((AptBatch) this.batch).dataAdd();
            final JsonArray appended = this.batch.dataA();
            final JsonArray replaced = this.batch.dataS();
            if (append) {
                return new JsonArray().addAll(add).addAll(appended);
            } else {
                return new JsonArray().addAll(add).addAll(replaced);
            }
        }, JsonArray::new, "dataS(Boolean)");
    }
}
