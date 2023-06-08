package io.vertx.mod.ambient.uca.digital;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Tree extends Aide {
    /*
     *  (N) FIELD = ? AND TYPE = ? AND LEAF = ? (Yes/No)
     */
    Future<JsonArray> fetch(String field, String type, Boolean leaf);
}
