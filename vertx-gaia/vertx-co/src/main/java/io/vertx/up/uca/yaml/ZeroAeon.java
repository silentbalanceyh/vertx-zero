package io.vertx.up.uca.yaml;

import io.vertx.aeon.eon.HName;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ZeroAeon implements Node<JsonObject> {

    @Override
    public JsonObject read() {
        /*
         * aeon:
         *   kinect:
         */
        final JsonObject config = ZeroTool.read(null, true);
        return Ut.valueJObject(config, HName.AEON);
    }
}
