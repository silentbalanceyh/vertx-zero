package io.vertx.tp.workflow.uca.component;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.WMove;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractDivert implements Divert {

    protected final transient JsonObject config = new JsonObject();

    private final transient ConcurrentMap<String, WMove> moveMap = new ConcurrentHashMap<>();

    @Override
    public Divert bind(final JsonObject config) {
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

    protected WMove moveGet(final String node) {
        return this.moveMap.getOrDefault(node, WMove.empty());
    }
}
