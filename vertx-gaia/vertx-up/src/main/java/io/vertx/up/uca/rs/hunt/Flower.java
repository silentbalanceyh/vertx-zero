package io.vertx.up.uca.rs.hunt;

import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.annotations.Codex;
import io.vertx.up.atom.Kv;
import io.vertx.up.atom.Rule;
import io.vertx.up.atom.agent.Depot;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._411ContentLengthException;
import io.vertx.up.extension.pointer.PluginExtension;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.container.Virtual;
import io.vertx.up.uca.rs.announce.Rigor;
import io.vertx.up.uca.rs.validation.Validator;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;
import java.util.Map;

class Flower {

    private static final Annal LOGGER = Annal.get(Flower.class);

    static <T> Future<Envelop> next(final RoutingContext context,
                                    final T entity) {
        final Envelop envelop = Envelop
                .success(entity)
                .bind(context);     // Bind Data Here.
        /*
         * Extension System of:
         * 1) PlugAuditor
         * 2) PlugRegion
         */
        return PluginExtension.Flower.next(context, envelop);
    }

    static void replyError(final RoutingContext context,
                           final WebException error,
                           final Event event) {
        final Envelop envelop = Envelop.failure(error);
        Answer.reply(context, envelop, event);
    }

    static void executeRequest(final RoutingContext context,
                               final Map<String, List<Rule>> rulers,
                               final Depot depot,
                               final Object[] args,
                               final Validator verifier) {
        // Extract major object
        WebException error = verifyPureArguments(verifier, depot, args);
        if (null == error) {
            error = verifyUpload(context);
        }
        if (null == error) {
            // Check if annotated with @Codex
            final Kv<Integer, Class<?>> found = findParameter(depot.getEvent().getAction());
            if (null == found.getValue()) {
                // Verify here.
                context.next();
            } else {
                // @Codex validation for different types
                final Class<?> type = found.getValue();
                final Object value = args[found.getKey()];
                verifyCodex(context, rulers, depot, type, value);
            }
        } else {
            // Hibernate validate failure
            replyError(context, error, depot.getEvent());
        }
    }

    private static Kv<Integer, Class<?>> findParameter(
            final Method method) {
        int index = 0;
        final Kv<Integer, Class<?>> result = Kv.create();
        for (final Parameter parameter : method.getParameters()) {
            if (parameter.isAnnotationPresent(Codex.class)) {
                result.set(index, parameter.getType());
                break;
            }
            index++;
        }
        return result;
    }

    private static void verifyCodex(final RoutingContext context,
                                    final Map<String, List<Rule>> rulers,
                                    final Depot depot,
                                    final Class<?> type,
                                    final Object value) {
        final Rigor rigor = Rigor.get(type);
        if (null == rigor) {
            LOGGER.warn(Info.RIGOR_NOT_FOUND, type);
            context.next();
        } else {
            final WebException error = rigor.verify(rulers, value);
            if (null == error) {
                // Ignore Errors
                context.next();
            } else {
                // Reply Error
                replyError(context, error, depot.getEvent());
            }
        }
    }

    private static WebException verifyPureArguments(
            final Validator verifier,
            final Depot depot,
            final Object[] args) {
        final Event event = depot.getEvent();
        final Object proxy = event.getProxy();
        final Method method = event.getAction();
        WebException error = null;
        try {
            if (Virtual.is(proxy)) {
                // TODO: Wait for proxy generation
                // Validation for dynamic proxy
                // final Object delegate = Instance.getProxy(method);
                // verifier.verifyMethod(delegate, method, args);
            } else {
                // Validation for proxy
                verifier.verifyMethod(proxy, method, args);
            }
        } catch (final WebException ex) {
            // Basic validation failure
            error = ex;
        }
        return error;
    }

    private static WebException verifyUpload(final RoutingContext context) {
        final HttpServerRequest request = context.request();
        if (request.isExpectMultipart()) {
            if (!request.headers().contains(HttpHeaders.CONTENT_LENGTH)) {
                /*
                 * Content-Length = 0
                 */
                return new _411ContentLengthException(Flower.class, 0);
            } else {
                return null;
            }
        } else return null;
    }
}
