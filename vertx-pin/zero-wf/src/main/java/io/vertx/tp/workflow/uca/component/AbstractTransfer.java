package io.vertx.tp.workflow.uca.component;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.element.WDecision;
import io.vertx.tp.workflow.atom.element.WRecord;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTransfer implements Behaviour {
    private final transient JsonObject config = new JsonObject();
    private final transient ConcurrentMap<String, WDecision> decision = new ConcurrentHashMap<>();
    private transient WRecord record;

    @Override
    public Behaviour bind(final JsonObject config) {
        final JsonObject sure = Ut.sureJObject(config);
        this.config.mergeIn(sure.copy(), true);
        if (sure.containsKey(KName.RECORD)) {
            this.record = Ux.fromJson(sure.getJsonObject(KName.RECORD), WRecord.class);
        }
        if (sure.containsKey(KName.Flow.DECISION)) {
            final List<WDecision> decisions = Ux.fromJson(sure.getJsonArray(KName.Flow.DECISION), WDecision.class);
            decisions.forEach(decision -> this.decision.put(decision.getNode(), decision));
        }
        return this;
    }

    protected JsonObject configuration() {
        return this.config.getJsonObject(KName.Flow.TODO, new JsonObject());
    }

    protected WRecord record() {
        return this.record;
    }

    protected WDecision decision(final String node) {
        return this.decision.get(node);
    }

    // Data Json for `record` and `todo`
    protected JsonObject requestR(final JsonObject params) {
        JsonObject rData = params.getJsonObject(KName.RECORD, new JsonObject());
        if (Ut.notNil(rData)) {
            rData = rData.copy();
        }
        return rData;
    }

    protected JsonObject requestT(final JsonObject params) {
        final JsonObject request = params.copy();
        request.remove(KName.RECORD);
        request.remove(KName.Flow.WORKFLOW);
        return request;
    }
}
