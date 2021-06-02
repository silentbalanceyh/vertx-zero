package io.vertx.up.atom.record;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.exception.heart.AtomyParameterException;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

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
        if (Objects.isNull(original) || Objects.isNull(current)) throw new AtomyParameterException();
        return new Apt(original, current, field);
    }

    public static Apt create(final JsonObject original, final JsonObject current) {
        if (Objects.isNull(original) && Objects.isNull(current)) throw new AtomyParameterException();
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
        final T reference;
        if (this.isBatch) {
            reference = (T) this.batch.dataO();
        } else {
            reference = (T) this.single.dataO();
        }
        return reference;
    }

    /* ChangeFlag, Return to change flag here, ADD / UPDATE / DELETE */
    public ChangeFlag type() {
        if (this.isBatch) {
            return this.batch.type();
        } else {
            return this.single.type();
        }
    }

    /* JsonObject / JsonArray Read current data T */
    public <T> T dataN() {
        final T reference;
        if (this.isBatch) {
            reference = (T) this.batch.dataN();
        } else {
            reference = (T) this.single.dataN();
        }
        return reference;
    }

    /*
     * Single operation here, select value
     * This method is for most usage
     *
     * 1. JsonObject:
     * --- ADD,
     *     original = null, current = value,
     *     return to `current` Json
     * --- DELETE,
     *     original = value, current = null,
     *     return to `original` Json
     * --- UPDATE,
     *     original = value, current = value,
     *     return to combined json, `original` + `current` both.
     *
     * 2. JsonArray:
     * --- ADD,
     *     original = null / Empty, current = value
     *     return to `current` Json Array
     * --- DELETE,
     *     original = value, current = null / Empty
     *     return to `original` Json Array
     * --- UPDATE,
     *     original = value, current = value
     *     This situation is specific, the system could not combine two array because there
     *     missed rule, in this kind of situation, the reference should be `original` array instead
     */
    public <T> T data() {
        if (this.isBatch) {
            return (T) this.batch.data();
        } else {
            return (T) this.single.data();
        }
    }

    /*  `io` method is for current data updating, it will replace the latest data ( JsonObject / JsonArray ) */
    public <T> T dataN(final T updated) {
        final T reference;
        if (this.isBatch) {
            reference = (T) this.batch.set((JsonArray) updated);
        } else {
            reference = (T) this.single.set((JsonObject) updated);
        }
        return reference;
    }

    public <T> Future<T> dataNAsync(final T updated) {
        return Future.succeededFuture(this.dataN(updated));
    }

    public JsonArray dataAdd() {
        return this.batch.compared().get(ChangeFlag.ADD);
    }


    public JsonArray dataEdit() {
        return this.batch.compared().get(ChangeFlag.UPDATE);
    }

    public ConcurrentMap<ChangeFlag, JsonArray> dataCompared() {
        if (this.isBatch) {
            return this.batch.compared();
        } else {
            LOGGER.warn(MSG_APT_BATCH, "compared()");
            return new ConcurrentHashMap<>();
        }
    }

    // =============================================================
    @Fluent
    public Apt add(final JsonArray inserted) {
        if (this.isBatch) {
            final JsonArray normalized = Ut.sureJArray(inserted);
            this.batch.compared().put(ChangeFlag.ADD, normalized);
        }
        return this;
    }

    public Future<Apt> addAsync(final JsonArray inserted) {
        return Future.succeededFuture(this.add(inserted));
    }

    @Fluent
    public Apt edit(final JsonArray updated) {
        if (this.isBatch) {
            final JsonArray normalized = Ut.sureJArray(updated);
            this.batch.compared().put(ChangeFlag.UPDATE, normalized);
        }
        return this;
    }

    public Future<Apt> editAsync(final JsonArray updated) {
        return Future.succeededFuture(this.edit(updated));
    }

    @Fluent
    public Apt edit(final JsonObject updated) {
        if (this.isBatch) {
            this.batch.update(updated);
        } else {
            this.single.update(updated);
        }
        return this;
    }

    public Future<Apt> editAsync(final JsonObject updated) {
        return Future.succeededFuture(this.edit(updated));
    }

    @Fluent
    public <T> Apt set(final T updated) {
        this.dataN(updated);
        return this;
    }

    @Fluent
    public Apt set(final ConcurrentMap<ChangeFlag, JsonArray> compared) {
        if (this.isBatch) {
            final ConcurrentMap<ChangeFlag, JsonArray> mapRef = this.dataCompared();
            mapRef.clear();
            mapRef.putAll(compared);
        } else {
            LOGGER.warn(MSG_APT_BATCH, "compared(ConcurrentMap)");
        }
        return this;
    }

    public <T> Future<Apt> setAsync(final T updated) {
        this.dataN(updated);
        return Future.succeededFuture(this);
    }

}
