package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;

import java.io.IOException;

/**
 * # 「Tp」 Jackson Deserializer
 *
 * Zero designed the cache pool for class that will be stored into `ConcurrentMap` ( Default implementation ),
 * * It means that this class could be loaded once and usage multi-times. Here I provide default serializer to convert
 * * `java.lang.Class<?>` to `java.lang.String` to simplify the clazz look-up in container.
 *
 * This serializer is reverted component to {@link com.fasterxml.jackson.databind.ClassDeserializer}.
 *
 * @author Lang
 */
public class ClassSerializer extends JsonSerializer<Class<?>> { // NOPMD

    @Override
    public void serialize(final Class<?> clazz, final JsonGenerator generator,
                          final SerializerProvider provider)
            throws IOException {
        generator.writeString(clazz.getName());
    }
}
