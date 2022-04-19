package io.vertx.tp.workflow.atom;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WRequest implements Serializable {
    private final transient JsonObject request = new JsonObject();
    private final transient Set<WRecord> recordSet = new HashSet<>();
    private transient WRecord recordBefore;
    private transient WRecord recordAfter;

    public WRequest(final JsonObject params) {
        final JsonObject request = Ut.valueJObject(params);
        this.request.mergeIn(request, true);
    }

    public JsonObject request() {
        return this.request.copy();
    }

    public WRequest request(final JsonObject request) {
        final JsonObject params = Ut.valueJObject(request);
        this.request.mergeIn(params, true);
        return this;
    }

    public WRequest before(final WRecord recordOld) {
        this.recordBefore = recordOld;
        return this;
    }

    public WRecord before() {
        return this.recordBefore;
    }

    @SuppressWarnings("unchecked")
    public <T> T after(final boolean batch) {
        if (batch) {
            return (T) this.recordSet;
        } else {
            return (T) this.recordAfter;
        }
    }

    public WRequest after(final WRecord recordNew) {
        this.recordAfter = recordNew;
        return this;
    }

    public WRequest after(final Set<WRecord> recordSet) {
        if (Objects.nonNull(recordSet)) {
            this.recordSet.clear();
            this.recordSet.addAll(recordSet);
        }
        return this;
    }

    public WRequest elementAdd(final WRecord record) {
        this.recordSet.add(record);
        return this;
    }

    public WRequest elementClean() {
        this.recordSet.clear();
        return this;
    }
}
