package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

/**
 * # 「Tp」Jackson Serializer
 *
 * Came from `vert.x` internally to support `io.vertx.core.json.JsonObject` serialization, ignored.
 *
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class JsonObjectSerializer extends JsonSerializer<JsonObject> {
    @Override
    public void serialize(final JsonObject value,
                          final JsonGenerator jgen,
                          final SerializerProvider provider) throws IOException {
        jgen.writeObject(value.getMap());
    }
}