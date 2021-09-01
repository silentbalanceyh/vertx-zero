package cn.vertxup.crud.api;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.tp.crud.cv.Addr;
import io.vertx.tp.crud.cv.IxMsg;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Adjust;
import io.vertx.up.annotations.Codex;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Orders;
import io.vertx.up.unity.Ux;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;

/*
 * Export / Import file here for processing
 */
@EndPoint
@Path("/api")
public class FileAgent {


    @Path("/{actor}/import")
    @POST
    @Address(Addr.File.IMPORT)
    @Adjust(Orders.MODULE)
    public JsonObject importFile(@PathParam("actor") final String actor,
                                 @QueryParam("module") final String module,
                                 @StreamParam @Codex final FileUpload fileUpload) {
        /* File stored */
        final String filename = fileUpload.uploadedFileName();
        Ix.Log.dao(this.getClass(), IxMsg.FILE_UPLOAD, fileUpload.fileName(), filename);
        return Ux.toZip(actor, filename, module);
    }

    @Path("/{actor}/export")
    @POST
    @Address(Addr.File.EXPORT)
    @Adjust(Orders.MODULE)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public JsonObject exportFile(@PathParam("actor") final String actor,
                                 @PointParam(KName.VIEW) final JsonArray view,
                                 @QueryParam("module") final String module,
                                 @BodyParam final JsonObject condition) {
        /*
         * Toggle format here
         * {
         *     "0": xxx,
         *     "1": "view",
         *     "2": "module",
         *     "3": {
         *          "columns":[],
         *          "criteria": {}
         *     }
         *     ......
         * }
         */
        return Ux.toZip(actor, view, module, condition);
    }
}
