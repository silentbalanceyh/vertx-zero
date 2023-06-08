package cn.vertxup.ambient.api.file;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KWeb;
import jakarta.ws.rs.*;

/**
 * Document Management Api
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface DocAgent {
    @GET
    @Path("/document/by/directory")
    @Address(Addr.Doc.BY_DIRECTORY)
    JsonArray byDirectory(@QueryParam(KName.DIRECTORY_ID) String directoryId);

    @GET
    @Path("/document/by/keyword/:keyword")
    @Address(Addr.Doc.BY_KEYWORD)
    JsonArray byKeyword(@PathParam(KName.KEY_WORD) String keyword);

    @GET
    @Path("/document/by/trashed")
    @Address(Addr.Doc.BY_TRASHED)
    JsonArray byTrashed();


    // ----------------- Operation Api ----------------------
    /*
     * Document Management Platform
     * 1. Fetch Category by `zero.directory`.
     * 2. Capture the component of these three and call `ExIo` interface ( Service Loader )
     * 3. Create all folders based on components defined ( First Time ).
     */
    @Path("/document/start/:type")
    @GET
    @Address(Addr.Doc.DOCUMENT)
    JsonArray start(@PathParam(KName.TYPE) String type,
                    @HeaderParam(KWeb.HEADER.X_APP_ID) String appId);

    /*
     * Following Operation Api Data Structure for each is as following
     * {
     *      "key": "???",
     *      "directory": "true - DIRECTORY, false - FILE"
     * }
     */
    @Path("/document/trash")
    @DELETE
    @Address(Addr.Doc.DOCUMENT_TRASH)
    JsonArray trashIn(@BodyParam JsonArray documentA);

    @Path("/document/purge")
    @DELETE
    @Address(Addr.Doc.DOCUMENT_PURGE)
    JsonArray trashKo(@BodyParam JsonArray documentA);

    @Path("/document/rollback")
    @PUT
    @Address(Addr.Doc.DOCUMENT_ROLLBACK)
    JsonArray trashOut(@BodyParam JsonArray documentA);

    @Path("/file/rename")
    @PUT
    @Address(Addr.File.RENAME)
    JsonObject rename(@BodyParam JsonObject documentJ);

    // ----------------- File Batch Operation ----------------------
    @Path("/file/upload")
    @POST
    @Address(Addr.File.UPLOAD_CREATION)
    JsonArray upload(@BodyParam JsonArray documentA);

    @Path("/file/download")
    @POST
    @Address(Addr.File.DOWNLOADS)
    JsonArray download(@BodyParam JsonArray keys);
}
