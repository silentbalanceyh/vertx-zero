package cn.vertxup.rbac.api;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.eon.KName;

import javax.ws.rs.BodyParam;
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

    // ---------------- 权限管理面板 -------------------
    /*
     * 读取权限管理区域，区域分为两种类型：
     * - ROLE: 角色权限
     * - USER：用户权限（一般是特权）
     */
    @GET
    @Path("/authority/region/:type")
    @Address(Addr.Rule.FETCH_REGION)
    Future<JsonArray> fetchRegion(@PathParam(KName.TYPE) String type);

    /*
     * 读取权限管理区域所有的值
     * - 由于类型已经在 regions 中有所说明，所以此处读取不分类型
     * - 必须指定 owner 以确定读取哪个用户、哪个角色
     * - regions 数据结构
     * {
     *     "regions": [
     *         "code1",
     *         "code2"
     *     ]
     * }
     **/
    @GET
    @Path("/authority/region-v/:owner")
    @Address(Addr.Rule.FETCH_REGION_VALUES)
    Future<JsonObject> fetchInitials(
        @PathParam(KName.OWNER) String owner,
        @BodyParam JsonObject regions);
}
