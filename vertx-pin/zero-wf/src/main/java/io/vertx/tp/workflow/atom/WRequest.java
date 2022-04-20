package io.vertx.tp.workflow.atom;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.experiment.specification.KFlow;
import io.vertx.up.unity.Ux;
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

    private final transient KFlow flow;
    private final transient Set<WRecord> recordSet = new HashSet<>();
    private transient WRecord recordBefore;
    private transient WRecord recordAfter;

    public WRequest(final JsonObject params) {
        final JsonObject request = Ut.valueJObject(params);
        this.request.mergeIn(request, true);
        this.flow = KFlow.build(request);
    }

    // =================== Extract Data =======================

    public JsonObject request() {
        return this.request.copy();
    }

    public KFlow workflow() {
        return this.flow;
    }

    @SuppressWarnings("unchecked")
    public <T> T after(final boolean batch) {
        if (batch) {
            return (T) this.recordSet;
        } else {
            return (T) this.recordAfter;
        }
    }

    public WRecord before() {
        return this.recordBefore;
    }

    public Future<WRecord> beforeRecord(final WRecord recordOld) {
        this.recordBefore = recordOld;
        return Ux.future(recordOld);
    }

    public Future<WRecord> afterRecord(final WRecord recordNew) {
        this.recordAfter = recordNew;
        return Ux.future(recordNew);
    }

    // =================== Fluent Method for Set =======================

    @Fluent
    public Future<WRequest> future(final JsonObject request) {
        return Ux.future(this.request(request));
    }

    @Fluent
    public WRequest request(final JsonObject request) {
        final JsonObject params = Ut.valueJObject(request);
        this.request.mergeIn(params, true);
        return this;
    }

    @Fluent
    public WRequest before(final WRecord recordOld) {
        this.recordBefore = recordOld;
        return this;
    }


    @Fluent
    public WRequest after(final WRecord recordNew) {
        this.recordAfter = recordNew;
        return this;
    }

    @Fluent
    public WRequest after(final Set<WRecord> recordSet) {
        if (Objects.nonNull(recordSet)) {
            this.recordSet.clear();
            this.recordSet.addAll(recordSet);
        }
        return this;
    }

    @Fluent
    public WRequest elementAdd(final WRecord record) {
        this.recordSet.add(record);
        return this;
    }

    @Fluent
    public WRequest elementClean() {
        this.recordSet.clear();
        return this;
    }
}
