package io.vertx.up.experiment.shape;

import io.vertx.core.json.JsonObject;
import io.vertx.up.experiment.mixture.HAttribute;
import io.vertx.up.experiment.mixture.HModel;
import io.vertx.up.experiment.mixture.HReference;
import io.vertx.up.experiment.mu.KMarker;
import io.vertx.up.experiment.mu.KTag;
import io.vertx.up.experiment.rule.RuleUnique;
import io.vertx.up.experiment.specification.request.KApp;
import io.vertx.up.util.Ut;

import java.util.Objects;
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
    protected transient KApp app;
    // The identifier of uniform model
    protected String identifier;
    // The bind json file
    protected String jsonFile;
    // Unique Rule
    protected RuleUnique unique;
    // Marker for
    protected KMarker marker;

    protected HReference reference;

    public AbstractHModel(final KApp app) {
        this.app = app;
    }

    // ================ Basic Method Api ==================
    @Override
    public String identifier() {
        return this.identifier;
    }

    @Override
    public String file() {
        return this.jsonFile;
    }

    @Override
    public String namespace() {
        return this.app.ns();
    }

    @Override
    public KMarker tag() {
        return this.marker;
    }

    @Override
    public KApp app() {
        return this.app;
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
        final JsonObject data = Ut.ioJObject(this.jsonFile);
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
    public RuleUnique rule() {
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

    protected abstract RuleUnique loadRule();

    protected HReference loadReference() {
        return new HAtomReference(this.app);
    }

    protected boolean trackable() {
        // Default track = true;
        return Boolean.TRUE;
    }

    private KMarker loadMarker() {
        final KMarker marker = new KMarker(this.trackable());
        this.attribute().forEach(name -> {
            final HAttribute attribute = this.attribute(name);
            if (Objects.nonNull(attribute)) {
                final KTag tag = attribute.tag();
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
        if (!(o instanceof AbstractHModel)) {
            return false;
        }
        final AbstractHModel that = (AbstractHModel) o;
        return Objects.equals(this.identifier, that.identifier) &&
            Objects.equals(this.app, that.app);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.app, this.identifier);
    }
}
