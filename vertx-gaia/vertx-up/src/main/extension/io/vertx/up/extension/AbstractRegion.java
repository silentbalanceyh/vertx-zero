package io.vertx.up.extension;

import io.horizon.uca.log.Annal;
import io.horizon.uca.qr.syntax.Ir;
import io.vertx.core.http.HttpMethod;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.configure.YmlCore;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;

public abstract class AbstractRegion implements PlugRegion {

    private static final Set<HttpMethod> METHOD_SET = Set.of(
        HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS);
    private transient final JsonObject config = new JsonObject();

    @Override
    public PlugRegion bind(final JsonObject config) {
        this.config.mergeIn(config);
        return this;
    }

    protected JsonObject config() {
        return this.config;
    }

    protected Annal getLogger() {
        return Annal.get(this.getClass());
    }

    /*
     * 启用数据域的核心条件
     * 1. 已配置了 prefix，且请求路径必须是以 prefix 开始
     * 2. HttpMethod 方法必须是 POST / GET 两种
     *    2.1. GET 查询数据时启用数据域
     *    2.2. POST 查询数据时启用数据域
     * 3. 将来考虑扩展模式（OPTIONS方法默认启用）
     */
    protected boolean isEnabled(final RoutingContext context) {
        final HttpMethod method = context.request().method();
        if (!METHOD_SET.contains(method)) {
            // HttpMethod 不匹配
            return false;
        }
        final String prefix = this.config.getString(YmlCore.extension.region.config.PREFIX);
        if (Ut.isNil(prefix)) {
            // prefix 未配置
            this.getLogger().warn("Data Region require config `prefix` attribute value, but now is null. Disabled! ");
            return false;
        }
        final String requestUri = context.request().path();
        return requestUri.startsWith(prefix);
    }

    /*
     * DataMatrix 是否启用 DataRegion 的专用判断方法
     * 1. matrix == null
     *    返回 false，不启用视图流程
     * 2. matrix 中包含：projection / criteria / credit / rows 数据
     *    启用视图流程
     * 3. matrix 中包含：seeker
     *    启用视图流程，并且是 visitant 流程
     */
    protected boolean isRegion(final JsonObject matrix) {
        if (Objects.isNull(matrix)) {
            return false;               // 禁用视图流程
        }
        boolean isEnabled = Ut.isNotNil(Ut.valueJArray(matrix, Ir.KEY_PROJECTION));
        if (isEnabled) {
            return true;                // 启用流程 projection 有值
        }
        isEnabled = Ut.isNotNil(Ut.valueJArray(matrix, KName.Rbac.CREDIT));
        if (isEnabled) {
            return true;                // 启用流程 credit 有值
        }
        isEnabled = Ut.isNotNil(Ut.valueJObject(matrix, KName.Rbac.ROWS));
        if (isEnabled) {
            return true;                // 启用流程 rows 有值
        }
        isEnabled = Ut.isNotNil(Ut.valueJObject(matrix, Ir.KEY_CRITERIA));
        if (isEnabled) {
            return true;                // 启用流程 criteria
        }
        // 最后检查 seeker 流程
        final boolean seeker = Ut.isNotNil(Ut.valueJObject(matrix, KName.SEEKER));
        if (seeker) {
            // seeker = true, 检查 view 是否存在
            final JsonObject viewJ = Ut.valueJObject(matrix, KName.VIEW);
            return Ut.isNotNil(viewJ);
        }
        return false;
    }
}
