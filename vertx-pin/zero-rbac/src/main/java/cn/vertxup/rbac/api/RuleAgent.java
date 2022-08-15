package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.pojos.SPath;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.EndPoint;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.eon.KName;

import javax.ws.rs.*;

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
     * 读取权限管理区域所有的值（只能单独存储）
     * - 由于类型已经在 regions 中有所说明，所以此处读取不分类型
     * - 必须指定 owner 以确定读取哪个用户、哪个角色
     * - regions 数据结构
     * {
     *     ....
     * }
     * - 根据传入的 region 完整Json提取初始化数据
     **/
    @POST
    @Path("/authority/region-v/:owner")
    @Address(Addr.Rule.FETCH_REGION_VALUES)
    Future<JsonObject> fetchInitials(
            @PathParam(KName.OWNER) String owner,
            @BodyParam SPath region,
            // ?view=[] Matrix Param for View Located
            @PointParam(KName.VIEW) Vis view);
}
