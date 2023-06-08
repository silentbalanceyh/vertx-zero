package io.vertx.mod.workflow.uca.modeling;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.atom.configuration.MetaInstance;
import io.vertx.up.unity.Ux;

import static io.vertx.mod.workflow.refine.Wf.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RegisterV extends AbstractRegister {
    @Override
    public Future<JsonObject> insertAsync(final JsonObject params, final MetaInstance metadata) {
        LOG.Move.info(this.getClass(), "`virtual` configured to true");
        return Ux.future(params);
    }

    @Override
    public Future<JsonObject> updateAsync(final JsonObject params, final MetaInstance metadata) {
        LOG.Move.info(this.getClass(), "`virtual` configured to true");
        final Register register = Register.instance(params);
        return register.updateAsync(params, metadata);
    }

    @Override
    public Future<JsonObject> saveAsync(final JsonObject params, final MetaInstance metadata) {
        LOG.Move.info(this.getClass(), "`virtual` configured to true");
        final Register register = Register.instance(params);
        return register.saveAsync(params, metadata);
    }
}
