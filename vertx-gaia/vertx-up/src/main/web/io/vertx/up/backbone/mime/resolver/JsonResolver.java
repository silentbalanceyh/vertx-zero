package io.vertx.up.backbone.mime.resolver;

import io.horizon.uca.log.Annal;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.backbone.mime.Resolver;
import io.vertx.up.runtime.ZeroSerializer;
import io.vertx.up.util.Ut;

/**
 * Json Resolver
 *
 * @param <T>
 */
@SuppressWarnings("unchecked")
public class JsonResolver<T> implements Resolver<T> {

    private static final Annal LOGGER = Annal.get(JsonResolver.class);

    @Override
    public Epsilon<T> resolve(final RoutingContext context,
                              final Epsilon<T> income) {
        // Json Resolver
        final String content = context.body().asString();
        LOGGER.info(Ut.isNotNil(content), "( Resolver ) KIncome Type: {0}, Content = \u001b[0;37m{1}\u001b[m",
            income.getArgType().getName(), content);
        if (Ut.isNil(content)) {
            // Default Value set for BodyParam
            final T defaultValue = (T) income.getDefaultValue();
            income.setValue(defaultValue);
        } else {
            final Object result = ZeroSerializer.getValue(income.getArgType(), content);
            if (null != result) {
                income.setValue((T) result);
            }
        }
        return income;
    }
}
