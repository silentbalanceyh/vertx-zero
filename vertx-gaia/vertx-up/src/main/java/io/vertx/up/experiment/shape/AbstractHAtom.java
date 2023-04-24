package io.vertx.up.experiment.shape;

import io.aeon.experiment.mu.KMarker;
import io.aeon.experiment.rule.RuleUnique;
import io.aeon.experiment.specification.power.KApp;
import io.horizon.specification.modeler.*;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.bridge.Strings;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.uca.compare.Vs;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractHAtom implements HAtom {

    private static final Cc<Integer, HAtomUnique> CC_RULE = Cc.open();
    private static final Cc<Integer, HAtomMetadata> CC_METADATA = Cc.open();

    protected final String unique;
    protected final KApp app;

    protected transient final HAtomUnique ruler;
    protected transient final HAtomMetadata metadata;
    protected transient final HReference reference;
    protected transient final KMarker marker;

    private transient final Vs vs;

    public AbstractHAtom(final KApp app, final HModel model) {
        Objects.requireNonNull(model);
        Objects.requireNonNull(app);
        this.app = app;
        this.unique = app.keyUnique(model.identifier());
        // Model Hash Code
        final Integer modelCode = model.hashCode();
        this.ruler = CC_RULE.pick(() -> new HAtomUnique(model), modelCode);
        this.metadata = CC_METADATA.pick(() -> new HAtomMetadata(model), modelCode);

        // Belong to model interface instead
        this.reference = model.reference();
        this.marker = model.tag();
        this.vs = Vs.create(this.unique, this.metadata.types());
    }

    // ------------ 基础模型部分 ------------
    @Override
    public String language() {
        return Objects.isNull(this.app) ? null :
            Ut.valueString(this.app.dimJ(), KName.LANGUAGE);
    }

    @Override
    public String sigma() {
        return Objects.isNull(this.app) ? null :
            Ut.valueString(this.app.dimJ(), KName.SIGMA);
    }

    @Override
    public String atomKey(final JsonObject options) {
        final String hashCode = Ut.isNil(options) ? Strings.EMPTY : String.valueOf(options.hashCode());
        return this.metadata.identifier() + "-" + hashCode;
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
    public <T extends HModel> T model() {
        return this.metadata.model();
    }

    /* 返回当前记录关联的 identifier */
    @Override
    public String identifier() {
        return this.metadata.identifier();
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
        final TypeField attribute = this.metadata.type(field);
        return Objects.isNull(attribute) ? String.class : attribute.type();
    }

    /** 返回 Shape 对象 */
    @Override
    public TypeAtom shape() {
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

    // ------------ 标识规则 ------------

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
    @SuppressWarnings("unchecked")
    public <T extends HAtom> T rule(final RuleUnique channelRule) {
        this.ruler.connect(channelRule);
        return (T) this;
    }

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
    public KMarker marker() {
        return this.marker;
    }

    // ==================== Equal for Atom ==================
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AbstractHAtom)) {
            return false;
        }
        final AbstractHAtom atom = (AbstractHAtom) o;
        return this.unique.equals(atom.unique);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.unique);
    }
}
