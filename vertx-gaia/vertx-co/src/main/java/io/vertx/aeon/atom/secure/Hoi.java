package io.vertx.aeon.atom.secure;

import io.vertx.aeon.eon.em.ModeApp;
import io.vertx.up.experiment.specification.power.KApp;
import io.vertx.up.experiment.specification.power.KTenant;

import java.io.Serializable;
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
public class Hoi implements Serializable {
    // 多层租户
    private final ConcurrentMap<String, Hoi> children = new ConcurrentHashMap<>();
    // 应用模式
    private ModeApp mode = ModeApp.CUBE;
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

    private Hoi(final ModeApp mode) {
        this.mode = mode;
    }

    public static Hoi create(final ModeApp mode) {
        return new Hoi(mode);
    }

    public Hoi bind(final KApp app) {
        this.app = app;
        return this;
    }

    public Hoi bind(final KTenant tenant) {
        this.tenant = tenant;
        return this;
    }

    public Hoi child(final String tenantId) {
        return this.children.getOrDefault(tenantId, null);
    }
}
