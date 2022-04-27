package io.vertx.tp.atom.modeling.data;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.eon.Strings;
import io.vertx.up.experiment.mixture.*;
import io.vertx.up.experiment.rule.RuleUnique;
import io.vertx.up.uca.compare.Vs;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 内部使用的元数据分析工具，提供
 * 当前 DataRecord的专用 辅助工具，核心元数据处理工厂
 */
public class DataAtom implements HAtom {

    private transient final AoDefine metadata;
    private transient final AoUnique ruler;
    private transient final AoMarker marker;
    private transient final AoReference reference;

    private transient final Vs vs;
    private transient final String unique;
    private transient final String appName;

    public DataAtom(final Model model, final String appName) {
        this.appName = appName;
        this.unique = Ao.toNS(appName, model.identifier()); //  Model.namespace(appName) + "-" + model.identifier();
        /*
         * 1. 基础模型信息
         * 2. 标识规则信息
         * 3. 基础标识信息
         * 4. 数据引用信息
         */
        final Integer modelCode = model.hashCode();
        this.metadata = Pool.CC_INFO.pick(() -> new AoDefine(model), modelCode);
        // Fn.po?l(Pool.META_INFO, modelCode, () -> new AoDefine(model));
        this.ruler = Pool.CC_RULE.pick(() -> new AoUnique(model), modelCode);
        // Fn.po?l(Pool.META_RULE, modelCode, () -> new AoUnique(model));
        this.marker = Pool.CC_MARKER.pick(() -> new AoMarker(model), modelCode);
        // Fn.po?l(Pool.META_MARKER, modelCode, () -> new AoMarker(model));
        this.reference = Pool.CC_REFERENCE.pick(() -> new AoReference(model, appName), modelCode);
        // Fn.po?l(Pool.META_REFERENCE, modelCode, () -> new AoReference(model, appName));
        this.vs = Vs.create(this.unique, this.metadata.types());
    }

    // ------------ 基础模型部分 ------------
    @Override
    public String atomKey(final JsonObject options) {
        final String hashCode = Ut.isNil(options) ? Strings.EMPTY : String.valueOf(options.hashCode());
        return this.metadata.identifier() + "-" + hashCode;
    }

    @Override
    public DataAtom atom(final String identifier) {
        return Ao.toAtom(this.appName, identifier);
    }

    /** 返回当前 Model 中的所有属性集 */
    @Override
    public Set<String> attribute() {
        return this.metadata.attribute();
    }

    @Override
    public HAttribute attribute(final String name) {
        return this.metadata.attribute(name);
    }

    /** 返回 name = alias */
    @Override
    public ConcurrentMap<String, String> alias() {
        return this.metadata.alias();
    }

    @Override
    public String alias(final String name) {
        return this.metadata.alias().getOrDefault(name, Strings.EMPTY);
    }

    @Override
    @SuppressWarnings("unchecked")
    public Model model() {
        return this.metadata.model();
    }

    /* 返回当前记录关联的 identifier */
    @Override
    public String identifier() {
        return this.metadata.identifier();
    }

    /* 返回当前记录专用的 sigma */
    @Override
    public String sigma() {
        return this.metadata.sigma();
    }

    /* 返回语言信息 */
    @Override
    public String language() {
        return this.metadata.language();
    }

    /* 属性类型 */
    @Override
    public ConcurrentMap<String, Class<?>> type() {
        final Set<String> attributes = this.metadata.attribute();
        final ConcurrentMap<String, Class<?>> result = new ConcurrentHashMap<>();
        attributes.forEach(name -> result.put(name, this.type(name)));
        return result;
    }

    @Override
    public Class<?> type(final String field) {
        final HTField attribute = this.metadata.type(field);
        return Objects.isNull(attribute) ? String.class : attribute.type();
    }

    /** 返回 Shape 对象 */
    @Override
    public HTAtom shape() {
        return this.metadata.shape();
    }

    // ------------ 比对专用方法 ----------

    @Override
    public Vs vs() {
        return this.vs;
    }

    // ------------ 引用专用方法 ----------

    @Override
    public HReference reference() {
        return this.reference;
    }
    // ------------ 标识规则 ----------

    /** 存储的规则 */
    @Override
    public RuleUnique ruleAtom() {
        return this.ruler.rule();
    }


    /** 智能检索规则 */
    @Override
    public RuleUnique ruleSmart() {
        return this.ruler.ruleSmart();
    }

    /** 连接的规则 */
    @Override
    public RuleUnique rule() {
        return this.ruler.ruleDirect();
    }

    /** 规则的链接 */
    @Override
    public DataAtom rule(final RuleUnique channelRule) {
        this.ruler.connect(channelRule);
        return this;
    }

    // ------------ 属性检查的特殊功能，收集相关属性 ----------
    /*
     * 模型本身打开Track属性
     */
    @Override
    public Boolean trackable() {
        return this.marker.trackable();
    }

    /*
     * 解决空指针问题，
     * isTrack
     * isConfirm
     * isSyncIn
     * isSyncOut
     *
     * 关于这四个属性需要详细说明
     * 1. Track：是否生成变更历史, 001
     * 2. Confirm：是否生成待确认, 002
     * 3. SyncIn：同步拉取, 003
     * 4. SyncOut：同步推送, 004
     */
    @Override
    public Set<String> falseTrack() {
        return this.marker.track(Boolean.FALSE);
    }

    @Override
    public Set<String> trueTrack() {
        return this.marker.track(Boolean.TRUE);
    }

    @Override
    public Set<String> falseIn() {
        return this.marker.in(Boolean.FALSE);
    }

    /*
     * 集成过程中引入
     */
    @Override
    public Set<String> trueIn() {
        return this.marker.in(Boolean.TRUE);
    }

    @Override
    public Set<String> falseOut() {
        return this.marker.out(Boolean.FALSE);
    }

    @Override
    public Set<String> trueOut() {
        return this.marker.out(Boolean.TRUE);
    }

    @Override
    public Set<String> falseConfirm() {
        return this.marker.confirm(Boolean.FALSE);
    }

    @Override
    public Set<String> trueConfirm() {
        return this.marker.confirm(Boolean.TRUE);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataAtom)) {
            return false;
        }
        final DataAtom atom = (DataAtom) o;
        return this.unique.equals(atom.unique);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.unique);
    }
}
