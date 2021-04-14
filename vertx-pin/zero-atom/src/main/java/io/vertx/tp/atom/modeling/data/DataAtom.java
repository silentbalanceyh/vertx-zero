package io.vertx.tp.atom.modeling.data;

import io.vertx.tp.atom.cv.AoCache;
import io.vertx.tp.atom.cv.AoMsg;
import io.vertx.tp.atom.modeling.Model;
import io.vertx.tp.atom.modeling.reference.RQuery;
import io.vertx.tp.atom.modeling.reference.RQuote;
import io.vertx.tp.atom.modeling.reference.RResult;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.modular.phantom.AoPerformer;
import io.vertx.up.commune.element.CParam;
import io.vertx.up.commune.element.Shape;
import io.vertx.up.commune.rule.RuleUnique;
import io.vertx.up.fn.Fn;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 内部使用的元数据分析工具，提供
 * 当前 DataRecord的专用 辅助工具，核心元数据处理工厂
 */
public class DataAtom {

    private transient final AoPerformer performer;

    private transient final String unique;
    private transient final String appName;

    private transient final MetaInfo metadata;
    private transient final MetaRule ruler;
    private transient final MetaMarker marker;
    private transient final MetaReference reference;

    private DataAtom(final String appName,
                     final String identifier,
                     final String unique) {
        /* Performer池化（每个App不一样）*/
        this.appName = appName;
        this.performer = AoPerformer.getInstance(appName);
        /* 构造当前模型的唯一值，从外置传入 */
        this.unique = unique;
        final Model model = Fn.pool(AoCache.POOL_MODELS, unique, () -> this.performer.fetchModel(identifier));
        /*
         * 1. 基础模型信息
         * 2. 标识规则信息
         * 3. 基础标识信息
         * 4. 数据引用信息
         */
        final Integer modelCode = model.hashCode();
        this.metadata = Fn.pool(Pool.META_INFO, modelCode, () -> new MetaInfo(model));
        this.ruler = Fn.pool(Pool.META_RULE, modelCode, () -> new MetaRule(model));
        this.marker = Fn.pool(Pool.META_MARKER, modelCode, () -> new MetaMarker(model));
        this.reference = Fn.pool(Pool.META_REFERENCE, modelCode, () -> new MetaReference(model, appName));

        /* LOG: 日志处理 */
        Ao.infoAtom(this.getClass(), AoMsg.DATA_ATOM, unique, model.toJson().encode());
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
        final String unique = Model.namespace(appName) + "-" + identifier;
        return new DataAtom(appName, identifier, unique);
    }

    // ------------ 基础模型部分 ------------

    /** 返回当前 Model 中的所有属性集 */
    public Set<String> attributes() {
        return this.metadata.attributes();
    }

    /** 返回 name = alias */
    public ConcurrentMap<String, String> alias() {
        return this.metadata.alias();
    }

    public Model getModel() {
        return this.metadata.reference();
    }

    /* 返回当前记录关联的 identifier */
    public String identifier() {
        return this.metadata.identifier();
    }

    /* 返回当前记录专用的 sigma */
    public String sigma() {
        return this.metadata.sigma();
    }

    /* 返回语言信息 */
    public String language() {
        return this.metadata.language();
    }

    /* 属性类型 */
    public ConcurrentMap<String, Class<?>> type() {
        return this.metadata.type();
    }

    /** 返回 Shape 对象 */
    public Shape shape() {
        return this.metadata.shape();
    }

    public Class<?> type(final String field) {
        return this.metadata.type(field);
    }

    // ------------ 比对专用方法 ----------

    public CParam diff(final Set<String> ignoreSet) {
        return this.metadata.diff(ignoreSet).diff(this.reference.fieldDiff());
    }

    public Set<String> diffSet(final String field) {
        return this.reference.fieldDiff().getOrDefault(field, new HashSet<>());
    }

    // ------------ 引用专用方法 ----------

    public ConcurrentMap<String, RQuote> refInput() {
        return this.reference.refInput();
    }

    public ConcurrentMap<String, RResult> refOutput() {
        return this.reference.refOutput();
    }

    public ConcurrentMap<String, RQuery> refQuery() {
        return this.reference.refQr();
    }
    // ------------ 标识规则 ----------

    /** 存储的规则 */
    public RuleUnique rule() {
        return this.ruler.rule();
    }

    /** 连接的规则 */
    public RuleUnique ruleDirect() {
        return this.ruler.ruleDirect();
    }

    /** 智能检索规则 */
    public RuleUnique ruleSmart() {
        return this.ruler.ruleSmart();
    }

    /** 规则的链接 */
    public DataAtom ruleConnect(final RuleUnique channelRule) {
        this.ruler.connect(channelRule);
        return this;
    }

    // ------------ 属性检查的特殊功能，收集相关属性 ----------
    /*
     * 模型本身打开Track属性
     */
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
    public Set<String> falseTrack() {
        return this.marker.track(Boolean.FALSE);
    }

    public Set<String> trueTrack() {
        return this.marker.track(Boolean.TRUE);
    }

    public Set<String> falseIn() {
        return this.marker.in(Boolean.FALSE);
    }

    /*
     * 集成过程中引入
     */
    public Set<String> trueIn() {
        return this.marker.in(Boolean.TRUE);
    }

    public Set<String> falseOut() {
        return this.marker.out(Boolean.FALSE);
    }

    public Set<String> trueOut() {
        return this.marker.out(Boolean.TRUE);
    }

    public Set<String> falseConfirm() {
        return this.marker.confirm(Boolean.FALSE);
    }

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
