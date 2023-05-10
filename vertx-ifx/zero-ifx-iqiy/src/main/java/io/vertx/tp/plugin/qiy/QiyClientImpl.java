package io.vertx.tp.plugin.qiy;

import io.horizon.uca.log.Annal;
import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._401QiyTokenException;
import io.vertx.tp.plugin.qiy.api.QiyAuthorize;
import io.vertx.tp.plugin.qiy.api.QiyUpload;
import io.vertx.up.fn.Fn;

public class QiyClientImpl implements QiyClient {

    private static final Annal LOGGER = Annal.get(QiyClientImpl.class);

    private transient final Vertx vertx;
    private transient final QiyAuthorize authorizeApi;
    private transient QiyConfig config;

    QiyClientImpl(final Vertx vertx, final QiyConfig config) {
        this.vertx = vertx;
        this.config = config;
        // Authorized Api Reference
        this.authorizeApi = config.getInitApi(QiyAuthorize.class);
    }

    @Override
    public QiyClient init(final JsonObject config) {
        // Refresh QiyToken
        LOGGER.info(Info.TOKEN_RECORD, config);
        this.config = QiyConfig.create(config);
        return this;
    }

    @Override
    public QiyClient authorize(final Handler<AsyncResult<JsonObject>> handler) {
        QiyRepdor.handle(this.authorizeApi.authorize(this.config.getClientId(), this.config.getClientSecret()))
            .onComplete(res -> {
                this.config.setToken(res.result());
                handler.handle(Future.succeededFuture(res.result()));
            });
        return this;
    }

    @Override
    public QiyClient refreshToken(final String refreshToken,
                                  final Handler<AsyncResult<JsonObject>> handler) {
        QiyRepdor.handle(this.authorizeApi.refreshToken(this.config.getClientId(), refreshToken))
            .onComplete(res -> {
                this.config.setToken(res.result());
                handler.handle(Future.succeededFuture(res.result()));
            });
        return this;
    }

    @Override
    public QiyClient requestFile(final String fileType,
                                 final String fileSize,
                                 final Handler<AsyncResult<JsonObject>> handler) {
        // Check whether the config is valid
        Fn.outWeb(null == this.config || !this.config.isValid(), LOGGER,
            _401QiyTokenException.class, this.getClass(), this.config.getClientId());
        // Request upload
        final QiyUpload uploadApi = this.config.getUpApi(QiyUpload.class);
        QiyRepdor.complete(uploadApi.requestUpload(fileType, fileSize, this.config.getAccessToken()))
            .onComplete(res -> handler.handle(Future.succeededFuture(res.result())));
        return this;
    }

    @Override
    public QiyClient upload(final String address,
                            final String size,
                            final String range,
                            final String fileId,
                            final char[] content) {

        return this;
    }
}
