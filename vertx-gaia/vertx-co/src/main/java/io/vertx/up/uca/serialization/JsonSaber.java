package io.vertx.up.uca.serialization;

import io.vertx.core.json.DecodeException;
import io.vertx.up.exception.web._400ParameterFromStringException;
import io.vertx.up.fn.Fn;

import java.util.function.Function;

/**
 * Json
 */
public abstract class JsonSaber extends BaseSaber {

    @Override
    public Object from(final Class<?> paramType,
                       final String literal) {
        return Fn.runOr(() -> Fn.runOr(this.isValid(paramType), this.getLogger(),
                () -> {
                    try {
                        return this.getFun().apply(literal);
                    } catch (final DecodeException ex) {
                        // Do not do anything
                        // getLogger().jvm(ex);
                        throw new _400ParameterFromStringException(this.getClass(), paramType, literal);
                    }
                }, () -> null),
            paramType, literal);
    }

    protected abstract boolean isValid(final Class<?> paramType);

    protected abstract <T> Function<String, T> getFun();
}
