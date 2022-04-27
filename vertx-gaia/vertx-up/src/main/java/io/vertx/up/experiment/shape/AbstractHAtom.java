package io.vertx.up.experiment.shape;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Strings;
import io.vertx.up.experiment.mixture.*;
import io.vertx.up.experiment.rule.RuleUnique;
import io.vertx.up.experiment.shape.atom.HAtomMetadata;
import io.vertx.up.experiment.shape.atom.HAtomReference;
import io.vertx.up.experiment.shape.atom.HAtomUnique;
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
    private static final Cc<Integer, HAtomReference> CC_REFERENCE = Cc.open();

    protected final String unique;
    protected final String appName;

    protected transient final HAtomUnique ruler;
    protected transient final HAtomMetadata metadata;
    protected transient final HAtomReference reference;

    private transient final Vs vs;

    public AbstractHAtom(final HModel model, final String appName) {
        Objects.requireNonNull(model);
        this.appName = appName;
        this.unique = HApp.ns(appName, model.identifier());
        // Model Hash Code
        final Integer modelCode = model.hashCode();
        this.ruler = CC_RULE.pick(() -> new HAtomUnique(model), modelCode);
        this.metadata = CC_METADATA.pick(() -> this.newMetadata(model), modelCode);
        this.reference = CC_REFERENCE.pick(() -> this.newReference(model), modelCode);
        this.vs = Vs.create(this.unique, this.metadata.types());
    }

    // ------------ 基础模型部分 ------------
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

    // ------------ 子类实现 ------------
    protected abstract <T extends HModel> HAtomMetadata newMetadata(final T model);

    protected abstract <T extends HModel> HAtomReference newReference(final T model);

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
