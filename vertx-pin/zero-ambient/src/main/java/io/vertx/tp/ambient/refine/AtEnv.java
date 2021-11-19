package io.vertx.tp.ambient.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.tp.ambient.atom.AtConfig;
import io.vertx.tp.ambient.cv.AtConstant;
import io.vertx.tp.ambient.init.AtPin;
import io.vertx.tp.ke.cv.em.FileStatus;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.text.MessageFormat;
import java.util.UUID;

/*
 * Application XHeader
 */
class AtEnv {

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
            uploaded.put(KName.KEY, key)
                .put(KName.Attachment.STORE_WAY, config.getFileStorage())
                .put(KName.STATUS, FileStatus.PROGRESS.name())
                .put(KName.TYPE, fileUpload.contentType())
                .put(KName.MIME, fileUpload.contentType())
                .put(KName.NAME, originalFile)
                .put(KName.FILE_KEY, Ut.randomString(64))
                .put(KName.Attachment.FILE_NAME, fileName)
                .put(KName.Attachment.FILE_URL, downloadUrl)
                .put(KName.Attachment.FILE_PATH, fileUpload.uploadedFileName())
                .put(KName.EXTENSION, extension)
                .put(KName.MODULE, category)
                .put(KName.SIZE, fileUpload.size())
                .put(KName.LANGUAGE, config.getFileLanguage())
                .put(KName.METADATA, new JsonObject().encode());
        }
        return uploaded;
    }
}
