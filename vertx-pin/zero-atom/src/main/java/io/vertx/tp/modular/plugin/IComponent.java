package io.vertx.tp.modular.plugin;

import io.horizon.specification.modeler.HRecord;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.Kv;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface IComponent extends IoSource {

    Object before(Kv<String, Object> kv, HRecord record, JsonObject combineData);
}
