package io.vertx.up.uca.serialization;

import io.vertx.core.buffer.Buffer;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;

/**
 * Buffer
 */
public class BufferSaber extends BaseSaber {
    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.getNull(() ->
                        Fn.getSemi(Buffer.class == paramType, getLogger(),
                                () -> {
                                    final Buffer buffer = Buffer.buffer();
                                    buffer.appendBytes(literal.getBytes(Values.DEFAULT_CHARSET));
                                    return buffer;
                                }, Buffer::buffer),
                paramType, literal);
    }
}
