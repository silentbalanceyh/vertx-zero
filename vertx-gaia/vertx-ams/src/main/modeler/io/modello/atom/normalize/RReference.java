package io.modello.atom.normalize;

import io.horizon.util.HUt;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class RReference implements Serializable {
    private final JsonObject sourceReference = new JsonObject();
    private String name;
    private String source;
    private String sourceField;

    public RReference name(final String name) {
        this.name = name;
        return this;
    }

    public String name() {
        return this.name;
    }

    public RReference source(final String source) {
        this.source = source;
        return this;
    }

    public String source() {
        return this.source;
    }

    public RReference sourceField(final String sourceField) {
        this.sourceField = sourceField;
        return this;
    }

    public String sourceField() {
        return this.sourceField;
    }

    public RReference sourceReference(final JsonObject sourceReference) {
        final JsonObject valued = HUt.valueJObject(sourceReference);
        this.sourceReference.mergeIn(valued, true);
        return this;
    }

    public RReference sourceReference(final String sourceReference) {
        final JsonObject config = HUt.toJObject(sourceReference);
        if (HUt.isNotNil(config)) {
            this.sourceReference(config);
        }
        return this;
    }

    public JsonObject sourceReference() {
        return this.sourceReference;
    }

    public boolean isReference() {
        return HUt.isNotNil(this.sourceReference);
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
