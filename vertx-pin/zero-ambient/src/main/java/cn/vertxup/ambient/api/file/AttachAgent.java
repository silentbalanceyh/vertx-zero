package cn.vertxup.ambient.api.file;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.mod.ambient.atom.AtConfig;
import io.vertx.mod.ambient.cv.Addr;
import io.vertx.mod.ambient.cv.AtConstant;
import io.vertx.mod.ambient.init.AtPin;
import io.vertx.mod.ke.cv.em.FileStatus;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;

import java.text.MessageFormat;
import java.util.UUID;

/*
 * Uniform attachment upload/download
 */
@EndPoint
@Path("/api")
public class AttachAgent {

    @Path("/file/upload/{identifier}")
    @POST
    @Address(Addr.File.UPLOAD)
    public JsonObject upload(@PathParam(KName.IDENTIFIER) final String identifier,
                             @QueryParam(KName.CATEGORY) final String category,
                             @QueryParam(KName.DIRECTORY) final String directory,
                             @StreamParam final FileUpload fileUpload) {
        final JsonObject uploaded = new JsonObject();
        final String originalFile = fileUpload.fileName();
        if (Ut.isNotNil(originalFile) && originalFile.contains(".")) {
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
                // New workflow for uploading, the default status is DONE
                .put(KName.STATUS, FileStatus.DONE.name())                      // File Status: PROGRESS, DONE
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
                .put(KName.LANGUAGE, config.getFileLanguage())                  // Configured System Language
                .put(KName.METADATA, new JsonObject().encode())                 // (Reserved)

                .put(KName.Attachment.STORE_WAY, config.getFileStorage())       // 「Dir」Configured Stored Way
                .put(KName.DIRECTORY, directory);                               // 「Dir」Will be calculate
            // Here only left `modelKey` field.
        }
        return uploaded;
    }

    @Path("/file/download/{fileKey}")
    @GET
    @Address(Addr.File.DOWNLOAD)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    public JsonObject download(@PathParam("fileKey") final String key) {
        return new JsonObject().put(KName.KEY, key);
    }
}
