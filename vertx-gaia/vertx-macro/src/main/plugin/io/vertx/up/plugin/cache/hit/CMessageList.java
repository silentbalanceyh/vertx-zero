package io.vertx.up.plugin.cache.hit;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

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
        // Fix BUG: Illegal type in JsonObject: class java.util.HashSet
        final Object normValue;
        if (value instanceof Set) {
            normValue = Ut.toJArray(value);
        } else {
            normValue = value;
        }
        this.condition.put(field, normValue);
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
