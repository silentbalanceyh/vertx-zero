package io.macrocosm.atom;

import io.horizon.atom.app.KApp;
import io.horizon.atom.app.KTenant;
import io.horizon.eon.VName;
import io.horizon.eon.em.app.AppMode;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 多租户维度专用对象规范（该对象规范直接从 XHeader 中提取）
 * - sigma：统一标识符
 * - language：语言信息
 * - appKey / appId / appName（内置包含KApp）
 * - tenant：租户ID
 * 拥有者ID，特殊维度（云环境专用，处理核心维度专用）
 * oi = Owner ID
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HOI implements Serializable {
    // 多层租户
    private final ConcurrentMap<String, HOI> children = new ConcurrentHashMap<>();
    // 应用模式
    private final AppMode mode;
    /*
     * 此处区别一下 KApp 对象的内容
     * > 业务层面的应用概念，该应用不支持动态建模，只包含了基本信息如
     *   - name:                应用名称
     *   - ns:                  应用名空间
     *   - language:            应用程序所使用的语言
     *   - sigma:               统一标识符
     *
     * > 中层环境信息，开放更多系统级的字段
     *   - appId, appKey, code  应用层系统信息
     *   - database:            当前应用所需访问的数据库信息
     *
     * > Hoi 是开放了 Aeon 系统过后的系统级信息，它包含了租户环境下的
     *   - 单应用
     *   - 多租户
     *   - 多层租户
     *
     * KApp的目的是简易应用模式
     * KCube是开启了「动态建模、动态路由」之后的复杂模式
     * Hoi则是云模式专用：这个对象中才会出现租户的概念
     *
     */
    // 租户基础
    private KTenant tenant;
    // 应用引用
    private KApp app;

    private HOI(final AppMode mode) {
        this.mode = mode;
    }

    public static HOI create(final AppMode mode) {
        return new HOI(mode);
    }

    public HOI bind(final KApp app) {
        this.app = app;
        return this;
    }

    public HOI bind(final KTenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public AppMode mode() {
        return this.mode;
    }

    /*
     * 查询条件统一参数追加
     * - forBusiness = false
     *   常用容器模式，根据 ModeApp 计算最终结果
     * - forBusiness = true
     *   带名空间的模式，主要用于应用数据
     */
    public JsonObject inputQr(final JsonObject qr, final boolean forBusiness) {
        if (Objects.isNull(this.app)) {
            return qr;
        }
        if (forBusiness) {
            // 名空间模式
            // namespace / name
            qr.mergeIn(this.app.dimB(), true);
        } else {
            // 非名空间模式
            // appId / appKey
            qr.mergeIn(this.app.dimA(), true);
        }
        // sigma / language
        qr.mergeIn(this.app.dimJ(), true);

        if (AppMode.CUBE != this.mode && Objects.nonNull(this.tenant)) {
            // tenantId
            qr.put(VName.TENANT_ID, this.tenant.id());
        }
        return qr;
    }

    public HOI child(final String tenantId) {
        return this.children.getOrDefault(tenantId, null);
    }
}
