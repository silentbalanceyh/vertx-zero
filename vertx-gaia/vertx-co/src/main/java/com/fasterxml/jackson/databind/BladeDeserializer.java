package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.unity.Uson;

import java.io.IOException;

/**
 * @author Lang
 */
public class BladeDeserializer extends JsonDeserializer<Uson> { // NOPMD
    /**
     *
     */
    @Override
    public Uson deserialize(final JsonParser parser,
                            final DeserializationContext context)
            throws IOException {
        final JsonNode node = parser.getCodec().readTree(parser);
        return Uson.create(new JsonObject(node.toString()));
    }
}
