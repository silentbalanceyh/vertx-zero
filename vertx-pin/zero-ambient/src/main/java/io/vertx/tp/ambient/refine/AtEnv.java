package io.vertx.tp.ambient.refine;

import cn.vertxup.ambient.domain.tables.daos.XSourceDao;
import cn.vertxup.ambient.domain.tables.pojos.XApp;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.tp.ambient.atom.AtApp;
import io.vertx.tp.ambient.atom.AtConfig;
import io.vertx.tp.ambient.cv.AtConstant;
import io.vertx.tp.ambient.cv.AtMsg;
import io.vertx.tp.ambient.init.AtPin;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.commune.config.Database;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import org.jooq.DSLContext;

import java.text.MessageFormat;
import java.util.UUID;

/*
 * Application XHeader
 */
class AtEnv {

    private static final Annal LOGGER = Annal.get(AtEnv.class);

    static XApp getApp(final DSLContext context,
                       final String name) {
        return AtApp.create(context, name).getApp();
    }

    static XApp getApp(final String name) {
        return AtApp.create(name).getApp();
    }


    /*
     * Must enable shared map when you call this method.
     */
    static Future<Database> getDatabaseWithCache(final String appId) {
        return Ke.poolAsync(AtConstant.POOL_DATABASE, appId, () -> getDatabase(appId));
    }

    /*
     * File Upload
     */
    static JsonObject upload(final String category,
                             final FileUpload fileUpload) {
        final JsonObject uploaded = new JsonObject();
        final String originalFile = fileUpload.fileName();
        if (Ut.notNil(originalFile) && originalFile.contains(".")) {
            // Config Read
            final AtConfig config = AtPin.getConfig();
            final int lastIndex = originalFile.lastIndexOf('.');
            final String fileName = originalFile.substring(0, lastIndex);
            final String extension = originalFile.substring(lastIndex + 1);
            // File key
            final String key = UUID.randomUUID().toString();
            // File Url
            final String downloadUrl = MessageFormat.format(AtConstant.DOWNLOAD_URI, key);
            uploaded.put("key", key)
                    .put("storeWay", config.getFileStorage())
                    .put("status", "PROGRESS")
                    .put("name", originalFile)
                    .put("fileKey", Ut.randomString(64))
                    .put("fileName", fileName)
                    .put("fileUrl", downloadUrl)
                    .put("filePath", fileUpload.uploadedFileName())
                    .put("extension", extension)
                    .put("module", category)
                    .put("mime", fileUpload.contentType())
                    .put("size", fileUpload.size())
                    .put("language", config.getFileLanguage())
                    .put("metadata", new JsonObject().encode());
        }
        return uploaded;
    }

    private static Future<Database> fromJsonAsync(final JsonObject source) {
        final Database database = new Database();
        database.fromJson(source);
        return Ux.future(database);
    }

    private static Future<Database> getDatabase(final String appId) {
        AtLog.infoEnv(LOGGER, AtMsg.SOURCE, appId);
        return Ux.Jooq.on(XSourceDao.class)
                .fetchOneAsync(KeField.APP_ID, appId)
                .compose(Ux::futureJ)
                .compose(AtEnv::fromJsonAsync);
    }
}
