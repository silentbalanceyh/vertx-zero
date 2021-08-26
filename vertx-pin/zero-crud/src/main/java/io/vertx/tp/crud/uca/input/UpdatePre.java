package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.KField;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class UpdatePre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxIn in) {
        /* UserId */
        this.auditor(data, in);
        return Ux.future(data);
    }

    private void auditor(final JsonObject data, final IxIn in) {
        /* UserId */
        final User user = in.user();
        final KModule module = in.module();
        if (Objects.nonNull(user)) {
            final String userId = Ke.keyUser(user);
            if (Ut.notNil(userId)) {
                final KField field = module.getField();
                /* Created */
                Ix.onAuditor(data, field.getUpdated(), userId);
            }
        }
    }

    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxIn in) {
        Ut.itJArray(data).forEach(json -> this.auditor(json, in));
        return Ux.future(data);
    }
}
