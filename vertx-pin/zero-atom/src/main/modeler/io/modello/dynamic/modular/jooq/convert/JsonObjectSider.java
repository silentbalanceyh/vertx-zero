package io.modello.dynamic.modular.jooq.convert;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;
import org.jooq.Converter;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JsonObjectSider implements Converter<String, JsonObject> {
    @Override
    public JsonObject from(final String s) {
        return Ut.toJObject(s);
    }

    @Override
    public String to(final JsonObject objects) {
        final JsonObject safe = Ut.valueJObject(objects);
        return safe.encode();
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<JsonObject> toType() {
        return JsonObject.class;
    }
}
