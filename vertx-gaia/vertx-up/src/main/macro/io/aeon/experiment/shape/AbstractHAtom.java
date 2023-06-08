package io.aeon.experiment.shape;

import io.horizon.uca.cache.Cc;
import io.horizon.uca.compare.Vs;
import io.macrocosm.specification.program.HArk;
import io.modello.atom.normalize.KMarkAtom;
import io.modello.specification.atom.*;
import io.modello.specification.meta.HMetaAtom;
import io.modello.specification.meta.HMetaField;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractHAtom implements HAtom {

    private static final Cc<Integer, HAtomRule> CC_RULE = Cc.open();
    private static final Cc<Integer, HAtomMetadata> CC_METADATA = Cc.open();

    protected final String unique;

    protected transient final HAtomRule ruler;
    protected transient final HAtomMetadata metadata;
    protected transient final HReference reference;
    protected transient final KMarkAtom marker;

    private transient final Vs vs;

    public AbstractHAtom(final HModel model) {
        Objects.requireNonNull(model);
        final HArk ark = model.ark();
        this.unique = ark.cached(model.identifier());
        // Model Hash Code
        final Integer modelCode = model.hashCode();
        this.ruler = CC_RULE.pick(() -> new HAtomRule(model), modelCode);
        this.metadata = CC_METADATA.pick(() -> new HAtomMetadata(model), modelCode);

        // Belong to model interface instead
        this.reference = model.reference();
        this.marker = model.marker();
        this.vs = Vs.create(this.unique, this.metadata.types());
    }

    // ------------ 基础模型部分，对接内置模型处理抽象层基础实现 ------------
    @Override
    public HArk ark() {
        return this.model().ark();
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
        attributes.forEach(name -> {
            final HMetaField attribute = this.metadata.type(name);
            final Class<?> type = Objects.isNull(attribute) ? String.class : attribute.type();
            result.put(name, type);
        });
        return result;
    }

    /** 返回 Shape 对象 */
    @Override
    public HMetaAtom shape() {
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
    public HRule ruleAtom() {
        return this.ruler.rule();
    }


    /** 智能检索规则 */
    @Override
    public HRule ruleSmart() {
        return this.ruler.ruleSmart();
    }

    /** 连接的规则 */
    @Override
    public HRule rule() {
        return this.ruler.ruleDirect();
    }

    /** 规则的链接 */
    @Override
    @SuppressWarnings("unchecked")
    public <T extends HAtom> T rule(final HRule channelRule) {
        this.ruler.connect(channelRule);
        return (T) this;
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
    public KMarkAtom marker() {
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
