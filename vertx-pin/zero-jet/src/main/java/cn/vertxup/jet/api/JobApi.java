package cn.vertxup.jet.api;

import io.vertx.core.json.JsonObject;
import io.vertx.mod.jet.cv.JtAddr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KWeb;
import jakarta.ws.rs.*;

@Path("/api")
@EndPoint
public interface JobApi {
    @Path("/job/start/{code}")
    @PUT
    @Address(JtAddr.Job.START)
    String startJob(@PathParam("code") String code);

    @Path("/job/stop/{code}")
    @PUT
    @Address(JtAddr.Job.STOP)
    String stopJob(@PathParam("code") String code);

    @Path("/job/resume/{code}")
    @PUT
    @Address(JtAddr.Job.RESUME)
    String resumeJob(@PathParam("code") String code);

    @Path("/job/info/status/{namespace}")
    @GET
    @Address(JtAddr.Job.STATUS)
    String statusJob(@PathParam("namespace") String namespace);

    @Path("/job/info/by/sigma")
    @POST
    @Address(JtAddr.Job.BY_SIGMA)
    String fetchJobs(@HeaderParam(KWeb.HEADER.X_SIGMA) String sigma,
                     @BodyParam JsonObject body,
                     @QueryParam("group") @DefaultValue("false") Boolean grouped);

    @Path("/job/info/mission/:key")
    @GET
    @Address(JtAddr.Job.GET_BY_KEY)
    String fetchJob(@PathParam("key") String key);

    @Path("/job/info/mission/:key")
    @PUT
    @Address(JtAddr.Job.UPDATE_BY_KEY)
    String updateJob(@PathParam("key") String key,
                     @BodyParam JsonObject data);

}
