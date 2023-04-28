package io.vertx.tp.modular.plugin;

import io.horizon.atom.Kv;
import io.horizon.specification.modeler.HRecord;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface OComponent extends IoSource {

    Object after(Kv<String, Object> kv, HRecord record, JsonObject combineData);
}
