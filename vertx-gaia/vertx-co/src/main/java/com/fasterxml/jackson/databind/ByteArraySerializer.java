package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.Base64;

public class ByteArraySerializer extends JsonSerializer<byte[]> {
    private final Base64.Encoder BASE64 = Base64.getEncoder();

    @Override
    public void serialize(final byte[] value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        jgen.writeString(this.BASE64.encodeToString(value));
    }
}
