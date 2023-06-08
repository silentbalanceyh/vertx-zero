package io.modello.specification.uca;

import io.horizon.atom.common.Kv;
import io.modello.specification.HRecord;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface IComponent extends IoSource {

    Object before(Kv<String, Object> kv, HRecord record, JsonObject combineData);
}
