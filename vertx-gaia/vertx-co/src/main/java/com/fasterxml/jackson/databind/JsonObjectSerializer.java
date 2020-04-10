package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

public class JsonObjectSerializer extends JsonSerializer<JsonObject> {
    @Override
    public void serialize(final JsonObject value,
                          final JsonGenerator jgen,
                          final SerializerProvider provider) throws IOException {
        jgen.writeObject(value.getMap());
    }
}