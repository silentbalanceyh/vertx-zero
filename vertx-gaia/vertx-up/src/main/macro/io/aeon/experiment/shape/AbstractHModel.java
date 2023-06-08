package io.aeon.experiment.shape;

import io.horizon.util.HUt;
import io.macrocosm.specification.program.HArk;
import io.modello.atom.normalize.KMarkAtom;
import io.modello.atom.normalize.KMarkAttribute;
import io.modello.specification.atom.HAttribute;
import io.modello.specification.atom.HModel;
import io.modello.specification.atom.HReference;
import io.modello.specification.atom.HRule;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractHModel implements HModel {
    // HAttribute Map
    protected final ConcurrentMap<String, HAttribute> attributeMap = new ConcurrentHashMap<>();
    // The namespace of current model
    protected transient HArk ark;
    // The identifier of uniform model
    protected String identifier;
    // The bind json file
    protected String jsonFile;
    // Unique Rule
    protected HRule unique;
    // Marker for
    protected KMarkAtom marker;

    protected HReference reference;

    public AbstractHModel(final HArk ark) {
        this.ark = ark;
    }

    // ================ Basic Method Api ==================
    @Override
    public String identifier() {
        return this.identifier;
    }

    @Override
    public String resource() {
        return this.jsonFile;
    }

    @Override
    public String namespace() {
        return Optional.ofNullable(this.ark.app()).orElseThrow().ns();
    }

    @Override
    public KMarkAtom marker() {
        return this.marker;
    }

    @Override
    public HArk ark() {
        return this.ark;
    }

    @Override
    public HReference reference() {
        return this.reference;
    }

    // ================= Attribute Part ===================
    @Override
    public void fromFile(final String file) {
        // Model会关心文件路径，所以这里需要这个操作
        this.jsonFile = file;
        final JsonObject data = HUt.ioJObject(this.jsonFile);
        this.fromJson(data);
    }

    @Override
    public HAttribute attribute(final String attributeName) {
        return this.attributeMap.getOrDefault(attributeName, null);
    }

    @Override
    public Set<String> attribute() {
        return this.attributeMap.keySet();
    }

    @Override
    public HRule rule() {
        if (Objects.isNull(this.unique)) {
            this.loadRule();
        }
        return this.unique;
    }

    // ======================= Sub Class Initialize -==========================

    protected void initialize() {
        // Attribute Load
        this.attributeMap.clear();
        this.attributeMap.putAll(this.loadAttribute());
        // RuleUnique Load
        this.unique = this.loadRule();
        // Marker Load
        this.marker = this.loadMarker();
        // Reference Load
        this.reference = this.loadReference();
    }

    protected abstract ConcurrentMap<String, HAttribute> loadAttribute();

    protected abstract HRule loadRule();

    protected HReference loadReference() {
        return new HAtomReference(this.ark);
    }

    protected boolean trackable() {
        // Default track = true;
        return Boolean.TRUE;
    }

    private KMarkAtom loadMarker() {
        final KMarkAtom marker = KMarkAtom.of(this.trackable());
        this.attribute().forEach(name -> {
            final HAttribute attribute = this.attribute(name);
            if (Objects.nonNull(attribute)) {
                final KMarkAttribute tag = attribute.marker();
                marker.put(name, tag);
            }
        });
        return marker;
    }
    // ===================== Equal / Hash Part =====================

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof final AbstractHModel that)) {
            return false;
        }
        return Objects.equals(this.identifier, that.identifier) &&
            Objects.equals(this.ark, that.ark);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.ark, this.identifier);
    }
}
