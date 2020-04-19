package io.vertx.up.uca.rs.mime.parse;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.atom.Epsilon;
import io.vertx.up.eon.em.MimeFlow;
import io.vertx.up.exception.WebException;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.rs.mime.Resolver;
import io.vertx.up.uca.rs.mime.Solve;
import io.vertx.up.uca.rs.mime.resolver.SolveResolver;
import io.vertx.up.uca.rs.mime.resolver.UnsetResolver;
import io.vertx.up.uca.yaml.Node;
import io.vertx.up.util.Ut;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;

/**
 * # 「Co」Zero Mime Processing here
 *
 * This component will process the request data before your code occurs
 *
 * @param <T> generic definition
 */
public class MimeAtomic<T> implements Atomic<T> {

    private static final Node<JsonObject> NODE = Node.infix("resolver");
    private static final Annal LOGGER = Annal.get(MimeAtomic.class);

    @Override
    public Epsilon<T> ingest(final RoutingContext context,
                             final Epsilon<T> income)
            throws WebException {
        final Epsilon<T> epsilon;
        if (MimeFlow.TYPED == income.getMime()) {
            /* Resolver **/
            final Atomic<T> atomic = Ut.singleton(TypedAtomic.class);
            epsilon = atomic.ingest(context, income);
        } else if (MimeFlow.STANDARD == income.getMime()) {
            /* System standard filler **/
            final Atomic<T> atomic = Ut.singleton(StandardAtomic.class);
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
        final Class<?> resolverCls = Ut.invoke(annotation, "resolver");
        final String header = context.request().getHeader(HttpHeaders.CONTENT_TYPE);
        /* 2.Check configured in default **/
        if (UnsetResolver.class == resolverCls) {
            /* 3. Old path **/
            final JsonObject content = NODE.read();
            // LOGGER.info("[ RESOLVER ] Resolvers = {0}", content.encodePrettily());
            final String resolver;
            if (null == header) {
                resolver = content.getString("default");
            } else {
                final MediaType type = MediaType.valueOf(header);
                final JsonObject resolverMap = content.getJsonObject(type.getType());
                resolver = resolverMap.getString(type.getSubtype());
            }
            LOGGER.info(Info.RESOLVER, resolver, header, context.request().absoluteURI());
            return Ut.singleton(resolver);
        } else {
            LOGGER.info(Info.RESOLVER_CONFIG, resolverCls, header);
            /*
             * Split workflow
             * Resolver or Solve
             */
            if (Ut.isImplement(resolverCls, Resolver.class)) {
                /*
                 * Resolver Directly
                 */
                return Ut.singleton(resolverCls);
            } else {
                /*
                 * Solve component, contract to set Solve<T> here.
                 */
                final Resolver<T> resolver = Ut.singleton(SolveResolver.class);
                Ut.contract(resolver, Solve.class, Ut.singleton(resolverCls));
                return resolver;
            }
        }
    }
}
