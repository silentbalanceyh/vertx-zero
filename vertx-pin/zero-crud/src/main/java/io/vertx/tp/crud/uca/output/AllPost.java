package io.vertx.tp.crud.uca.output;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AllPost implements Post<JsonArray> {
    @Override
    public Future<JsonArray> outAsync(final Object active, final Object standBy) {
        System.out.println(active);
        System.out.println(standBy);
        return Ux.futureA();
    }
}
