package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.up.unity.Ux;

/**
 * Support Variable
 *
 * 1. module
 * 2. view
 * 3. location
 * 4. sigma
 * 5. language
 * 6. appId
 *
 * Other variable should use fixed value instead of expression
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class InitialPre implements Pre {
    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        return Ux.future(data);
    }
}
