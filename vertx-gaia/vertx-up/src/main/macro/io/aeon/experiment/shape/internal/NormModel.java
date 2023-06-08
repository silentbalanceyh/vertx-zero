package io.aeon.experiment.shape.internal;

import io.aeon.experiment.mu.KClass;
import io.aeon.experiment.mu.KHybrid;
import io.aeon.experiment.shape.AbstractHModel;
import io.horizon.exception.web._501NotSupportException;
import io.macrocosm.specification.program.HArk;
import io.modello.eon.em.EmModel;
import io.modello.specification.atom.HAttribute;
import io.modello.specification.atom.HRule;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NormModel extends AbstractHModel {

    private final KClass kClass;
    private final KHybrid hybrid;

    public NormModel(final HArk ark, final String identifier) {
        super(ark);
        this.kClass = KClass.create(ark, identifier, true);
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
    public EmModel.Type type() {
        return EmModel.Type.READONLY;
    }

    @Override
    protected ConcurrentMap<String, HAttribute> loadAttribute() {
        return Objects.requireNonNull(this.hybrid).attribute();
    }

    @Override
    protected HRule loadRule() {
        return Objects.requireNonNull(this.hybrid).rule();
    }
}
