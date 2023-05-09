package io.aeon.experiment.shape.internal;

import io.aeon.experiment.mu.KClass;
import io.aeon.experiment.mu.KHybrid;
import io.aeon.experiment.shape.AbstractHModel;
import io.horizon.exception.web._501NotSupportException;
import io.modello.atom.app.KApp;
import io.modello.eon.em.ModelType;
import io.modello.specification.atom.HAttribute;
import io.modello.specification.atom.HUnique;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NormModel extends AbstractHModel {

    private final KClass kClass;
    private final KHybrid hybrid;

    public NormModel(final KApp app, final String identifier) {
        super(app);
        this.kClass = KClass.create(app, identifier, true);
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
    protected HUnique loadRule() {
        return Objects.requireNonNull(this.hybrid).rule();
    }
}
