package io.vertx.up.backbone.argument;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.backbone.Filler;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroSerializer;

import java.util.Map;

public class ContextFiller implements Filler {
    @Override
    public Object apply(final String name,
                        final Class<?> paramType,
                        final RoutingContext context) {
        final Map<String, Object> data = context.data();
        final Object value = data.get(name);
        return Fn.runOr(() -> {
            if (paramType == value.getClass()) {
                return value;
            } else {
                final String valueStr = value.toString();
                return ZeroSerializer.getValue(paramType, valueStr);
            }
        }, value);
    }
}
