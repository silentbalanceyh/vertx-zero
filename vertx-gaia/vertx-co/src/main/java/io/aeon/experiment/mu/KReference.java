package io.aeon.experiment.mu;

import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KReference implements Serializable {
    private final JsonObject sourceReference = new JsonObject();
    private String name;
    private String source;
    private String sourceField;

    public KReference name(final String name) {
        this.name = name;
        return this;
    }

    public String name() {
        return this.name;
    }

    public KReference source(final String source) {
        this.source = source;
        return this;
    }

    public String source() {
        return this.source;
    }

    public KReference sourceField(final String sourceField) {
        this.sourceField = sourceField;
        return this;
    }

    public String sourceField() {
        return this.sourceField;
    }

    public KReference sourceReference(final JsonObject sourceReference) {
        final JsonObject valued = Ut.valueJObject(sourceReference);
        this.sourceReference.mergeIn(valued, true);
        return this;
    }

    public KReference sourceReference(final String sourceReference) {
        final JsonObject config = Ut.toJObject(sourceReference);
        if (Ut.isNotNil(config)) {
            this.sourceReference(config);
        }
        return this;
    }

    public JsonObject sourceReference() {
        return this.sourceReference;
    }

    public boolean isReference() {
        return Ut.isNotNil(this.sourceReference);
    }

    @Override
    public String toString() {
        return "KReference{" +
            "sourceReference=" + this.sourceReference +
            ", name='" + this.name + '\'' +
            ", source='" + this.source + '\'' +
            ", sourceField='" + this.sourceField + '\'' +
            '}';
    }
}
