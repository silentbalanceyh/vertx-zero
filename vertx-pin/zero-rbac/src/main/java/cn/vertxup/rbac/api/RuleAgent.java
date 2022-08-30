package cn.vertxup.rbac.api;

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
        @BodyParam JsonObject region,
        // ?view=[] Matrix Param for View Located
        @PointParam(KName.VIEW) Vis view);

    // ---------------- 权限保存 -------------------
    /*
     * 更新 code = ? 的 SPath 关联的所有资源记录，此处
     * viewData 的结构如下：
     * {
     *     "owner":         ??,
     *     "ownerType":     ??,
     *     "name":          ??,
     *     "position":      ??,
     *     "active":        true,
     *     "language":      cn,
     *     "sigma":         xxx,
     *     "resource": {
     *         "code1": {
     *              "rows":          ??,
     *              "projection":    ??,
     *              "criteria":      ??
     *         }
     *     }
     * }
     *
     * view的逻辑执行：
     * 1) 检查是否存在
     *    -- 视图存在（直接更新视图内容）
     *    -- 视图不存在（插入新视图）
     * 2）更新资源相对应的权限信息
     * 3）更新权限缓存
     */
    @POST
    @Path("/authority/region/:path")
    @Address(Addr.Rule.SAVE_REGION)
    Future<JsonObject> saveRegion(
        @PathParam(KName.PATH) String path,
        @BodyParam JsonObject viewData
    );
}
