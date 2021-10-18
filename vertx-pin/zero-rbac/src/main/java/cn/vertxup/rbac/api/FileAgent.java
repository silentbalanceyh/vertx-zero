package cn.vertxup.rbac.api;

import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Codex;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.unity.Ux;

import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.StreamParam;

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
        Sc.infoWeb(this.getClass(), "User importing, filename = `{0}`, uploaded = `{1}`", fileUpload.fileName(), filename);
        return Ux.toZip(filename);
    }
}
