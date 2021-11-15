package io.vertx.tp.workflow.uca.component;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.ConfigRecord;
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
    private transient ConfigRecord record;

    @Override
    public Behaviour bind(final JsonObject config) {
        final JsonObject sure = Ut.sureJObject(config);
        this.config.mergeIn(sure.copy(), true);
        if (sure.containsKey(KName.Flow.NODE)) {
            final JsonObject configData = sure.getJsonObject(KName.Flow.NODE);
            Ut.<JsonObject>itJObject(configData, (value, field) -> {
                final WMove item = Ux.fromJson(value, WMove.class);
                item.setNode(field);
                this.moveMap.put(field, item);
            });
        }
        return this;
    }

    @Override
    public Behaviour bind(final ConfigRecord record) {
        Objects.requireNonNull(record);
        this.record = record;
        return this;
    }


    protected WMove configN(final String node) {
        return this.moveMap.get(node);
    }

    protected ConfigRecord configR() {
        return this.record;
    }

    /*
     * {
     *     "record": "...",
     * }
     * - record: The json data of record
     * - The json data of todo is the major key=value
     */
    protected JsonObject dataR(final JsonObject params, final boolean isNew) {
        JsonObject rData = params.getJsonObject(KName.RECORD, new JsonObject());
        if (Ut.notNil(rData)) {
            rData = rData.copy();
        }
        // Auditor Processing
        if (isNew) {
            Ut.ifJAssign(params,
                KName.CREATED_AT,
                KName.CREATED_BY
            ).apply(rData);
        }
        Ut.ifJAssign(params,
            KName.UPDATED_AT,
            KName.UPDATED_BY,
            KName.SIGMA
        ).apply(rData);
        return rData;
    }

    protected JsonObject dataM(final JsonObject params, final WMove move) {
        final ConcurrentMap<String, String> pattern = move.getData();
        final JsonObject request = new JsonObject();
        pattern.forEach((to, from) -> request.put(to, params.getValue(from)));
        return request;
    }
}
