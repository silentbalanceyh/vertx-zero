package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.experiment.specification.KJoin;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class RWholePre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxMod in) {
        /*
         * For `/api/{actor}/by/sigma`
         * Only support extract the data that active = true
         */
        data.put(Strings.EMPTY, Boolean.TRUE);      // AND
        data.put(KName.ACTIVE, Boolean.TRUE);
        if (in.canJoin()) {
            /*
             * Can join for `JOIN`
             */
            final KJoin join = in.module().getConnect();
            final String targetIndent = join.getTargetIndent();
            final KModule connect = in.connect();
            data.put(targetIndent, connect.getIdentifier());
        }
        return Ux.future(data);
    }
}
