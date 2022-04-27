package io.vertx.up.experiment.shape;

import io.vertx.core.json.JsonObject;
import io.vertx.up.experiment.mixture.HAttribute;
import io.vertx.up.experiment.mixture.HModel;
import io.vertx.up.experiment.rule.RuleUnique;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractHModel implements HModel {
    // The namespace of current model
    protected final transient String namespace;
    // HAttribute Map
    protected final ConcurrentMap<String, HAttribute> attributeMap = new ConcurrentHashMap<>();
    // The identifier of uniform model
    protected String identifier;
    // The bind json file
    protected String jsonFile;
    // Unique Rule
    protected RuleUnique unique;

    public AbstractHModel(final String namespace) {
        this.namespace = namespace;
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
        return this.namespace;
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
        this.loadAttribute();
        return this.attributeMap.getOrDefault(attributeName, null);
    }

    @Override
    public Set<String> attribute() {
        this.loadAttribute();
        return this.attributeMap.keySet();
    }

    @Override
    public RuleUnique rule() {
        if (Objects.isNull(this.unique)) {
            this.loadRule();
        }
        return this.unique;
    }

    protected abstract void loadAttribute();

    protected abstract void loadRule();
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
            Objects.equals(this.namespace, that.namespace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.namespace, this.identifier);
    }
}
