package io.vertx.up.backbone.mime.parse;

import io.horizon.exception.WebException;
import io.horizon.uca.cache.Cc;
import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.backbone.mime.Resolver;
import io.vertx.up.backbone.mime.Solve;
import io.vertx.up.backbone.mime.resolver.SolveResolver;
import io.vertx.up.backbone.mime.resolver.UnsetResolver;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.eon.em.EmMime;
import io.vertx.up.runtime.ZeroStore;
import io.vertx.up.util.Ut;
import jakarta.ws.rs.core.HttpHeaders;
import jakarta.ws.rs.core.MediaType;

import java.lang.annotation.Annotation;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * # 「Co」Zero Mime Processing here
 *
 * This component will process the request data before your code occurs
 *
 * @param <T> generic definition
 */
@SuppressWarnings("all")
public class MimeAtomic<T> implements Atomic<T> {
    private static final Annal LOGGER = Annal.get(MimeAtomic.class);

    private static final ConcurrentMap<String, Atomic> POOL_ATOMIC = new ConcurrentHashMap<>();
    private static final Cc<String, Atomic> CC_ATOMIC = Cc.openThread();
    private static final Cc<String, Resolver> CC_RESOLVER = Cc.openThread();
    private static final Cc<String, Solve> CC_SOLVE = Cc.openThread();

    @Override
    public Epsilon<T> ingest(final RoutingContext context,
                             final Epsilon<T> income)
        throws WebException {
        final Epsilon<T> epsilon;
        if (EmMime.Flow.TYPED == income.getMime()) {
            /* Resolver **/
            final Atomic<T> atomic = CC_ATOMIC.pick(TypedAtomic::new, TypedAtomic.class.getName());
            // Fn.po?lThread(POOL_ATOMIC, TypedAtomic::new, TypedAtomic.class.getName());
            epsilon = atomic.ingest(context, income);
        } else if (EmMime.Flow.STANDARD == income.getMime()) {
            /* System standard filler **/
            final Atomic<T> atomic = CC_ATOMIC.pick(StandardAtomic::new, StandardAtomic.class.getName());
            // Fn.po?lThread(POOL_ATOMIC, StandardAtomic::new, StandardAtomic.class.getName());
            epsilon = atomic.ingest(context, income);
        } else {
            /* Resolver **/
            final Resolver<T> resolver = this.getResolver(context, income);
            epsilon = resolver.resolve(context, income);
        }
        return epsilon;
    }

    private Resolver<T> getResolver(final RoutingContext context,
                                    final Epsilon<T> income) {
        /* 1.Read the resolver first **/
        final Annotation annotation = income.getAnnotation();
        final Class<?> resolverCls = Ut.invoke(annotation, YmlCore.resolver.__KEY);
        final String header = context.request().getHeader(HttpHeaders.CONTENT_TYPE);
        /* 2.Check configured in default **/
        if (UnsetResolver.class == resolverCls) {
            /* 3. Old path **/
            final JsonObject content = ZeroStore.option(YmlCore.resolver.__KEY);
            final String resolver;
            if (null == header) {
                resolver = content.getString("default");
                LOGGER.info(INFO.RESOLVER_DEFAULT, resolver, context.request().absoluteURI());
            } else {
                final MediaType type = MediaType.valueOf(header);
                final JsonObject resolverMap = content.getJsonObject(type.getType());
                resolver = resolverMap.getString(type.getSubtype());
                LOGGER.info(INFO.RESOLVER, resolver, header, context.request().absoluteURI());
            }
            return CC_RESOLVER.pick(() -> Ut.instance(resolver), resolver);
            // Fn.po?lThread(POOL_RESOLVER, () -> Ut.instance(resolver), resolver);
        } else {
            LOGGER.info(INFO.RESOLVER_CONFIG, resolverCls, header);
            /*
             * Split workflow
             * Resolver or Solve
             */
            if (Ut.isImplement(resolverCls, Resolver.class)) {
                /*
                 * Resolver Directly
                 */
                return CC_RESOLVER.pick(() -> Ut.instance(resolverCls), resolverCls.getName());
                // Fn.po?lThread(POOL_RESOLVER, () -> Ut.instance(resolverCls), resolverCls.getName());
            } else {
                /*
                 * Solve component, contract to set Solve<T> here.
                 */
                final Resolver<T> resolver = CC_RESOLVER.pick(() -> Ut.instance(SolveResolver.class), SolveResolver.class.getName());
                // Fn.po?lThread(POOL_RESOLVER, () -> Ut.instance(SolveResolver.class), SolveResolver.class.getName());
                final Solve solve = CC_SOLVE.pick(() -> Ut.instance(resolverCls), resolverCls.getName());
                // Fn.po?lThread(POOL_SOLVE, () -> Ut.instance(resolverCls), resolverCls.getName());
                Ut.contract(resolver, Solve.class, solve);
                return resolver;
            }
        }
    }
}
