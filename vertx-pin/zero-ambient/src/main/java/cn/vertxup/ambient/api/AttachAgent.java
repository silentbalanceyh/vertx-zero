package cn.vertxup.ambient.api;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/*
 * Uniform attachment upload/download
 */
@EndPoint
@Path("/api")
public class AttachAgent {

    @Path("/file/upload/{category}")
    @POST
    @Address(Addr.File.UPLOAD)
    public JsonObject upload(@PathParam(KName.CATEGORY) final String category,
                             @StreamParam final FileUpload fileUpload) {
        return At.upload(category, fileUpload);
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
