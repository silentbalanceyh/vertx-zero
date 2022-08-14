package cn.vertxup.rbac.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@EndPoint
@Path("/api")
public interface RuleAgent {

    @GET
    @Path("/rule-items/rule/:ruleId")
    @Address(Addr.Rule.FETCH_RULE_ITEMS)
    Future<JsonArray> fetchPocket(@PathParam("ruleId") String ruleId);

    /* Admin Region */
    @GET
    @Path("/authority/region/:type")
    @Address(Addr.Rule.FETCH_REGION)
    Future<JsonArray> fetchRegion(@PathParam(KName.TYPE) String type);

    /*
     * Here provide two inputs
     * - type: ROLE | USER
     * - owner: key of owner entity
     **/
    @GET
    @Path("/authority/region-value/:type/:owner")
    @Address(Addr.Rule.FETCH_REGION_VALUES)
    Future<JsonObject> fetchInitials(
            @PathParam(KName.TYPE) String type,
            @PathParam(KName.OWNER) String owner);
}
