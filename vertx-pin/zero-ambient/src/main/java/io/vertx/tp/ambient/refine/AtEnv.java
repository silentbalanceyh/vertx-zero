package io.vertx.tp.ambient.refine;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.tp.ambient.atom.AtConfig;
import io.vertx.tp.ambient.cv.AtConstant;
import io.vertx.tp.ambient.init.AtPin;
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
}
