package io.aeon.experiment.rule;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RuleTermSerializer extends JsonSerializer<RuleTerm> {

    @Override
    public void serialize(final RuleTerm ruleTerm,
                          final JsonGenerator jgen,
                          final SerializerProvider serializerProvider) throws IOException {
        if (Objects.nonNull(ruleTerm)) {
            final Set<String> terms = ruleTerm.getFields();
            if (terms.isEmpty()) {
                jgen.writeObject(new ArrayList<>());
            } else {
                if (1 == terms.size()) {
                    final String literal = terms.iterator().next();
                    jgen.writeString(literal);
                } else {
                    final List<String> list = new ArrayList<>(terms);
                    jgen.writeObject(list);
                }
            }
        } else {
            jgen.writeObject(new ArrayList<>());
        }
    }
}
