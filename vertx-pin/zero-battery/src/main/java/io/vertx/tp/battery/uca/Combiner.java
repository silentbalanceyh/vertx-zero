package io.vertx.tp.battery.uca;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Combiner<I, M> {

    Future<JsonObject> configure(I bag, M map);
}
