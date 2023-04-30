package io.vertx.up.uca.serialization;

import io.horizon.uca.log.Annal;
import io.vertx.up.exception.web._400ParameterFromStringException;
import io.vertx.up.fn.Fn;

public abstract class BaseSaber implements Saber {

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }

    void verifyInput(final boolean condition,
                     final Class<?> paramType,
                     final String literal) {
        Fn.outWeb(condition,
            this.getLogger(), _400ParameterFromStringException.class,
            this.getClass(), paramType, literal);
    }

    @Override
    public <T> Object from(final T input) {
        // Default direct
        return input;
    }

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        // Default direct
        return literal;
    }
}
