package io.vertx.up.uca.serialization;

import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.util.Ut;

import java.util.Set;

/*
 * Analyze the arguments by type
 * 1) used by `io.vertx.up.uca.rs.mime.parse.TypedAtomic`.
 * 2) used by `io.vertx.up.uca.invoker.InvokerUtil`.
 * The arguments are different, but could support more method declare
 */
public class TypedArgument {
    /*
     * used by
     * `io.vertx.up.uca.invoker.InvokerUtil`.
     *
     * Worker Component
     */
    public static Object analyze(final Envelop envelop, final Class<?> type) {
        final Object returnValue;
        if (is(type, User.class)) {
            /*
             * User type
             */
            returnValue = envelop.user();
        } else if (is(type, Session.class)) {
            /*
             * RBAC required ( When Authenticate )
             * 1) Provide username / password to get data from remote server.
             * 2) Request temp authorization code ( Required Session ).
             */
            returnValue = envelop.getSession();
        } else if (is(type, XHeader.class)) {
            /*
             * XHeader for
             * - sigma
             * - appId
             * - appKey
             * - lang
             */
            final MultiMap headers = envelop.headers();
            final XHeader header = new XHeader();
            header.fromHeader(headers);
            returnValue = header;
        } else {
            /*
             * Typed failure
             */
            returnValue = null;
        }
        return returnValue;
    }

    /*
     * used by
     * `io.vertx.up.uca.rs.mime.parse.TypedAtomic`.
     *
     * Agent Component
     */
    public static Object analyze(final RoutingContext context, final Class<?> type) {
        Object returnValue = null;
        if (is(type, XHeader.class)) {
            /*
             * XHeader for
             * - sigma
             * - appId
             * - appKey
             * - lang
             */
            final HttpServerRequest request = context.request();
            final MultiMap headers = request.headers();
            final XHeader header = new XHeader();
            header.fromHeader(headers);
            returnValue = header;
        } else if (is(type, Session.class)) {
            /*
             * Http Session
             */
            returnValue = context.session();
        } else if (is(type, HttpServerRequest.class)) {
            /*
             * ( Agent Only ) HttpServerRequest type
             */
            returnValue = context.request();
        } else if (is(type, HttpServerResponse.class)) {
            /*
             *  ( Agent Only ) HttpServerResponse type
             */
            returnValue = context.response();
        } else if (is(type, Vertx.class)) {
            /*
             *  ( Agent Only ) Vertx type
             */
            returnValue = context.vertx();
        } else if (is(type, EventBus.class)) {
            /*
             *  ( Agent Only ) EventBus type
             */
            returnValue = context.vertx().eventBus();
        } else if (is(type, User.class)) {
            /*
             * User Type
             */
            returnValue = context.user();
        } else if (is(type, Set.class)) {
            /*
             * It's only for file uploading here.
             * ( FileUpload ) type here for actual in agent
             */
            final Class<?> actualType = type.getComponentType();
            if (is(actualType, FileUpload.class)) {
                returnValue = context.fileUploads();
            }
        } else if (is(type, JsonArray.class)) {
            /*
             * JsonArray, Could get from Serialization
             */
            returnValue = context.getBodyAsJsonArray();
        } else if (is(type, JsonObject.class)) {
            /*
             * JsonObject, Could get from Serialization
             */
            returnValue = context.getBodyAsJson();
        } else if (is(type, Buffer.class)) {
            /*
             * Buffer, Could get from Serialization
             */
            returnValue = context.getBody();
        }
        return returnValue;
    }

    private static boolean is(final Class<?> paramType, final Class<?> expected) {
        return expected == paramType || Ut.isImplement(paramType, expected);
    }
}
