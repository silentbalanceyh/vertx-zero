package io.vertx.up.secure.authorization;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authorization.*;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthorizationHandler;
import io.vertx.up.annotations.Authorized;
import io.vertx.up.atom.secure.Aegis;
import io.vertx.up.eon.em.AuthWord;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._403ForbiddenException;
import io.vertx.up.exception.web._500InternalServerException;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * Async authorization handler to extract resource from async database
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WallAuthorizationHandler implements AuthorizationHandler {
    private static final Annal LOGGER = Annal.get(WallAuthorizationHandler.class);
    private transient final Aegis aegis;

    private transient Authorization authorization;
    private final transient Collection<AuthorizationProvider> providers;
    private BiConsumer<RoutingContext, AuthorizationContext> consumer;

    private WallAuthorizationHandler(final Aegis aegis) {
        this.aegis = aegis;
        this.providers = new ArrayList<>();
    }

    public static WallAuthorizationHandler create(final Aegis aegis) {
        return Fn.poolThread(Pool.POOL, () -> new WallAuthorizationHandler(aegis), WallAuthorizationHandler.class.getName());
    }

    @Override
    public AuthorizationHandler addAuthorizationProvider(final AuthorizationProvider authorizationProvider) {
        Objects.requireNonNull(authorizationProvider);
        this.providers.add(authorizationProvider);
        return this;
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
            this.runAuthorization(event.user(), v -> {
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
            });
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
                provider.getAuthorizations(routingContext.user(), result -> {
                    if (result.failed()) {
                        LOGGER.warn("[ Auth ] Error occurs when getting authorization - providerId: {0}", provider.getId());
                        LOGGER.jvm(result.cause());
                        // LOG.warn("An error occured getting authorization - providerId: " + provider.getId(), authorizationResult.cause());
                        // note that we don't 'record' the fact that we tried to fetch the authorization provider. therefore it will be re-fetched later-on
                    }
                    this.checkOrFetchAuthorizations(routingContext, authorizationContext, providers);
                });
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

    @SuppressWarnings("all")
    private void runAuthorization(final User user, final Handler<AsyncResult<Void>> handler) {
        if (Objects.isNull(this.authorization)) {
            final Method method = this.aegis.getAuthorizer().getAuthorization();
            Fn.safeJvm(() -> {
                final Future<Object> future = (Future) method.invoke(this.aegis.getProxy(), user);
                future.onComplete(res -> {
                    if (res.succeeded()) {
                        this.authorization = this.runAuthorizationSuccess(res.result());
                        handler.handle(Future.succeededFuture());
                    } else {
                        final Throwable ex = runAuthorizationFailure(res.cause());
                        handler.handle(Future.failedFuture(ex));
                    }
                });
            });
        } else {
            // Get current
            handler.handle(Future.succeededFuture());
        }
    }

    public Throwable runAuthorizationFailure(final Throwable ex) {
        if (Objects.isNull(ex)) {
            return new _500InternalServerException(this.getClass(), "Authorization Get Error!");
        } else {
            return ex;
        }
    }

    @SuppressWarnings("all")
    public Authorization runAuthorizationSuccess(final Object returnT) {
        final Method method = this.aegis.getAuthorizer().getAuthorization();
        final Authorization single;
        if (returnT instanceof Set) {
            final Set<Authorization> set = (Set<Authorization>) returnT;
            final Annotation annotation = method.getAnnotation(Authorized.class);
            final AuthWord word = Ut.invoke(annotation, "value");
            Objects.requireNonNull(word);
            if (AuthWord.AND == word) {
                final AndAuthorization combine = AndAuthorization.create();
                set.forEach(combine::addAuthorization);
                single = combine;
            } else {
                final OrAuthorization combine = OrAuthorization.create();
                set.forEach(combine::addAuthorization);
                single = combine;
            }
        } else {
            single = (Authorization) returnT;
        }
        return single;
    }
}
