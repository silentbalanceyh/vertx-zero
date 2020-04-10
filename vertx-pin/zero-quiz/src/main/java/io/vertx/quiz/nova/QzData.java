package io.vertx.quiz.nova;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Envelop;
import io.vertx.up.log.Annal;
import io.vertx.up.util.Ut;
import io.vertx.quiz.cv.QzApi;

import java.io.Serializable;

/*
 * Data Container for different data hold.
 */
class QzData implements Serializable {

    private final transient JsonObject input;
    private final transient JsonObject json;
    private transient QzApi api;

    private QzData(final JsonObject json) {
        this.json = json;
        this.input = json.getJsonObject("input");
    }

    static QzData create(final Class<?> target, final String filename) {
        final JsonObject data = Ut.ioJObject(QzIo.ioFile(target, filename));
        return new QzData(data);
    }

    /*
     * Data Normalized
     */
    QzData init() {
        /* Current method that will return Future<Envelop> */
        this.api = QzIo.getApi(this.json);
        /* Input Source */
        return this;
    }

    Envelop input() {
        return Envelop.success(this.input);
    }

    @SuppressWarnings("all")
    <T> Future<T> async() {
        final Annal logger = Annal.get(getClass());
        logger.info("[ ZERO Qz ] Api Type = {0}", this.api);
        if (QzApi.DEFINED != this.api) {
            return (Future<T>) Pool.METHOD_POOL.get(this.api).apply(input());
        } else {
            // TODO:
            return Future.succeededFuture();
        }
    }
}
