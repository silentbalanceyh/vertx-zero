package io.vertx.up.secure.authorization;

import io.vertx.core.MultiMap;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.AuthorizationContext;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.eon.ID;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._403ForbiddenException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.runtime.ZeroAnno;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;

/**
 * Async authorization handler to extract resource from async database
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PermissionHandler implements AuthorizationHandler {
    private static final Annal LOGGER = Annal.get(PermissionHandler.class);
    private final transient Collection<AuthorizationProvider> providers;
    private final transient Authorization authorization;
    private BiConsumer<RoutingContext, AuthorizationContext> consumer;

    private PermissionHandler() {
        this.providers = new ArrayList<>();
        this.authorization = AuthorizationForbidden.create();
    }

    public static PermissionHandler create() {
        return Fn.poolThread(Pool.HANDLER_POOL, PermissionHandler::new, PermissionHandler.class.getName());
    }

    @Override
    public AuthorizationHandler variableConsumer(final BiConsumer<RoutingContext, AuthorizationContext> handler) {
        this.consumer = handler;
        return this;
    }

    @Override
    public void handle(final RoutingContext event) {
        if (Objects.isNull(event.user())) {
            /*
             * Whether the user is `null`, if null, return 403 exception
             */
            final WebException error = new _403ForbiddenException(this.getClass());
            event.fail(error);
        } else {
            /*
             * Before starting any potential async operation here
             * pause parsing the request body, The reason is that we don't want to
             * loose the body or protocol upgrades for async operations
             */
            event.request().pause();
            /*
             * The modification for default to async fetch authorization
             */
            try {
                // Context Creation ( Async )
                // create the authorization context
                final AuthorizationContext authorizationContext = this.getAuthorizationContext(event);
                // check or fetch authorizations
                this.checkOrFetchAuthorizations(event, authorizationContext, this.providers.iterator());
            } catch (final RuntimeException ex) {
                event.request().resume();
                event.fail(ex);
            }
        }
    }

    private void checkOrFetchAuthorizations(final RoutingContext routingContext, final AuthorizationContext authorizationContext, final Iterator<AuthorizationProvider> providers) {
        if (this.authorization.match(authorizationContext)) {
            // resume the processing of the request
            routingContext.request().resume();
            routingContext.next();
            return;
        }
        if (!providers.hasNext()) {
            // resume as the error handler may allow this request to become valid again
            routingContext.request().resume();
            routingContext.fail(new _403ForbiddenException(this.getClass()));
            return;
        }

        // there was no match, in this case we do the following:
        // 1) contact the next provider we haven't contacted yet
        // 2) if there is a match, get out right away otherwise repeat 1)
        while (providers.hasNext()) {
            final AuthorizationProvider provider = providers.next();
            // we haven't fetch authorization from this provider yet
            if (!routingContext.user().authorizations().getProviderIds().contains(provider.getId())) {
                final User user = routingContext.user();
                if (provider instanceof PermissionProvider) {
                    // Zero workflow for Permission Provider
                    final JsonObject request = this.getResource(routingContext);
                    ((PermissionProvider) provider).getAuthorizations(user, request, result -> {
                        if (result.failed()) {
                            LOGGER.warn("[ Auth ] (Zero) Error occurs when getting authorization - providerId: {0}", provider.getId());
                            LOGGER.jvm(result.cause());
                            routingContext.fail(result.cause());
                        }
                        this.checkOrFetchAuthorizations(routingContext, authorizationContext, providers);
                    });
                } else {
                    provider.getAuthorizations(user, result -> {
                        if (result.failed()) {
                            LOGGER.warn("[ Auth ] Error occurs when getting authorization - providerId: {0}", provider.getId());
                            LOGGER.jvm(result.cause());
                            routingContext.fail(result.cause());
                        }
                        this.checkOrFetchAuthorizations(routingContext, authorizationContext, providers);
                    });
                }
                // get out right now as the callback will decide what to do next
                return;
            }
        }
    }

    private AuthorizationContext getAuthorizationContext(final RoutingContext event) {
        final AuthorizationContext result = AuthorizationContext.create(event.user());
        if (this.consumer != null) {
            this.consumer.accept(event, result);
        }
        return result;
    }

    @Override
    public AuthorizationHandler addAuthorizationProvider(final AuthorizationProvider authorizationProvider) {
        Objects.requireNonNull(authorizationProvider);
        this.providers.add(authorizationProvider);
        return this;
    }

    /*
     * This method is for dynamic using of 403 authorization.
     * Sometimes when the URI has been stored as resource in zero system,
     * You can extract the metadata in @Wall classes by
     *      data.getJsonObject("metadata");
     * It means that you can find unique resource identifier by
     * 1) Http Method: GET, DELETE, POST, PUT
     * 2) Uri Original
     * Here are some calculation results that has been provided by zero container such as following situation:
     * When the registry uri is as : /api/test/:name
     * In this situation the real path should be : /api/test/lang
     * In this method the metadata -> uri will be provided by : /api/test/:name
     *                    metadata -> requestUri will be provided by : /api/test/lang
     * It's specific situation when you used path variable.
     *
     * 「Objective」
     * The metadata stored for real project when you want to do some limitation in RBAC mode.
     * Because the application system will scanned our storage to do resource authorization, the application
     * often need the metadata information to do locating and checking here.
     */
    private JsonObject getResource(final RoutingContext context) {
        final JsonObject normalized = new JsonObject();
        final HttpServerRequest request = context.request();
        /*
         * Build metadata
         */
        final JsonObject metadata = new JsonObject();
        /*
         * Old: request.uri()
         * New: request.path()
         * path() will remove all query string part
         */
        metadata.put(KName.URI, ZeroAnno.recoveryUri(request.path(), request.method()));
        metadata.put(KName.URI_REQUEST, request.path());
        metadata.put(KName.METHOD, request.method().name());
        /*
         * view parameters for ScRequest to build cache key
         * It's important
         */
        final String literal = request.getParam(KName.VIEW);
        final Vis view = Vis.create(literal);
        metadata.put(KName.VIEW, view);
        normalized.put(KName.METADATA, metadata);
        /*
         * Build Custom Headers
         */
        final MultiMap inputHeaders = request.headers();
        final JsonObject headers = new JsonObject();
        inputHeaders.forEach(entry -> {
            if (ID.Header.PARAM_MAP.containsKey(entry.getKey())) {
                headers.put(entry.getKey(), entry.getValue());
            }
        });
        normalized.put("headers", headers);
        /*
         * Build data part ( collect all data )
         */
        return normalized;
    }
}
