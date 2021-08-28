package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class UserPre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxIn in) {
        final Envelop envelop = in.envelop();
        data.put(KName.USER, Ke.keyUser(envelop));
        data.put(KName.HABITUS, Ke.keyHabitus(envelop));
        return Ux.future(data);
    }
}
