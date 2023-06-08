package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import io.modello.atom.normalize.KRuleTerm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class RuleTermSerializer extends JsonSerializer<KRuleTerm> {

    @Override
    public void serialize(final KRuleTerm ruleTerm,
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
