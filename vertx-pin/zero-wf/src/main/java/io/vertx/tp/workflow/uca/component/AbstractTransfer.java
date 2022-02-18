package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.up.eon.KName;
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

    @Override
    public Behaviour bind(final JsonObject config) {
        final JsonObject sure = Ut.sureJObject(config);
        this.config.mergeIn(sure.copy(), true);
        this.config.fieldNames().stream()
            // Ignore `record` and `todo` configuration key
            .filter(field -> !KName.RECORD.equals(field))
            .filter(field -> !KName.Flow.TODO.equals(field))
            .filter(field -> !KName.LINKAGE.equals(field))
            .forEach(field -> {
                final JsonObject value = this.config.getJsonObject(field);
                final WMove item = WMove.create(field, value);
                this.moveMap.put(field, item);
            });
        return this;
    }

    @Override
    public Behaviour bind(final MetaInstance metadata) {
        // Empty Binding on Instance
        return this;
    }


    protected WMove moveGet(final String node) {
        return this.moveMap.getOrDefault(node, WMove.empty());
    }

    /*
     * Record UPDATE Processing
     */
    protected Future<JsonObject> updateAsync(final JsonObject params, final MetaInstance metadata) {
        final KtRecord recordKit = KtRecord.toolkit(metadata);
        return recordKit.updateAsync(params)
            /* Record must be put in `params` -> `record` field */
            .compose(record -> this.recordPost(params, record));
    }

    /*
     * Record Indent Processing
     * ( Reserved )
     */
    protected Future<JsonObject> insertAsync(final JsonObject params, final MetaInstance metadata) {
        final KtRecord recordKit = KtRecord.toolkit(metadata);
        return recordKit.insertAsync(params)
            /* Record must be put in `params` -> `record` field */
            .compose(record -> this.recordPost(params, record));
    }

    /*
     * Record Save Processing
     */
    protected Future<JsonObject> saveAsync(final JsonObject params, final MetaInstance metadata) {
        final KtRecord recordKit = KtRecord.toolkit(metadata);
        if (Objects.isNull(this.config)) {
            return Ux.future(params);
        }
        return recordKit.saveAsync(params)
            /* Record must be put in `params` -> `record` field */
            .compose(record -> this.recordPost(params, record));
    }

    private Future<JsonObject> recordPost(final JsonObject params, final JsonObject record) {
        return Ux.future(params.put(KName.RECORD, record));
    }
}
