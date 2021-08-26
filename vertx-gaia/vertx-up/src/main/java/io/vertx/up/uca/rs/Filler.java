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
 * 「Co」Zero for JSR311 Arguments
 *
 * There are a uniform request flow to get parameters to support JSR311 in zero framework, here I defined **Filler**
 * interface ( Fill argument into container, it's specific component )
 *
 * 1. In common request workflow, it provide standard `key = value` attributes into map.
 * 2. For body/stream request workflow, there should be placeholder named `EmptyFiller` to taken the flow node and then continue for extracting.
 *
 * For Standard JSR311, it support most parameter annotations and extend JSR311 for business requirement.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Filler {
    /**
     * JSR311 Standard mapping from annotation to `Filler` component
     */
    ConcurrentMap<Class<? extends Annotation>, Filler> PARAMS =
        new ConcurrentHashMap<Class<? extends Annotation>, Filler>() {
            {
                // JSR311 Provided
                this.put(QueryParam.class, Ut.singleton(QueryFiller.class));
                this.put(FormParam.class, Ut.singleton(FormFiller.class));
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
    /**
     * The type of parameters that will use `EmptyFiller` for taking place.
     */
    Set<Class<? extends Annotation>> NO_VALUE =
        new HashSet<Class<? extends Annotation>>() {
            {
                this.add(BodyParam.class);
                this.add(StreamParam.class);
            }
        };

    /**
     * The major code logic to get the value of input field name here.
     *
     * @param name      The parameter name
     * @param paramType The parameter declared type
     * @param datum     The `RoutingContext` of Vert.x ( vertx-web )
     *
     * @return The extracted value of parameter
     */
    Object apply(String name,
                 Class<?> paramType,
                 RoutingContext datum);
}
