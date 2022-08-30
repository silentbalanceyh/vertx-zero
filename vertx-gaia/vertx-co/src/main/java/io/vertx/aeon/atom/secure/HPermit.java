package io.vertx.aeon.atom.secure;

import io.vertx.aeon.eon.em.ScDim;
import io.vertx.aeon.eon.em.ScIn;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.run.ActPhase;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * 「许可定义」
 * 区域定义，用于定义当前系统中的权限管理所需的基础配置结构
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class HPermit implements Serializable {
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
    private ScDim shape = ScDim.NONE;
    private ScIn source = ScIn.NONE;

    private ActPhase phase = ActPhase.EAGER;

    /*
     * 权限所需执行表
     * -- 1. 关联权限集
     * -- 2. 关联权限
     * -- 3. 关联接口
     */
    private HPermit(final String code, final String sigma, final String name) {
        this.code = code;
        this.sigma = sigma;
        this.name = name;
    }

    public static HPermit create(final String code, final String sigma) {
        return new HPermit(code, sigma, null);
    }

    public static HPermit create(final String code, final String sigma, final String name) {
        return new HPermit(code, sigma, name);
    }

    // ===================== 维度配置
    public HPermit shape(final ScDim scDim) {
        this.shape = scDim;
        return this;
    }

    public HPermit shape(final JsonObject qr, final JsonObject config) {
        final JsonObject qrJ = Ut.valueJObject(qr);
        final JsonObject configJ = Ut.valueJObject(config);
        this.shapeQr.mergeIn(qrJ, true);
        this.shapeConfig.mergeIn(configJ, true);
        return this;
    }

    public HPermit shape(final JsonObject mapping) {
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
    public HPermit ui(final ScIn source, final ActPhase phase) {
        this.source = source;
        this.phase = phase;
        return this;
    }

    public HPermit ui(final JsonObject surface) {
        final JsonObject surfaceJ = Ut.valueJObject(surface);
        this.uiSurface.mergeIn(surfaceJ, true);
        return this;
    }

    public HPermit ui(final JsonObject qr, final JsonObject config) {
        final JsonObject qrJ = Ut.valueJObject(qr);
        final JsonObject configJ = Ut.valueJObject(config);
        this.uiQr.mergeIn(qrJ, true);
        this.uiConfig.mergeIn(configJ, true);
        return this;
    }

    // ===================== 枚举数据提取，表定义中拿
    public ScDim shape() {
        return this.shape;
    }

    public ScIn source() {
        return this.source;
    }

    public ActPhase phase() {
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
}
