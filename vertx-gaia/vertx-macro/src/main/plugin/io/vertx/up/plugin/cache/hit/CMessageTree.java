package io.vertx.up.plugin.cache.hit;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.TreeMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class CMessageTree extends AbstractCMessage {
    private transient String id;

    public CMessageTree(final Object id, final Class<?> type) {
        super(type);
        if (Objects.nonNull(id)) {
            this.id = id.toString();
        }
    }

    @Override
    public String dataKey() {
        final TreeMap<String, String> treeMap = this.dataMap(this.id);
        final L1Algorithm algorithm = Ut.singleton(AlgorithmRecord.class);
        return algorithm.dataTreeKey(this.typeName(), treeMap);
    }

    @Override
    public boolean isRef() {
        return Boolean.TRUE;
    }

    @Override
    public boolean isList() {
        return Boolean.TRUE;
    }

    @Override
    public JsonObject dataOverwrite() {
        /*
         * {
         *      "data": "The tree key that will be removed."
         * }
         */
        return new JsonObject().put("data", this.dataKey());
    }
}
