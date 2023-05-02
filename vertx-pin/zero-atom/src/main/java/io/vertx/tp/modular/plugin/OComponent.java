package io.vertx.tp.modular.plugin;

import io.horizon.atom.common.Kv;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface OComponent extends IoSource {

    Object after(Kv<String, Object> kv, HRecord record, JsonObject combineData);
}
