package io.vertx.tp.workflow.uca.component;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WRecord;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTransfer implements Behaviour {
    private final transient JsonObject config = new JsonObject();
    private transient WRecord record;

    @Override
    public Behaviour bind(final JsonObject config) {
        final JsonObject sure = Ut.sureJObject(config);
        this.config.mergeIn(sure.copy(), true);
        if (sure.containsKey(KName.RECORD)) {
            this.record = Ut.deserialize(sure.getJsonObject(KName.RECORD), WRecord.class);
        }
        return this;
    }

    protected JsonObject configuration() {
        return this.config.getJsonObject(KName.Flow.TODO, new JsonObject());
    }

    protected WRecord record() {
        return this.record;
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
