package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import io.vertx.core.json.JsonArray;

import java.io.IOException;

/**
 * # 「Tp」Jackson Serializer
 *
 * Came from `vert.x` internally to support `io.vertx.core.json.JsonArray` serialization, ignored.
 *
 * @author lang
 */
public class JsonArraySerializer extends JsonSerializer<JsonArray> {
    @Override
    public void serialize(final JsonArray value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        jgen.writeObject(value.getList());
    }
}
