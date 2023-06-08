package io.vertx.up.backbone.mime.resolver;

import io.horizon.exception.web._500InternalServerException;
import io.horizon.uca.log.Annal;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.annotations.Contract;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.backbone.mime.Resolver;
import io.vertx.up.backbone.mime.Solve;

import java.util.Objects;

public class SolveResolver<T> implements Resolver<T> {

    private static final Annal LOGGER = Annal.get(SolveResolver.class);
    @Contract
    private transient Solve<T> internalResolver;

    @Override
    public Epsilon<T> resolve(final RoutingContext context, final Epsilon<T> income) {
        if (Objects.isNull(this.internalResolver)) {
            throw new _500InternalServerException(this.getClass(), "Solve instance is null");
        } else {
            // Default content from `context`
            final String content = context.body().asString();
            LOGGER.info("( Resolver ) Solve Type: {0}, Content = {1}",
                this.internalResolver.getClass(), content);
            final T processed = this.internalResolver.resolve(content);
            income.setValue(processed);
        }
        return income;
    }
}
