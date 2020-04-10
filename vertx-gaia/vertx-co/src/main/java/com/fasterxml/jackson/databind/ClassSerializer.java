package com.fasterxml.jackson.databind;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.io.IOException;

/**
 * @author Lang
 */
public class ClassSerializer extends JsonSerializer<Class<?>> { // NOPMD


    /**
     *
     */
    @Override
    public void serialize(final Class<?> clazz, final JsonGenerator generator,
                          final SerializerProvider provider)
            throws IOException {
        generator.writeString(clazz.getName());
    }


}
