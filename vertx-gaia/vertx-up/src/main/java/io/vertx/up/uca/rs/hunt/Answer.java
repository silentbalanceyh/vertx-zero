package io.vertx.up.uca.rs.hunt;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.http.HttpStatusCode;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.up.annotations.SessionData;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.ID;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.extension.pointer.PluginExtension;
import io.vertx.up.util.Ut;

import javax.ws.rs.core.MediaType;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Response process to normalize the response request.
 * 1. Media definition
 * 2. Operation based on event, envelop, context
 */
public final class Answer {

    public static Envelop previous(final RoutingContext context) {
        Envelop envelop = context.get(ID.REQUEST_BODY);
        if (Objects.isNull(envelop)) {
            envelop = Envelop.failure(new _500InternalServerException(Answer.class, "Previous Error of " + ID.REQUEST_BODY));
        }
        return envelop;
    }

    public static void next(final RoutingContext context, final Envelop envelop) {
        if (envelop.valid()) {
            /*
             * Next step here
             */
            context.put(ID.REQUEST_BODY, envelop);
            context.next();
        } else {
            reply(context, envelop);
        }
    }

    public static void normalize(final RoutingContext context, final Envelop envelop) {
        if (envelop.valid()) {
            /*
             * Updated here
             */
            envelop.bind(context);
            context.put(ID.REQUEST_BODY, envelop);
            context.next();
        } else {
            reply(context, envelop);
        }
    }

    public static void reply(final RoutingContext context, final Envelop envelop) {
        reply(context, envelop, new HashSet<>());
    }

    public static void reply(final RoutingContext context, final Envelop envelop, final Supplier<Set<MediaType>> supplier) {
        Set<MediaType> produces = Objects.isNull(supplier) ? new HashSet<>() : supplier.get();
        if (Objects.isNull(produces)) {
            produces = new HashSet<>();
        }
        reply(context, envelop, produces);
    }

    public static void reply(final RoutingContext context, final Envelop envelop, final Event event) {
        Set<MediaType> produces;
        if (Objects.isNull(event)) {
            produces = new HashSet<>();
        } else {
            produces = event.getProduces();
            if (Objects.isNull(produces)) {
                produces = new HashSet<>();
            }
        }
        reply(context, envelop, produces, Objects.isNull(event) ? null : event.getAction());
    }

    private static void reply(final RoutingContext context, final Envelop envelop, final Set<MediaType> mediaTypes) {
        reply(context, envelop, mediaTypes, null);
    }

    private static void reply(final RoutingContext context, final Envelop envelop,
                              final Set<MediaType> mediaTypes, final Method sessionAction) {
        /*
         * New workflow here, firstly checking response after get response reference.
         */
        final HttpServerResponse response = context.response();
        /*
         * FIX: java.lang.IllegalStateException: Response is closed
         */
        if (!response.closed()) {
            /*
             * Set http status information on response, all information came from `Envelop`
             * 1) Status Code
             * 2) Status Message
             */
            final HttpStatusCode code = envelop.status();
            response.setStatusCode(code.code());
            response.setStatusMessage(code.message());
            /*
             * Bind Data
             */
            envelop.bind(context);

            /*
             * Media processing
             */
            Outcome.media(response, mediaTypes);

            /*
             * Security for response
             */
            Outcome.security(response);

            /*
             * SessionData Stored
             */
            storeSession(context, envelop.data(), sessionAction);
            /*
             * Plugin Extension for response replying here ( Plug-in )
             */
            PluginExtension.Answer.reply(context, envelop);
            /*
             * Output of current situation
             */
            Outcome.out(response, envelop, mediaTypes);
        }
    }

    private static <T> void storeSession(final RoutingContext context,
                                         final T data,
                                         final Method method) {
        final Session session = context.session();
        if (null != session && null != data) {
            if (Objects.nonNull(method) && method.isAnnotationPresent(SessionData.class)) {
                final Annotation annotation = method.getAnnotation(SessionData.class);
                final String key = Ut.invoke(annotation, "value");
                final String field = Ut.invoke(annotation, "field");
                // Data Storage
                Object reference = data;
                if (Ut.isJObject(data) && Ut.notNil(field)) {
                    final JsonObject target = (JsonObject) data;
                    reference = target.getValue(field);
                }
                // Session Put / Include Session ID
                session.put(key, reference);
            }
        }
    }
}
