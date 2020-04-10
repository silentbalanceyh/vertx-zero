package io.vertx.tp.plugin.qiy;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.AsyncResult;
import io.vertx.core.Handler;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.up.plugin.TpClient;

/**
 * QiyClient for platform of http://open.iqiyi.com/
 * Video open sdk
 */
public interface QiyClient extends TpClient<QiyClient> {

    static QiyClient createShared(final Vertx vertx) {
        return new QiyClientImpl(vertx, QiyConfig.create());
    }

    static QiyClient createShared(final Vertx vertx, final JsonObject config) {
        return new QiyClientImpl(vertx, QiyConfig.create(config));
    }

    @Fluent
    @Override
    QiyClient init(final JsonObject params);

    /**
     * /iqiyi/authorize
     *
     * @param handler async handler
     * @return self reference
     */
    @Fluent
    @SuppressWarnings("all")
    QiyClient authorize(Handler<AsyncResult<JsonObject>> handler);

    /**
     * /oauth2/token
     *
     * @param refreshToken refresh token
     * @param handler      async handler
     * @return self reference
     */
    @Fluent
    @SuppressWarnings("all")
    QiyClient refreshToken(String refreshToken, Handler<AsyncResult<JsonObject>> handler);

    /**
     * /openupload
     *
     * @param fileType request up.god.file type
     * @param size     request up.god.file size
     * @param handler  async handler
     * @return self reference
     */
    @Fluent
    @SuppressWarnings("all")
    QiyClient requestFile(String fileType, String size, Handler<AsyncResult<JsonObject>> handler);

    /**
     * @param address upload address
     * @param size    up.god.file size
     * @param range   size range
     * @param fileId  return id by requestFile
     * @param content File content part
     * @return self reference
     */
    @Fluent
    QiyClient upload(String address, String size, String range, String fileId, char[] content);
}
