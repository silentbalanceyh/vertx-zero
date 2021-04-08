package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import io.vertx.core.json.JsonObject;

import java.io.IOException;

/**
 * # 「Tp」Jackson Deserializer
 *
 * Came from `vert.x` internally to support `io.vertx.core.json.JsonObject` deserialization, ignored.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JsonObjectDeserializer extends JsonDeserializer<JsonObject> {

    @Override
    public JsonObject deserialize(final JsonParser parser,
                                  final DeserializationContext context)
            throws IOException {
        final JsonNode node = parser.getCodec().readTree(parser);
        return new JsonObject(node.toString());
    }
}
