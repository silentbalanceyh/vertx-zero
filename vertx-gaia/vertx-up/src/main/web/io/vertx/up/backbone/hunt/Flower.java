package io.vertx.up.backbone.hunt;

import io.horizon.atom.common.Kv;
import io.horizon.exception.WebException;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.annotations.Codex;
import io.vertx.up.atom.Rule;
import io.vertx.up.atom.agent.Depot;
import io.vertx.up.atom.agent.Event;
import io.vertx.up.atom.container.VInstance;
import io.vertx.up.backbone.announce.Rigor;
import io.vertx.up.commune.Envelop;
import io.vertx.up.exception.web._411ContentLengthException;
import io.vertx.up.extension.dot.PluginExtension;
import io.vertx.up.secure.validation.Validator;

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
            if (null == found.value()) {
                // Verify here.
                context.next();
            } else {
                // @Codex validation for different types
                final Class<?> type = found.value();
                final Object value = args[found.key()];
                verifyCodex(context, rulers, depot, type, value);
            }
        } else {
            // Hibernate validate handler
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
            LOGGER.warn(INFO.RIGOR_NOT_FOUND, type);
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
            final Object delegate;
            if (proxy instanceof VInstance) {
                // Validation for dynamic proxy
                final VInstance vInstance = (VInstance) proxy;
                delegate = vInstance.proxy();
            } else {
                // Validation for proxy
                delegate = proxy;
            }
            verifier.verifyMethod(delegate, method, args);
        } catch (final WebException ex) {
            // Basic validation handler
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
        } else {
            return null;
        }
    }
}
