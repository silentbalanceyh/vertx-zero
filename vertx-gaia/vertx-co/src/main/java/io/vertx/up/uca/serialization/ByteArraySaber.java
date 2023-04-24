package io.vertx.up.uca.serialization;

import io.vertx.up.fn.Fn;
import io.zero.cv.VValue;

public class ByteArraySaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.orNull(() -> Fn.orSemi(Byte[].class == paramType ||
                    byte[].class == paramType, this.getLogger(),
                () -> literal.getBytes(VValue.DFT.CHARSET), () -> new byte[0]),
            paramType, literal);
    }
}
