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
    static JsonObject upload(final String identifier,
                             final FileUpload fileUpload,
                             final String category) {
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
            uploaded.put(KName.KEY, key)                                        // The primary key of attachment
                .put(KName.STATUS, FileStatus.PROGRESS.name())                  // File Status: PROGRESS, DONE
                .put(KName.TYPE, fileUpload.contentType())                      // (Reserved)
                .put(KName.MIME, fileUpload.contentType())                      // MIME type here
                .put(KName.NAME, originalFile)                                  // File name: name.extension
                .put(KName.FILE_KEY, Ut.randomString(64))                // File Key that has been generated
                .put(KName.Attachment.FILE_NAME, fileName)                      // File name without extension: name
                .put(KName.EXTENSION, extension)                                // File extension name
                .put(KName.SIZE, fileUpload.size())                             // File size
                .put(KName.Attachment.FILE_URL, downloadUrl)                    // Download Url for user download
                .put(KName.Attachment.FILE_PATH, fileUpload.uploadedFileName()) // Stored file path, schedule remove all invalid files based on this field
                .put(KName.MODEL_ID, identifier)                                // Related Model Identifier
                .put(KName.MODEL_CATEGORY, category)                            // Related Model field dim for different category
                .put(KName.Attachment.STORE_WAY, config.getFileStorage())       // Configured Stored Way
                .put(KName.LANGUAGE, config.getFileLanguage())                  // Configured System Language
                .put(KName.METADATA, new JsonObject().encode());                // (Reserved)
            // Here only left `modelKey` field.
        }
        return uploaded;
    }
}
