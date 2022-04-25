package io.vertx.tp.atom.modeling.data;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.atom.cv.AoMsg;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.error._404ModelNotFoundException;
import io.vertx.tp.modular.phantom.AoPerformer;
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

    private DataAtom(final Model model, final String appName) {
        this.appName = appName;
        this.unique = Model.namespace(appName) + "-" + model.identifier();
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

    public static DataAtom get(final String appName,
                               final String identifier) {
        /*
         * 每次创建一个新的 DataAtom
         * - DataAtom 属于 Model 的聚合体，它的核心两个数据结构主要是 Performer 和 Model，这两个结构在底层做了
         * 池化处理，也就是说：访问器 和 模型（底层）都不会出现多次创建的情况，那么为什么 DataAtom 要创建多个呢？
         * 主要原因是 DataAtom 开始出现了分离，DataAtom 中的 RuleUnique 包含了两部分内容
         *
         * 1. RuleUnique 来自 Model （模型定义），Master 的 RuleUnique
         * 2. RuleUnique 来自 connect 编程模式，Slave 的 RuleUnique
         * 3. 每个 RuleUnique 的结构如
         *
         * {
         *      "...":"内容（根）",
         *      "children": {
         *          "identifier1": {},
         *          "identifier2": {}
         *      }
         * }
         *
         * 通道如果是静态绑定，则直接使用 children 就好
         * 而通道如果出现了动态绑定，IdentityComponent 实现，则要根据切换过后的 DataAtom 对应的 identifier 去读取相对应的
         * 标识规则。
         *
         * 所以，每次get的时候会读取一个新的 DataAtom 而共享其他数据结构。
         */
        try {
            /*
             * Performer processing to expose exception
             */
            final String unique = Model.namespace(appName) + "-" + identifier;
            final AoPerformer performer = AoPerformer.getInstance(appName);
            final Model model = AoCache.CC_MODEL.pick(() -> performer.fetchModel(identifier), unique);
            // Fn.po?l(AoCache.POOL_MODELS, unique, () -> performer.fetchModel(identifier));
            /*
             * Log for data atom and return to the reference.
             */
            final DataAtom atom = new DataAtom(model, appName);
            Ao.infoAtom(DataAtom.class, AoMsg.DATA_ATOM, unique, model.toJson().encode());
            return atom;
        } catch (final _404ModelNotFoundException ignored) {
            /*
             * 这里的改动主要基于动静态模型同时操作导致，如果可以找到Model则证明模型存在于系统中，这种
             * 情况下可直接初始化DataAtom走标准流程，否则直接返回null引用，使得系统无法返回正常模型，
             * 但不影响模型本身的执行。
             */
            return null;
        }
    }

    // ------------ 基础模型部分 ------------
    @Override
    public String atomKey(final JsonObject options) {
        final String hashCode = Ut.isNil(options) ? Strings.EMPTY : String.valueOf(options.hashCode());
        return this.metadata.identifier() + "-" + hashCode;
    }

    @Override
    public DataAtom atom(final String identifier) {
        return get(this.appName, identifier);
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
