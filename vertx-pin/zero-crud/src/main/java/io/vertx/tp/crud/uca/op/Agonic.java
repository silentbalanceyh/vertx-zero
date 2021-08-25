package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.uca.desk.IxIn;

/**
 * I -> I
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Agonic<T> {

    Future<T> runAsync(JsonObject T, IxIn module);
}
