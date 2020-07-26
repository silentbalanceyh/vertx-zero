package cn.vertxup.rbac.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@EndPoint
@Path("/api")
public interface CriterionAgent {

    @GET
    @Path("/rules")
    @Address(Addr.Rule.FETCH_BY_SIGMA)
    Future<JsonArray> fetchAsync();

    @GET
    @Path("/rule-items/rule/:ruleId")
    @Address(Addr.Rule.FETCH_RULE_ITEMS)
    Future<JsonArray> fetchPocket(@PathParam("ruleId") String ruleId);
}
