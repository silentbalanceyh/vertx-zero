package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.*;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.tp.workflow.uca.runner.EventOn;
import io.vertx.up.atom.Refer;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.camunda.bpm.engine.task.Task;

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
                final WMove item = WMove.create(field, value);
                this.moveMap.put(field, item);
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
    protected ConcurrentMap<String, WMove> rules() {
        return this.moveMap;
    }

    protected void rules(final ConcurrentMap<String, WMove> moveMap) {
        if (Objects.nonNull(moveMap)) {
            this.moveMap.clear();
            this.moveMap.putAll(moveMap);
        }
    }

    protected WMove rule(final String node) {
        return this.moveMap.getOrDefault(node, WMove.empty());
    }

    protected Future<WRequest> beforeAsync(final WRequest request, final Refer process) {
        // Instance Building
        return Wf.process(request)
            /* Bind WProcess reference */
            .compose(process::future)
            /* Extract WMove from WProcess smartly */
            .compose(instance -> {
                if (instance.isStart()) {
                    // Started Workflow
                    return this.ruleAsync(instance);
                } else {
                    // Not Started Workflow
                    return this.ruleAsync(request);
                }
            })
            /* 「Aop」Before based on WMove */
            .compose(move -> this.trackerKit.beforeAsync(request, move));
    }

    protected Future<WRecord> afterAsync(final WRecord record, final WProcess process) {
        // Started Workflow
        return this.ruleAsync(process)
            /* 「Aop」After based on WMove */
            .compose(move -> this.trackerKit.afterAsync(record, move));
    }

    // ==================== Rule Bind / Get ======================
    private Future<WMove> ruleAsync(final WProcess process) {
        final Task task = process.task();
        final String node = task.getTaskDefinitionKey();
        Wf.Log.infoWeb(this.getClass(), "Flow Started, rule fetched by {0}", node);
        return Ux.future(this.rule(node));
    }

    private Future<WMove> ruleAsync(final WRequest request) {
        final EventOn eventOn = EventOn.get();
        final KFlow workflow = request.workflow();
        final String node = workflow.definitionId();
        Wf.Log.infoWeb(this.getClass(), "Flow Not Started, rule fetched by {0}", node);
        return eventOn.start(node)
            .compose(event -> Ux.future(this.rule(event.getId())));
    }
}
