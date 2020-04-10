package io.vertx.tp.atom.modeling.rule;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import io.vertx.core.json.JsonArray;
import io.vertx.up.util.Ut;

import java.io.IOException;

public class RuleTermDeserializer extends JsonDeserializer<RuleTerm> {
    @Override
    public RuleTerm deserialize(final JsonParser parser,
                                final DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        final JsonNode node = parser.getCodec().readTree(parser);
        final String literal = node.toString();
        if (Ut.isJArray(literal)) {
            final JsonArray array = new JsonArray(literal);
            return new RuleTerm(array);
        } else {
            final String stringValue = node.asText();
            return new RuleTerm(stringValue);
        }
    }
}
