package cn.vertxup.ambient.api.file;

import io.vertx.core.json.JsonArray;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.ID;
import io.vertx.up.eon.KName;

import javax.ws.rs.*;

/**
 * Document Management
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

    /*
     * Document Management Platform
     * 1. Fetch Category by `zero.directory`.
     * 2. Capture the component of these three and call `ExIo` interface ( Service Loader )
     * 3. Create all folders based on components defined.
     */
    @Path("/document/start/:type")
    @GET
    @Address(Addr.Doc.DOCUMENT)
    JsonArray start(@PathParam(KName.TYPE) String type,
                    @HeaderParam(ID.Header.X_APP_ID) String appId);

}
