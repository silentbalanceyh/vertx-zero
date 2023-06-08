package io.aeon.atom.secure;

import io.horizon.eon.VString;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.EmSecure;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 「许可定义」
 * 区域定义，用于定义当前系统中的权限管理所需的基础配置结构
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KPermit implements Serializable {
    /*
     * 基础分类维度计算，如菜单管理，根据维度区分成不同维度的管理，每个维度的管理模式维持一致
     * 维度计算基本法则，基础维度（资源定位专用）
     * -- 1. 树根分类：如菜单分成不同菜单组
     * -- 2. 树型分类：每一个分类下是否需要根据属性结构执行分类
     * -- 3. 最终分组条件设置，根据条件筛选合理的资源集合
     * -- 4. 最终搭载界面配置进行呈现
     */
    private final String name;
    private final String code;
    /*
     * 界面配置数据源
     * -- 1. 目标数据源类型
     * -- 2. 目标界面配置
     * -- 3. 查询模板
     * -- 4. 组件设置
     */
    // ------------------- DIM 配置 --------------------
    private final JsonObject shapeQr = new JsonObject();
    private final JsonObject shapeConfig = new JsonObject();
    private final JsonObject shapeMapping = new JsonObject();
    // ------------------- UI 配置 --------------------
    private final JsonObject uiQr = new JsonObject();
    private final JsonObject uiConfig = new JsonObject();
    private final JsonObject uiSurface = new JsonObject();
    private final String sigma;
    // ------------------- 子节点 --------------------
    // code = KPermit
    private final ConcurrentMap<String, KPermit> children = new ConcurrentHashMap<>();
    private EmSecure.ScDim shape = EmSecure.ScDim.NONE;
    private EmSecure.ScIn source = EmSecure.ScIn.NONE;
    private EmSecure.ActPhase phase = EmSecure.ActPhase.EAGER;

    /*
     * 权限所需执行表
     * -- 1. 关联权限集
     * -- 2. 关联权限
     * -- 3. 关联接口
     */
    private KPermit(final String code, final String sigma, final String name) {
        this.code = code;
        this.sigma = sigma;
        this.name = name;
    }

    public static KPermit create(final String code, final String sigma) {
        return new KPermit(code, sigma, null);
    }

    public static KPermit create(final String code, final String sigma, final String name) {
        return new KPermit(code, sigma, name);
    }

    // ===================== 维度配置
    public KPermit shape(final EmSecure.ScDim scDim) {
        this.shape = scDim;
        return this;
    }

    public KPermit shape(final JsonObject qr, final JsonObject config) {
        final JsonObject qrJ = Ut.valueJObject(qr);
        final JsonObject configJ = Ut.valueJObject(config);
        this.shapeQr.mergeIn(qrJ, true);
        this.shapeConfig.mergeIn(configJ, true);
        return this;
    }

    public KPermit shape(final JsonObject mapping) {
        final JsonObject mappingJ = Ut.valueJObject(mapping);
        this.shapeMapping.mergeIn(mappingJ, true);
        return this;
    }

    // ===================== 配置提取
    /*
     * Shape处理的规范配置结构
     * {
     *     ...shapeConfig,
     *     "mapping": shapeMapping,
     *     "qr":      shapeQr
     * }
     *
     * DM_MAPPING       -> mapping
     * DM_CONDITION     -> qr
     */
    public JsonObject dmJ() {
        final JsonObject shapeJ = this.shapeConfig.copy();
        final JsonObject mappingJ = Ut.valueJObject(this.shapeMapping);
        shapeJ.put(KName.MAPPING, mappingJ);
        final JsonObject qrJ = Ut.valueJObject(this.shapeQr);
        shapeJ.put(KName.Rbac.QR, qrJ);
        return shapeJ;
    }

    /*
     * UI处理的规范配置结构
     * {
     *     ...uiConfig,
     *     "surface": uiSurface,
     *     "qr":      uiQr
     * }
     *
     * DM_SURFACE       -> surface
     * DM_CONDITION     -> qr
     */
    public JsonObject uiJ() {
        final JsonObject uiJ = this.uiConfig.copy();
        final JsonObject qrJ = Ut.valueJObject(this.uiQr);
        uiJ.put(KName.Rbac.QR, qrJ);
        final JsonObject surfaceJ = Ut.valueJObject(this.uiSurface);
        uiJ.put(KName.Rbac.SURFACE, surfaceJ);
        return uiJ;
    }

    // ===================== 界面配置
    public KPermit ui(final EmSecure.ScIn source, final EmSecure.ActPhase phase) {
        this.source = source;
        this.phase = phase;
        return this;
    }

    public KPermit ui(final JsonObject surface) {
        final JsonObject surfaceJ = Ut.valueJObject(surface);
        this.uiSurface.mergeIn(surfaceJ, true);
        return this;
    }

    public KPermit ui(final JsonObject qr, final JsonObject config) {
        final JsonObject qrJ = Ut.valueJObject(qr);
        final JsonObject configJ = Ut.valueJObject(config);
        this.uiQr.mergeIn(qrJ, true);
        this.uiConfig.mergeIn(configJ, true);
        return this;
    }

    // ===================== 枚举数据提取，表定义中拿
    public EmSecure.ScDim shape() {
        return this.shape;
    }

    public EmSecure.ScIn source() {
        return this.source;
    }

    public EmSecure.ActPhase phase() {
        return this.phase;
    }

    // ===================== 基础数据
    public String code() {
        return this.code;
    }

    public String name() {
        return this.name;
    }

    public String sigma() {
        return this.sigma;
    }

    // ===================== 缓存键值
    public String keyCache() {
        return this.sigma + VString.SLASH + this.code;
    }

    // ===================== 子节点
    public KPermit child(final KPermit child) {
        final String code = child.code;
        if (Ut.isNotNil(code)) {
            this.children.put(code, child);
        }
        return this;
    }

    public KPermit child(final String code) {
        return this.children.getOrDefault(code, null);
    }

    public Set<KPermit> children() {
        return new HashSet<>(this.children.values());
    }
}
