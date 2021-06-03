package io.vertx.up.atom.record;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.exception.heart.AptParameterException;
import io.vertx.up.fn.Actuator;
import io.vertx.up.log.Annal;
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
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("unchecked")
public class Apt {
    private final static Annal LOGGER = Annal.get(Apt.class);

    private final static String MSG_APT_BATCH = "Current api does not support `isBatch = false`. Method = {0}";

    private final transient boolean isBatch;
    private transient AptOp<JsonObject> single;
    private transient AptOp<JsonArray> batch;

    private transient final ConcurrentMap<ChangeFlag, JsonArray> compared = new ConcurrentHashMap<>();

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
        if (Ut.isNil(original) && Ut.isNil(current)) throw new AptParameterException();
        return new Apt(original, current, field);
    }

    public static Apt create(final JsonObject original, final JsonObject current) {
        if (Ut.isNil(original) && Ut.isNil(current)) throw new AptParameterException();
        return new Apt(original, current);
    }

    public static Apt create(final JsonArray original, final JsonArray current) {
        return create(original, current, null);
    }

    // =============================================================

    // -------------  Extract data here --------------
    /* JsonObject / JsonArray Return to original data T */
    @SuppressWarnings("unchecked")
    public <T> T dataO() {
        return this.isBatch ?
                (T) this.batch.dataO() :
                (T) this.single.dataO();
    }

    /* ChangeFlag, Return to change flag here, ADD / UPDATE / DELETE */
    public ChangeFlag type() {
        return this.isBatch ? this.batch.type() : this.single.type();
    }

    /* JsonObject / JsonArray Read current data T */
    public <T> T dataN() {
        return this.isBatch ?
                (T) this.batch.dataN() :
                (T) this.single.dataN();
    }

    /* Old Refer */
    public <T> T dataDft() {
        return this.isBatch ?
                (T) this.batch.dataDft() :
                (T) this.single.dataDft();
    }

    /*  `io` method is for current data updating, it will replace the latest data ( JsonObject / JsonArray ) */
    public <T> T dataN(final T updated) {
        return this.isBatch ?
                (T) this.batch.set((JsonArray) updated) :
                (T) this.single.set((JsonObject) updated);
    }

    public JsonArray dataS() {
        return this.dataS(false);
    }

    public JsonArray dataK() {
        return this.dataK(false);
    }

    public JsonArray dataK(final Boolean append) {
        return this.doBatch(() -> {
            final JsonArray deleted = ((AptBatch) this.batch).dataDelete();
            final JsonArray appended = this.batch.dataA();
            final JsonArray replaced = this.batch.dataR();
            if (append) {
                return new JsonArray().addAll(deleted).addAll(appended);
            } else {
                return new JsonArray().addAll(deleted).addAll(replaced);
            }
        }, JsonArray::new, "dataK(Boolean)");
    }

    public JsonArray dataS(final Boolean append) {
        return this.doBatch(() -> {
            final JsonArray add = ((AptBatch) this.batch).dataAdd();
            final JsonArray appended = this.batch.dataA();
            final JsonArray replaced = this.batch.dataR();
            if (append) {
                return new JsonArray().addAll(add).addAll(appended);
            } else {
                return new JsonArray().addAll(add).addAll(replaced);
            }
        }, JsonArray::new, "dataS(Boolean)");
    }

    public <T> Future<T> dataNAsync(final T updated) {
        return Future.succeededFuture(this.dataN(updated));
    }

    public JsonArray comparedA() {
        return this.compared.get(ChangeFlag.ADD);
    }

    public JsonArray comparedU() {
        return this.compared.get(ChangeFlag.UPDATE);
    }

    public ConcurrentMap<ChangeFlag, JsonArray> compared() {
        return this.doBatch(() -> this.compared, ConcurrentHashMap::new, "compared()");
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
            final JsonArray normalized = Ut.sureJArray(inserted);
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
            final JsonArray normalized = Ut.sureJArray(updated);
            this.compared.put(ChangeFlag.UPDATE, normalized);
        }, "edit(JsonArray)");
        return this;
    }

    public Future<Apt> comparedUAsync(final JsonArray updated) {
        return Future.succeededFuture(this.comparedU(updated));
    }

    @Fluent
    public Apt comparedU(final JsonObject updated) {
        if (this.isBatch) {
            this.batch.update(updated);
        } else {
            this.single.update(updated);
        }
        return this;
    }

    public Future<Apt> comparedUAsync(final JsonObject updated) {
        return Future.succeededFuture(this.comparedU(updated));
    }

    @Fluent
    public <T> Apt set(final T updated) {
        this.dataN(updated);
        return this;
    }

    public void doBatch(final Actuator actuator, final String method) {
        this.doBatch(() -> {
            actuator.execute();
            return null;
        }, null, method);
    }

    public <T> T doBatch(final Supplier<T> supplier, final Supplier<T> defaultSupplier, final String method) {
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

    public <T> Future<Apt> setAsync(final T updated) {
        this.dataN(updated);
        return Future.succeededFuture(this);
    }

}
