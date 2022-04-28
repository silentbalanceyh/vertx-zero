package io.vertx.up.experiment.shape.internal;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.em.atom.ModelType;
import io.vertx.up.experiment.shape.AbstractHModel;
import io.vertx.up.experiment.specification.KClass;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NormModel extends AbstractHModel {

    private final KClass kClass;

    public NormModel(final String namespace, final String identifier) {
        super(namespace);
        this.kClass = KClass.create(namespace, identifier);
    }

    @Override
    public JsonObject toJson() {
        return null;
    }

    @Override
    public void fromJson(final JsonObject json) {

    }

    @Override
    public ModelType type() {
        return null;
    }

    @Override
    protected void loadAttribute() {

    }

    @Override
    protected void loadRule() {

    }
}
