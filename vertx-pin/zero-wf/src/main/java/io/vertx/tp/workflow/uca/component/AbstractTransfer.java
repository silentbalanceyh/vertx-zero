package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigLinkage;
import io.vertx.tp.workflow.atom.ConfigRecord;
import io.vertx.tp.workflow.atom.ConfigTodo;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTransfer implements Behaviour {
    protected final transient JsonObject config = new JsonObject();
    private final transient ConcurrentMap<String, WMove> moveMap = new ConcurrentHashMap<>();

    private transient KtRecord recordKit;

    @Override
    public Behaviour bind(final JsonObject config) {
        final JsonObject sure = Ut.sureJObject(config);
        this.config.mergeIn(sure.copy(), true);
        this.config.fieldNames().stream()
            // Ignore `record` and `todo` configuration key
            .filter(field -> !KName.RECORD.equals(field))
            .filter(field -> !KName.Flow.TODO.equals(field))
            .forEach(field -> {
                final JsonObject value = this.config.getJsonObject(field);
                final WMove item = WMove.create(field, value);
                this.moveMap.put(field, item);
            });
        return this;
    }

    @Override
    public Behaviour bind(final ConfigTodo todo, final ConfigLinkage linkage) {
        // Not Required for this component
        return this;
    }

    @Override
    public Behaviour bind(final ConfigRecord record) {
        Objects.requireNonNull(record);
        this.recordKit = new KtRecord(record);
        return this;
    }


    protected WMove moveGet(final String node) {
        return this.moveMap.getOrDefault(node, WMove.empty());
    }

    protected ChangeFlag recordMode() {
        return this.recordKit.mode();
    }

    /*
     * Record UPDATE Processing
     */
    protected Future<JsonObject> recordUpdate(final JsonObject params, final ConfigTodo config) {
        return this.recordKit.updateAsync(params, config)
            /* Record must be put in `params` -> `record` field */
            .compose(record -> this.recordPost(params, record));
    }

    /*
     * Record Indent Processing
     * ( Reserved )
     */
    protected Future<JsonObject> recordInsert(final JsonObject params, final ConfigTodo config) {
        return this.recordKit.insertAsync(params, config)
            /* Record must be put in `params` -> `record` field */
            .compose(record -> this.recordPost(params, record));
    }

    /*
     * Record Save Processing
     */
    protected Future<JsonObject> recordSave(final JsonObject params, final ConfigTodo config) {
        if (Objects.isNull(config)) {
            return Ux.future(params);
        }
        return this.recordKit.saveAsync(params, config)
            /* Record must be put in `params` -> `record` field */
            .compose(record -> this.recordPost(params, record));
    }

    private Future<JsonObject> recordPost(final JsonObject params, final JsonObject record) {
        return Ux.future(params.put(KName.RECORD, record));
    }
}
