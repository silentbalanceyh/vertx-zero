package io.vertx.up.experiment.shape.internal;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.atom.ModelType;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.experiment.mixture.HAttribute;
import io.vertx.up.experiment.mu.KClass;
import io.vertx.up.experiment.mu.KHybrid;
import io.vertx.up.experiment.rule.RuleUnique;
import io.vertx.up.experiment.shape.AbstractHModel;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NormModel extends AbstractHModel {

    private final KClass kClass;
    private final KHybrid hybrid;

    public NormModel(final String namespace, final String identifier) {
        super(namespace);
        this.kClass = KClass.create(namespace, identifier);
        this.hybrid = this.kClass.hybrid();
        /*
         * Initialize
         * -- attributeMap
         * -- unique
         * -- marker
         * -- reference
         */
        this.initialize();
    }

    @Override
    public String identifier() {
        if (Objects.isNull(this.identifier)) {
            // identifier extract
            this.identifier = this.kClass.identifier();
        }
        return this.identifier;
    }

    @Override
    public JsonObject toJson() {
        throw new _501NotSupportException(this.getClass());
    }

    @Override
    public void fromJson(final JsonObject json) {
        throw new _501NotSupportException(this.getClass());
    }

    @Override
    public ModelType type() {
        return ModelType.READONLY;
    }

    @Override
    protected ConcurrentMap<String, HAttribute> loadAttribute() {
        return Objects.requireNonNull(this.hybrid).attribute();
    }

    @Override
    protected RuleUnique loadRule() {
        return Objects.requireNonNull(this.hybrid).rule();
    }
}
