package io.modello.dynamic.modular.jooq.convert;

import io.vertx.core.json.JsonArray;
import io.vertx.up.util.Ut;
import org.jooq.Converter;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JsonArraySider implements Converter<String, JsonArray> {
    @Override
    public JsonArray from(final String s) {
        return Ut.toJArray(s);
    }

    @Override
    public String to(final JsonArray objects) {
        final JsonArray safe = Ut.valueJArray(objects);
        return safe.encode();
    }

    @Override
    public Class<String> fromType() {
        return String.class;
    }

    @Override
    public Class<JsonArray> toType() {
        return JsonArray.class;
    }
}
