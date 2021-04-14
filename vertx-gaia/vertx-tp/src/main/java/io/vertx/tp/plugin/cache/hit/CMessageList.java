package io.vertx.tp.plugin.cache.hit;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CMessageList extends AbstractCMessage {
    private final transient JsonObject condition = new JsonObject();

    public CMessageList(final JsonObject condition, final Class<?> type) {
        super(type);
        if (Objects.nonNull(condition)) {
            this.condition.mergeIn(condition, true);
        }
    }

    public CMessageList(final String field, final Object value, final Class<?> type) {
        super(type);
        this.condition.put(field, value);
    }

    @Override
    public String dataKey() {
        /*
         * Single Record
         */
        final L1Algorithm algorithm = Ut.singleton(AlgorithmCollection.class);
        return algorithm.dataKey(this.typeName(), this.condition);
    }

    @Override
    public boolean isList() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isRef() {
        return Boolean.TRUE;
    }

    @Override
    public JsonObject dataOverwrite() {
        return new JsonObject().put("condition", this.condition.copy());
    }
}
