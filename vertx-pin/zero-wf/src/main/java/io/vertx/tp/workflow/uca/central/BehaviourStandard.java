package io.vertx.tp.workflow.uca.central;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.configuration.MetaInstance;
import io.vertx.tp.workflow.atom.runtime.WMove;
import io.vertx.tp.workflow.atom.runtime.WRecord;
import io.vertx.tp.workflow.atom.runtime.WRequest;
import io.vertx.tp.workflow.atom.runtime.WTransition;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BehaviourStandard implements Behaviour {
    protected final transient JsonObject config = new JsonObject();
    private final transient ConcurrentMap<String, WMove> moveMap = new ConcurrentHashMap<>();
    protected transient AidTracker trackerKit;
    private transient MetaInstance metadata;

    @Override
    public Behaviour bind(final JsonObject config) {
        final JsonObject sure = Ut.valueJObject(config);
        this.config.mergeIn(sure.copy(), true);
        this.config.fieldNames().stream()
            // Ignore `record` and `todo` configuration key
            .filter(field -> !KName.RECORD.equals(field))
            .filter(field -> !KName.Flow.TODO.equals(field))
            .filter(field -> !KName.LINKAGE.equals(field))
            .forEach(field -> {
                final JsonObject value = this.config.getJsonObject(field);
                this.moveMap.put(field, WMove.create(field, value));
            });
        return this;
    }

    protected MetaInstance metadataIn() {
        return this.metadata;
    }

    @Override
    public Behaviour bind(final MetaInstance metadata) {
        // Empty Binding on Instance
        Objects.requireNonNull(metadata);
        this.metadata = metadata;
        this.trackerKit = new AidTracker(metadata);
        return this;
    }

    // ==================== Rule Bind / Get ======================
    /*
     * BehaviourStandard contains only one definition for moving here
     * This move map will be bind after the component has been created, in future usage this instance
     * will be passed into `WTransition` to store, instead of the old version,
     * WTransition will wrap the `WMove/WRule` etc and abay following rules:
     * 1) WMove won't be exposed to any component out of WTransition
     * 2) WRule could be exposed to all the component for selection the path
     *
     * This action will be done inner WTransition
     */
    protected ConcurrentMap<String, WMove> rules() {
        return this.moveMap;
    }


    // ==================== Before / After Processing in Current component ======================
    protected Future<WRequest> beforeAsync(final WRequest request, final WTransition transition) {
        // Instance Building
        return transition.start()
            /* 「Aop」Before based on WTransition */
            .compose(started -> this.trackerKit.beforeAsync(request, started));
    }

    protected Future<WRecord> afterAsync(final WRecord record, final WTransition transition) {
        // Started Workflow
        return transition.start()
            /* 「Aop」After based on WMove */
            .compose(started -> this.trackerKit.afterAsync(record, started));
    }
}
