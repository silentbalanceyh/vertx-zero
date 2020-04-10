package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import io.vertx.core.json.JsonArray;

import java.io.IOException;

public class JsonArraySerializer extends JsonSerializer<JsonArray> {
    @Override
    public void serialize(final JsonArray value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        jgen.writeObject(value.getList());
    }
}
