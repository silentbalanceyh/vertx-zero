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
    private transient AidTracker trackerKit;
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
    protected WTransition createTransition(final WRequest request) {
        return WTransition.create(request, this.moveMap);
    }

    protected Future<WRequest> beforeAsync(final WRequest request, final WTransition wTransition) {
        return wTransition.start()
            // 「AOP」Before
            .compose(started -> this.trackerKit.beforeAsync(request, started));
    }

    protected Future<WRecord> afterAsync(final WRecord record, final WTransition wTransition) {
        return wTransition.start()
            // 「AOP」After
            .compose(started -> this.trackerKit.afterAsync(record, started));
    }
}
