package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import io.vertx.up.atom.unity.Uson;

import java.io.IOException;

/**
 * @author Lang
 */
public class BladeSerializer extends JsonSerializer<Uson> {
    @Override
    public void serialize(final Uson blade,
                          final JsonGenerator jgen,
                          final SerializerProvider provider) throws IOException {
        jgen.writeObject(blade.to().getMap());
    }
}
