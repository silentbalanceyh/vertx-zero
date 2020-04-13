package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.time.Instant;

import static java.time.format.DateTimeFormatter.ISO_INSTANT;

/**
 * # 「Tp」Jackson Serializer
 *
 * This serializer came from `vert.x` internally. In vert.x framework the datetime object will be converted to
 * `java.time.Instant` as uniform, in this situation it could provide developers to simplify `Date Format` processing
 * in json specification.
 *
 * @author lang
 */
public class InstantSerializer extends JsonSerializer<Instant> {
    @Override
    public void serialize(final Instant value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        jgen.writeString(ISO_INSTANT.format(value));
    }
}
