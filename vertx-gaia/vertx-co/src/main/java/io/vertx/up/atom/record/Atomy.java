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

/*
 * This data structure is for different compare
 */
@SuppressWarnings("unchecked")
public class Atomy {
    private final static Annal LOGGER = Annal.get(Atomy.class);

    private final static String MSG_ATOMY_BATCH = "Current api does not support `isBatch = false`.";

    private final transient boolean isBatch;
    private transient AtomyOp<JsonObject> single;
    private transient AtomyOp<JsonArray> batch;

    private Atomy(final JsonObject original, final JsonObject current) {
        this.single = new AtomySingle(original, current);
        this.isBatch = false;
    }

    private Atomy(final JsonArray original, final JsonArray current) {
        this.batch = new AtomyBatch(original, current, null);
        this.isBatch = true;
    }

    private Atomy(final JsonArray original, final JsonArray current, final String field) {
        this.batch = new AtomyBatch(original, current, field);
        this.isBatch = true;
    }

    public static Atomy create(final JsonArray original, final JsonArray current, final String field) {
        /*
         * Ok for update only
         */
        if (Objects.isNull(original) || Objects.isNull(current)) {
            throw new AtomyParameterException();
        }
        return new Atomy(original, current, field);
    }

    public static Atomy create(final JsonObject original, final JsonObject current) {
        if (Objects.isNull(original) && Objects.isNull(current)) {
            throw new AtomyParameterException();
        }
        return new Atomy(original, current);
    }

    public static Atomy create(final JsonArray original, final JsonArray current) {
        if (Ut.isNil(original) && Ut.isNil(current)) {
            throw new AtomyParameterException();
        }
        return new Atomy(original, current);
    }

    // -------------  Common Api for data getting --------------
    /*
     * JsonObject / JsonArray
     * Return to original data T
     */
    @SuppressWarnings("unchecked")
    public <T> T original() {
        final T reference;
        if (this.isBatch) {
            reference = (T) this.batch.original();
        } else {
            final JsonObject dataRef = this.single.original();
            if (Objects.isNull(dataRef)) {
                reference = null;
            } else {
                reference = (T) dataRef;
            }
        }
        return reference;
    }

    /*
     * ChangeFlag
     * Return to change flag here
     * ADD / UPDATE / DELETE
     */
    public ChangeFlag type() {
        if (this.isBatch) {
            return this.batch.type();
        } else {
            return this.single.type();
        }
    }

    /*
     * JsonObject / JsonArray
     * Read current data T
     */
    public <T> T current() {
        final T reference;
        if (this.isBatch) {
            reference = (T) this.batch.current();
        } else {
            final JsonObject dataRef = this.single.current();
            if (Objects.isNull(dataRef)) {
                reference = null;
            } else {
                reference = (T) dataRef;
            }
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

    /*
     * `io` method is for current data updating, it will replace the
     *  latest data ( JsonObject / JsonArray )
     */
    public <T> T current(final T updated) {
        final T reference;
        if (this.isBatch) {
            reference = (T) this.batch.current((JsonArray) updated);
        } else {
            reference = (T) this.single.current((JsonObject) updated);
        }
        return reference;
    }

    public <T> Future<T> currentAsync(final T updated) {
        return Future.succeededFuture(this.current(updated));
    }

    @Fluent
    public <T> Atomy next(final T updated) {
        this.current(updated);
        return this;
    }

    @Fluent
    public <T> Future<Atomy> nextAsync(final T updated) {
        this.current(updated);
        return Future.succeededFuture(this);
    }

    /*
     * Batch operation api here
     * - compared                   - Read Io
     * - comparedAsync              - Read Io
     * - compared(compared)         - Write Io
     * - add(JsonArray)             - Replace flag = ADD queue
     * - update(JsonArray)          - Replace flag = UPDATE queue
     * - update(JsonObject)         - Update data based on `JsonObject`
     *
     * @return
     */
    public ConcurrentMap<ChangeFlag, JsonArray> compared() {
        if (this.isBatch) {
            return this.batch.compared();
        } else {
            LOGGER.warn(MSG_ATOMY_BATCH);
            return new ConcurrentHashMap<>();
        }
    }

    public Future<ConcurrentMap<ChangeFlag, JsonArray>> comparedAsync() {
        return Future.succeededFuture(this.compared());
    }

    @Fluent
    public Atomy compared(final ConcurrentMap<ChangeFlag, JsonArray> compared) {
        if (this.isBatch) {
            this.batch.compared(compared);
        } else {
            LOGGER.warn(MSG_ATOMY_BATCH);
        }
        return this;
    }

    @Fluent
    public Atomy add(final JsonArray inserted) {
        if (this.isBatch) {
            final JsonArray normalized = Ut.sureJArray(inserted);
            this.batch.compared().put(ChangeFlag.ADD, normalized);
        }
        return this;
    }

    public JsonArray add() {
        return this.batch.compared(ChangeFlag.ADD);
    }

    @Fluent
    public Atomy update(final JsonArray updated) {
        if (this.isBatch) {
            final JsonArray normalized = Ut.sureJArray(updated);
            this.batch.compared().put(ChangeFlag.UPDATE, normalized);
        }
        return this;
    }

    public Atomy update(final JsonObject updated) {
        if (this.isBatch) {
            this.batch.update(updated);
        } else {
            this.single.update(updated);
        }
        return this;
    }

    public Future<Atomy> updateAsync(final JsonObject updated) {
        return Future.succeededFuture(this.update(updated));
    }

    public JsonArray update() {
        return this.batch.compared(ChangeFlag.UPDATE);
    }
}
