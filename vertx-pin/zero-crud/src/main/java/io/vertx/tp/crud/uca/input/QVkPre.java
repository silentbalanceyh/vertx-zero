package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.cv.IxMsg;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QVkPre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxIn in) {
        final String sessionKey = Ke.keySession(
            data.getString(KName.METHOD),
            data.getString(KName.URI)
        );
        Ix.Log.dao(this.getClass(), IxMsg.CACHE_KEY_PROJECTION, sessionKey);
        data.put(KName.DATA_KEY, sessionKey);
        return Ux.future(data);
    }
}
