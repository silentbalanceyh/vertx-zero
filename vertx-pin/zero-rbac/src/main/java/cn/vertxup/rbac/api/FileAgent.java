package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.mod.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Codex;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.unity.Ux;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.StreamParam;

import static io.vertx.mod.rbac.refine.Sc.LOG;

/*
 * User
 * 1）Import here for processing, this import will overwrite
 * - /api/user/import of uri
 * 2) It will import multi roles format such as
 * - A,B,D
 */
@EndPoint
@Path("/api")
public class FileAgent {

    @Path("/user/import")
    @POST
    @Address(Addr.User.IMPORT)
    public JsonObject importUser(@StreamParam @Codex final FileUpload fileUpload) {
        /* File stored */
        final String filename = fileUpload.uploadedFileName();
        LOG.Web.info(this.getClass(), "User importing, filename = `{0}`, uploaded = `{1}`", fileUpload.fileName(), filename);
        return Ux.toZip(filename);
    }
}
