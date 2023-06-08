package cn.vertxup.integration.api;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.is.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;
import jakarta.ws.rs.*;

/**
 * Here the directory support `CRUD` on single record only, it means that
 *
 * 1. No Batch deleting processing
 * 2. But when you do modification/deleting on a directory that contains sub-dir, the system should
 * update `storePath` and rename instead
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface DirAgent {

    @POST
    @Path("/i-directory")
    @Address(Addr.Directory.ADD)
    JsonObject create(@BodyParam JsonObject body);

    @PUT
    @Path("/i-directory/:key")
    @Address(Addr.Directory.UPDATE)
    JsonObject update(@PathParam(KName.KEY) String key, @BodyParam JsonObject body);

    @DELETE
    @Path("/i-directory/:key")
    @Address(Addr.Directory.DELETE)
    JsonObject remove(@PathParam(KName.KEY) String key);
}
