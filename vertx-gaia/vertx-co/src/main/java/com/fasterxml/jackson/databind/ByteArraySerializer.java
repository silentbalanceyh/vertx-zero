package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;
import java.util.Base64;

/**
 * # 「Tp」Jackson Serializer
 *
 * This serializer came from `vert.x` internally. In vert.x all the data `byte[]` will be converted to
 * `Base 64 format` to stored into `io.vertx.core.json.JsonArray/io.vertx.core.json.JsonObject` for usage.
 * ZeroModule is new module that will be registry on zero framework, In this situation it must support default
 * features in `vert.x` first.
 *
 * 1. This module should support most of features that in `vert.x` internally`.
 * 2. This module must extend the default jackson serialization sub-system here.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ByteArraySerializer extends JsonSerializer<byte[]> {
    private final Base64.Encoder BASE64 = Base64.getEncoder();

    @Override
    public void serialize(final byte[] value, final JsonGenerator jgen, final SerializerProvider provider) throws IOException {
        jgen.writeString(this.BASE64.encodeToString(value));
    }
}
