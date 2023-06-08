package io.vertx.up.secure.authenticate;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.ext.auth.User;
import io.vertx.ext.auth.authentication.AuthenticationProvider;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.handler.AuthenticationHandler;
import io.vertx.ext.web.handler.HttpException;
import io.vertx.ext.web.handler.impl.AuthenticationHandlerImpl;
import io.vertx.ext.web.handler.impl.AuthenticationHandlerInternal;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ChainHandlerImpl extends AuthenticationHandlerImpl<AuthenticationProvider> implements ChainHandler {

    private final List<AuthenticationHandlerInternal> handlers = new ArrayList<>();
    private final boolean all;

    private int willRedirect = -1;

    public ChainHandlerImpl(final boolean all) {
        super(null);
        this.all = all;
    }

    @Override
    public boolean performsRedirect() {
        return this.willRedirect != -1;
    }

    @Override
    public synchronized ChainHandler add(final AuthenticationHandler other) {
        if (this.performsRedirect()) {
            throw new IllegalStateException("Cannot add a handler after a handler known to perform a HTTP redirect: " + this.handlers.get(this.willRedirect));
        }
        final AuthenticationHandlerInternal otherInternal = (AuthenticationHandlerInternal) other;
        // control if we should not allow more handlers due to the possibility of a redirect to happen
        if (otherInternal.performsRedirect()) {
            this.willRedirect = this.handlers.size();
        }
        this.handlers.add(otherInternal);
        return this;
    }

    @Override
    public void authenticate(final RoutingContext context, final Handler<AsyncResult<User>> handler) {
        if (this.handlers.size() == 0) {
            handler.handle(Future.failedFuture("No providers in the auth chain."));
        } else {
            // iterate all possible authN
            this.iterate(0, context, null, null, handler);
        }
    }

    private void iterate(final int idx, final RoutingContext ctx, final User result, final Throwable exception, final Handler<AsyncResult<User>> handler) {
        // stop condition
        if (idx >= this.handlers.size()) {
            if (this.all) {
                // no more providers, if the call is signaling an error we fail as the last handler failed
                if (exception == null) {
                    handler.handle(Future.succeededFuture(result));
                } else {
                    handler.handle(Future.failedFuture(exception));
                }
            } else {
                // no more providers, means that we failed to find a provider capable of performing this operation
                handler.handle(Future.failedFuture(exception));
            }
            return;
        }

        // parse the request in order to extract the credentials object
        final AuthenticationHandlerInternal authHandler = this.handlers.get(idx);

        authHandler.authenticate(ctx, res -> {
            if (res.failed()) {
                if (this.all) {
                    /*
                     * Critical modification that's different from
                     * io.vertx.ext.web.handler.ChainAuthHandler is that all the handler should be
                     * in sequence flow, it means that here are the code execution:
                     *
                     * Monad<User> -> Monad<User> -> Monad<User> ....
                     *
                     * All the handlers will be in sequence and all must be passed, instead of
                     * stateless here.
                     */
                } else {
                    // any handler can be valid, if the response is within a validation error
                    // the chain is allowed to proceed, otherwise we must abort.
                    if (res.cause() instanceof final HttpException ex) {
                        switch (ex.getStatusCode()) {
                            case 302, 400, 401, 403 -> {
                                // try again with next provider since we know what kind of error it is
                                this.iterate(idx + 1, ctx, null, ex, handler);
                                return;
                            }
                        }
                    }
                    // the error is not a validation exception, so we abort regardless
                }
                handler.handle(Future.failedFuture(res.cause()));
                return;
            }

            if (this.all) {
                /*
                 * 「Change Point」Mount result User for next handler processing, it means that you can extract
                 * User data from previous result here, the condition is:
                 */
                ctx.setUser(res.result());
                // this handler is succeeded, but as we need all, we must continue with
                // the iteration of the remaining handlers.
                this.iterate(idx + 1, ctx, res.result(), null, handler);
            } else {
                // a single success is enough to signal the end of the validation
                handler.handle(Future.succeededFuture(res.result()));
            }
        });
    }
}
