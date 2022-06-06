package io.vertx.tp.ke.aop;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.uca.sectio.After;
import io.vertx.up.unity.Ux;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AfterUserSync implements After {
    @Override
    public Set<ChangeFlag> types() {
        return new HashSet<>() {
            {
                this.add(ChangeFlag.ADD);
                this.add(ChangeFlag.UPDATE);
            }
        };
    }

    @Override
    public Future<JsonArray> afterAsync(final JsonArray data, final JsonObject config) {

        return Ux.future(data);
    }
}
