package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.horizon.util.HUt;
import io.modello.atom.normalize.KRuleTerm;
import io.vertx.core.json.JsonArray;

import java.io.IOException;

public class RuleTermDeserializer extends JsonDeserializer<KRuleTerm> {
    @Override
    public KRuleTerm deserialize(final JsonParser parser,
                                 final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonNode node = parser.getCodec().readTree(parser);
        final String literal = node.toString();
        if (HUt.isJArray(literal)) {
            final JsonArray array = new JsonArray(literal);
            return new KRuleTerm(array);
        } else {
            final String stringValue = node.asText();
            return new KRuleTerm(stringValue);
        }
    }
}
