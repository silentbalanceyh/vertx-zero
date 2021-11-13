package io.vertx.tp.workflow.uca.component;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractTransfer implements Behaviour {
    protected transient JsonObject config = new JsonObject();

    @Override
    public Behaviour bind(final JsonObject config) {
        final JsonObject sure = Ut.sureJObject(config);
        this.config.mergeIn(sure.copy(), true);
        return this;
    }

    protected JsonObject configuration() {
        return this.config.getJsonObject(KName.Flow.TODO, new JsonObject());
    }

    protected ChangeFlag opType() {
        final JsonObject record = this.config.getJsonObject(KName.RECORD, new JsonObject());
        return Ut.toEnum(() -> record.getString(KName.FLAG), ChangeFlag.class, ChangeFlag.NONE);
    }

    protected String opUnique() {
        final JsonObject record = this.config.getJsonObject(KName.RECORD, new JsonObject());
        return record.getString(KName.UNIQUE);
    }

    protected JsonObject opData(final JsonObject params) {
        return params.getJsonObject(KName.RECORD, null);
    }
}
