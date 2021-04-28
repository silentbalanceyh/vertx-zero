package io.vertx.tp.modular.plugin;

import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Kv;
import io.vertx.up.commune.Record;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface IComponent extends IoSource {

    Object before(Kv<String, Object> kv, Record record, JsonObject combineData);
}
