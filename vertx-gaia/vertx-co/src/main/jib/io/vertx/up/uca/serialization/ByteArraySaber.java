package io.vertx.up.uca.serialization;

import io.horizon.eon.VValue;
import io.vertx.up.fn.Fn;

public class ByteArraySaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() -> Fn.runOr(Byte[].class == paramType ||
                    byte[].class == paramType, this.getLogger(),
                () -> literal.getBytes(VValue.DFT.CHARSET), () -> new byte[0]),
            paramType, literal);
    }
}
