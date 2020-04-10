package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import io.vertx.up.util.Ut;

import java.io.IOException;

/**
 * @author Lang
 */
public class ClassDeserializer extends JsonDeserializer<Class<?>> { // NOPMD
    /**
     *
     */
    @Override
    public Class<?> deserialize(final JsonParser parser,
                                final DeserializationContext context)
            throws IOException {
        final JsonNode node = parser.getCodec().readTree(parser);
        return Ut.clazz(node.asText().trim(), null);
    }
}
