package io.vertx.up.unity;

import io.vertx.core.*;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.eventbus.Message;
import io.vertx.core.http.HttpServerRequest;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.ext.web.FileUpload;
import io.vertx.ext.web.RoutingContext;
import io.vertx.ext.web.Session;
import io.vertx.up.atom.Refer;
import io.vertx.up.atom.worker.Mission;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.web._417JobMethodException;
import io.vertx.up.specification.action.Commercial;
import io.vertx.up.util.Ut;
import jakarta.ws.rs.BodyParam;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;

class Web {

    static <T> Handler<AsyncResult<T>> toHandler(
        final Message<Envelop> message
    ) {
        return handler -> {
            if (handler.succeeded()) {
                message.reply(To.toEnvelop(handler.result()));
            } else {
                // Readible codec for configured information, error flow needed.
                if (null != handler.cause()) {
                    handler.cause().printStackTrace();
                }
                message.reply(Envelop.failure(Ut.failWeb(Web.class, handler.cause())));
            }
        };
    }

    static <T> Future<T> toFuture(final Consumer<Handler<AsyncResult<T>>> handler) {
        final Promise<T> promise = Promise.promise();
        handler.accept(result -> {
            if (result.succeeded()) {
                promise.complete(result.result());
            } else {
                promise.fail(result.cause());
            }
        });
        return promise.future();
    }

    static JsonObject pageData(JsonArray data, Long count) {
        if (Ut.isNil(data)) {
            data = new JsonArray();
        }
        if (Objects.isNull(count) || 0 > count) {
            count = 0L;
        }
        return new JsonObject().put(KName.LIST, data).put(KName.COUNT, count);
    }

    static JsonObject pageData(final JsonObject pageData, final Function<JsonArray, JsonArray> function) {
        final JsonArray data = Ut.valueJArray(pageData.getJsonArray(KName.LIST));
        final JsonArray updated;
        if (Objects.nonNull(function)) {
            updated = function.apply(data);
        } else {
            updated = data;
        }
        return pageData.put(KName.LIST, updated);
    }


    /**
     * Job Component
     */
    static Object toParameter(final Envelop envelop, final Class<?> type,
                              final Mission mission, final Refer underway) {
        if (Envelop.class == type) {
            /*
             * Envelop
             */
            return envelop;
        } else if (is(type, Session.class)) {
            /*
             * 「ONCE Only」Session
             */
            return envelop.session();
        } else if (is(type, User.class)) {
            /*
             * 「ONCE Only」User
             */
            return envelop.user();
        } else if (is(type, MultiMap.class)) {
            /*
             * 「ONCE Only」Headers
             */
            return envelop.headers();
        } else if (is(type, Commercial.class)) {
            /*
             * Commercial specification
             */
            final JsonObject metadata = mission.getMetadata();
            final String className = metadata.getString(KName.__.CLASS);
            if (Ut.isNotNil(className)) {
                final Commercial commercial = Ut.instance(className);
                commercial.fromJson(metadata);
                return commercial;
            } else {
                return null;
            }
        } else if (is(type, JsonObject.class)) {
            if (type.isAnnotationPresent(BodyParam.class)) {
                /*
                 * @BodyParam, it's for data passing
                 */
                return envelop.data();
            } else {
                /*
                 * Non @BodyParam, it's for configuration of current job here.
                 * Return to additional data of JsonObject
                 * This method will be used in future.
                 */
                return mission.getAdditional().copy();
            }
        } else if (is(type, Mission.class)) {
            /*
             * Actor/Director must
             */
            return mission;
        } else if (is(type, Refer.class)) {
            /*
             * Bind Assist call here
             */
            return underway;
        } else {
            throw new _417JobMethodException(Web.class, mission.getCode());
        }
    }

    /*
     * used by
     * `io.vertx.up.uca.invoker.InvokerUtil`.
     *
     * Worker Component
     */
    static Object toParameter(final Envelop envelop, final Class<?> type) {
        final Object returnValue;
        final RoutingContext context = envelop.context();
        if (is(type, XHeader.class)) {
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
        } else if (is(type, Session.class)) {
            /*
             * RBAC required ( When Authenticate )
             * 1) Provide username / password to get data from remote server.
             * 2) Request temp authorization code ( Required Session ).
             */
            returnValue = envelop.session();
        } else if (is(type, HttpServerRequest.class)) {
            /* HttpServerRequest type */
            returnValue = context.request();
        } else if (is(type, HttpServerResponse.class)) {
            /* HttpServerResponse type */
            returnValue = context.response();
        } else if (is(type, Vertx.class)) {
            /* Vertx type */
            returnValue = context.vertx();
        } else if (is(type, EventBus.class)) {
            /* EventBus type */
            returnValue = context.vertx().eventBus();
        } else if (is(type, User.class)) {
            /*
             * User type
             */
            returnValue = envelop.user();
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
    static Object toParameter(final RoutingContext context, final Class<?> type) {
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
            /* Http Session */
            returnValue = context.session();
        } else if (is(type, HttpServerRequest.class)) {
            /* HttpServerRequest type */
            returnValue = context.request();
        } else if (is(type, HttpServerResponse.class)) {
            /* HttpServerResponse type */
            returnValue = context.response();
        } else if (is(type, Vertx.class)) {
            /* Vertx type */
            returnValue = context.vertx();
        } else if (is(type, EventBus.class)) {
            /* EventBus type */
            returnValue = context.vertx().eventBus();
        } else if (is(type, User.class)) {
            /* User type */
            returnValue = context.user();
        } else if (is(type, Set.class)) {
            /*
             * It's only for file uploading here.
             * ( FileUpload ) type here for actual in agent
             */
            returnValue = new HashSet<>(context.fileUploads());
        } else if (is(type, JsonArray.class)) {
            /*
             * JsonArray, Could get from Serialization
             */
            returnValue = context.body().asJsonArray();
            if (Objects.isNull(returnValue)) {
                returnValue = new JsonArray();
            }
        } else if (is(type, JsonObject.class)) {
            /*
             * JsonObject, Could get from Serialization
             */
            returnValue = context.body().asJsonObject();
            if (Objects.isNull(returnValue)) {
                returnValue = new JsonObject();
            }
        } else if (is(type, Buffer.class)) {
            /*
             * Buffer, Could get from Serialization
             */
            returnValue = context.body().buffer();
            if (Objects.isNull(returnValue)) {
                returnValue = Buffer.buffer();
            }
        } else if (is(type, FileUpload.class)) {
            /*
             * Single FileUpload
             */
            final Set<FileUpload> uploads = new HashSet<>(context.fileUploads());
            if (!uploads.isEmpty()) {
                returnValue = uploads.iterator().next();
            }
        }
        return returnValue;
    }

    private static boolean is(final Class<?> paramType, final Class<?> expected) {
        return expected == paramType || Ut.isImplement(paramType, expected);
    }
}
