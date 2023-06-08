package io.vertx.up.commune;

import io.horizon.eon.em.web.HttpStatusCode;
import io.horizon.uca.log.Annal;
import io.modello.specification.HRecord;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.exchange.BMapping;
import io.vertx.up.atom.exchange.BTree;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;

public class ActOut extends ActMapping implements Serializable {

    private static final Annal LOGGER = Annal.get(ActOut.class);
    private transient final Envelop envelop;
    private transient String identifier;

    /*
     * Success or Failure response building
     */
    ActOut(final Object data, final HttpStatusCode statusCode) {
        this.envelop = Envelop.success(data, statusCode);
    }

    ActOut(final Object data) {
        this.envelop = Envelop.success(data, HttpStatusCode.OK);
    }

    ActOut(final Throwable ex) {
        this.envelop = Envelop.failure(ex);
    }

    /*
     * 1）The default response is 204 no content
     * 2）True / False result
     * 3）Buffer data here
     * 4）Throwable exception here
     *
     * These methods Do not support `identifier` binding
     */
    public static Future<ActOut> empty() {
        return Ux.future(Act.empty());
    }

    public static Future<ActOut> future(final Boolean result) {
        return Ux.future(Act.response(result));
    }

    public static Future<ActOut> future(final Buffer buffer) {
        return Ux.future(Act.response(buffer));
    }

    public static Future<ActOut> future(final Throwable ex) {
        return Ux.future(Act.response(ex));
    }

    /*
     * 1）JsonObject
     * 2）JsonArray
     * 3）Record[]
     * 4）Record
     */
    public static Future<ActOut> future(final JsonObject data) {
        return Ux.future(Act.response(data));
    }

    public static Future<ActOut> future(final JsonObject data, final String identifier) {
        return Ux.future(Act.response(data).bind(identifier));
    }

    public static Future<ActOut> future(final JsonArray dataArray) {
        return Ux.future(Act.response(dataArray));
    }

    public static Future<ActOut> future(final JsonArray dataArray, final String identifier) {
        return Ux.future(Act.response(dataArray).bind(identifier));
    }

    public static Future<ActOut> future(final HRecord[] records) {
        return Ux.future(Act.response(records));
    }

    public static Future<ActOut> future(final HRecord[] records, final String identifier) {
        return Ux.future(Act.response(records).bind(identifier));
    }

    public static Future<ActOut> future(final HRecord record) {
        return Ux.future(Act.response(record));
    }

    public static Future<ActOut> future(final HRecord record, final String identifier) {
        return Ux.future(Act.response(record).bind(identifier));
    }

    private ActOut bind(final String identifier) {
        if (Ut.isNotNil(identifier)) {
            this.identifier = identifier;
        }
        return this;
    }

    /*
     * Envelop processing
     */
    public Envelop envelop(final BTree mapping) {
        final Object response = this.envelop.data();
        if (response instanceof JsonObject || response instanceof JsonArray) {
            if (this.isAfter(mapping)) {
                final BMapping bMapping;
                if (Objects.isNull(this.identifier)) {
                    bMapping = mapping.child();
                } else {
                    bMapping = mapping.child(this.identifier);
                    if (!bMapping.isEmpty()) {
                        LOGGER.info("identifier `{0}`, extract child mapping. {1}", this.identifier, bMapping.toString());
                    }
                }
                final HttpStatusCode status = this.envelop.status();
                if (response instanceof JsonObject) {
                    /*
                     * JsonObject here for mapping
                     */
                    final JsonObject normalized = this.mapper().out(((JsonObject) response), bMapping);
                    return Envelop.success(normalized, status).from(this.envelop);
                } else {
                    /*
                     * JsonArray here for mapping
                     */
                    final JsonArray normalized = new JsonArray();
                    Ut.itJArray((JsonArray) response).map(item -> this.mapper().out(item, bMapping))
                        .forEach(normalized::add);
                    return Envelop.success(normalized, status).from(this.envelop);
                }
            } else {
                return this.envelop;
            }
        } else {
            return this.envelop;
        }
    }
}
