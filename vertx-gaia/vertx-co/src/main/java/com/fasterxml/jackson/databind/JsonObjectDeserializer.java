package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

public class JsonObjectDeserializer extends JsonDeserializer<JsonObject> {

    @Override
    public JsonObject deserialize(final JsonParser parser,
                                  final DeserializationContext context)
            throws IOException {
        final JsonNode node = parser.getCodec().readTree(parser);
        return new JsonObject(node.toString());
    }
}
