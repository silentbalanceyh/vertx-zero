package io.vertx.up.secure.authorization;

import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.Authorization;
import io.vertx.ext.auth.authorization.AuthorizationContext;
import io.vertx.ext.auth.authorization.AuthorizationProvider;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._403ForbiddenException;
import io.vertx.up.log.Annal;

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
public class AuthorizationBuiltInHandler implements AuthorizationHandler {
    private static final Annal LOGGER = Annal.get(AuthorizationBuiltInHandler.class);
    private final transient Collection<AuthorizationProvider> providers;
    private final transient AuthorizationResource resource;
    private BiConsumer<RoutingContext, AuthorizationContext> consumer;

    private AuthorizationBuiltInHandler(final AuthorizationResource resource) {
        this.resource = resource;
        this.providers = new ArrayList<>();
    }

    // Built In
    public static AuthorizationBuiltInHandler create(final Aegis aegis) {
        return new AuthorizationBuiltInHandler(AuthorizationResource.buildIn(aegis));
    }

    // Build by Resource
    public static AuthorizationBuiltInHandler create(final AuthorizationResource resource) {
        return new AuthorizationBuiltInHandler(resource);
    }

    @Override
    public AuthorizationHandler variableConsumer(final BiConsumer<RoutingContext, AuthorizationContext> handler) {
        this.consumer = handler;
        return this;
    }

    @Override
    public void handle(final RoutingContext event) {
        final WebException error = new _403ForbiddenException(this.getClass());
        if (Objects.isNull(event.user())) {
            /*
             * Whether the user is `null`, if null, return 403 exception
             */
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
                this.resource.requestResource(event, res -> {
                    if (res.succeeded()) {
                        /*
                         * Iterator and checking
                         */
                        this.checkOrFetchAuthorizations(
                            event,                          // RoutingContext
                            res.result(),                   // Authorization
                            authorizationContext,           // AuthorizationContext
                            this.providers.iterator()       // Iterator<AuthorizationProvider>
                        );
                    } else {
                        // Exception happened
                        final Throwable ex = res.cause();
                        event.request().resume();
                        if (Objects.nonNull(ex)) {
                            event.fail(ex);
                        } else {
                            event.fail(error);
                        }
                    }
                });
            } catch (final RuntimeException ex) {
                event.request().resume();
                event.fail(ex);
            }
        }
    }

    /*
     * Here are
     */
    private void checkOrFetchAuthorizations(final RoutingContext routingContext,
                                            final Authorization resource,
                                            final AuthorizationContext authorizationContext,
                                            final Iterator<AuthorizationProvider> providers) {
        if (resource.match(authorizationContext)) {
            final User user = authorizationContext.user();
            LOGGER.info("[ Auth ] 403 Authorized successfully for user: principal = {0}, attribute = {1}",
                user.principal(), user.attributes());
            routingContext.request().resume();
            routingContext.next();
            return;
        }
        if (!providers.hasNext()) {
            // resume as the error handler may allow this request to become valid again
            // Here the provides has no next to process, it means that all the providers
            // fail to 401/403 workflow here.
            routingContext.request().resume();
            routingContext.fail(new _403ForbiddenException(this.getClass()));
            return;
        }

        // there was no match, in this case we do the following:
        // 1) contact the next provider we haven't contacted yet
        // 2) if there is a match, get out right away otherwise repeat 1)
        while (providers.hasNext()) {
            final AuthorizationProvider provider = providers.next();
            /*
             * we haven't fetched authorization from this provider yet,
             * in this situation, continue to next provider to validate, after validated successfully
             * you can call next() method of routing context to pass
             */
            if (!routingContext.user().authorizations().getProviderIds().contains(provider.getId())) {
                final User user = routingContext.user();
                provider.getAuthorizations(user, result -> {
                    if (result.failed()) {
                        LOGGER.warn("[ Auth ] Error occurs when getting authorization - providerId: {0}", provider.getId());
                        LOGGER.jvm(result.cause());
                    }
                    this.checkOrFetchAuthorizations(routingContext, resource, authorizationContext, providers);
                });
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

}
