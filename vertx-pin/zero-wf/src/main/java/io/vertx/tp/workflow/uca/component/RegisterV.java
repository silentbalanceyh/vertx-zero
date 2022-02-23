package io.vertx.tp.workflow.uca.component;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.atom.MetaInstance;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RegisterV extends AbstractRecord {
    @Override
    public Future<JsonObject> insertAsync(final JsonObject params, final MetaInstance metadata) {
        Wf.Log.infoMove(this.getClass(), "`virtual` configured to true");
        return Ux.future(params);
    }

    @Override
    public Future<JsonObject> updateAsync(final JsonObject params, final MetaInstance metadata) {
        Wf.Log.infoMove(this.getClass(), "`virtual` configured to true");
        final Register register = Register.instance(params);
        return register.updateAsync(params, metadata);
    }

    @Override
    public Future<JsonObject> saveAsync(final JsonObject params, final MetaInstance metadata) {
        Wf.Log.infoMove(this.getClass(), "`virtual` configured to true");
        final Register register = Register.instance(params);
        return register.saveAsync(params, metadata);
    }
}
