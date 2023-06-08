package io.vertx.mod.workflow.atom.runtime;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.extension.KFlow;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.time.Instant;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WRequest implements Serializable {
    private final transient JsonObject request = new JsonObject();

    private final transient KFlow flow;

    private transient WRecord record;

    public WRequest(final JsonObject params) {
        final JsonObject request = Ut.valueJObject(params);
        /*
         * Fix issue of `createdAt/createdBy` that are not standard in request JsonObject
         * In Zero Framework, the field `createdAt/createdBy` must be bind to method POST
         * When the method is `PUT`, these two fields are `null`, it means that we must
         * put `createdAt/createdBy` manually when it's null here to build request
         */
        if (!request.containsKey(KName.CREATED_AT)) {
            request.put(KName.CREATED_AT, Instant.now());
        }
        if (!request.containsKey(KName.CREATED_BY)) {
            final String user = request.getString(KName.UPDATED_BY, null);
            if (Ut.isNotNil(user)) {
                request.put(KName.CREATED_BY, user);
            }
        }
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

    public WRecord record() {
        return this.record;
    }

    public Future<WRecord> record(final WRecord record) {
        this.record = record;
        return Ux.future(record);
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
}
