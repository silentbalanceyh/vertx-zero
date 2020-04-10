package io.vertx.up.uca.rs;

import io.vertx.ext.web.RoutingContext;
import io.vertx.up.uca.rs.argument.*;
import io.vertx.up.util.Ut;

import javax.ws.rs.*;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Fill the arguments into reference list
 * as arguments
 */
public interface Filler {
    ConcurrentMap<Class<? extends Annotation>, Filler> PARAMS =
            new ConcurrentHashMap<Class<? extends Annotation>, Filler>() {
                {
                    // JSR311 Provided
                    this.put(QueryParam.class, Ut.singleton(QueryFiller.class));
                    this.put(FormParam.class, Ut.singleton(FormFiller.class));
                    this.put(MatrixParam.class, Ut.singleton(QueryFiller.class));
                    this.put(PathParam.class, Ut.singleton(PathFiller.class));
                    this.put(HeaderParam.class, Ut.singleton(HeaderFiller.class));
                    this.put(CookieParam.class, Ut.singleton(CookieFiller.class));
                    // Extension
                    this.put(BodyParam.class, Ut.singleton(EmptyFiller.class));
                    this.put(StreamParam.class, Ut.singleton(EmptyFiller.class));
                    this.put(SessionParam.class, Ut.singleton(SessionFiller.class));
                    this.put(ContextParam.class, Ut.singleton(ContextFiller.class));
                }
            };
    Set<Class<? extends Annotation>> NO_VALUE =
            new HashSet<Class<? extends Annotation>>() {
                {
                    this.add(BodyParam.class);
                    this.add(StreamParam.class);
                }
            };

    /**
     * @param name
     * @param paramType
     * @param datum
     * @return
     */
    Object apply(String name,
                 Class<?> paramType,
                 RoutingContext datum);
}
