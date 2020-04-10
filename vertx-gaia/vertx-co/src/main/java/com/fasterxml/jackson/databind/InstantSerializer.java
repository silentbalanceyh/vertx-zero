package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.time.Instant;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

public class InstantSerializer extends JsonSerializer<Instant> {
    @Override
    public void serialize(final Instant value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        jgen.writeString(ISO_INSTANT.format(value));
    }
}
