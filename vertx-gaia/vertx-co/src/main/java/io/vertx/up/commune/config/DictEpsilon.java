package io.vertx.up.commune.config;

import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Json;
import io.vertx.up.util.Ut;

import java.io.Serializable;

public class DictEpsilon implements Serializable, Json {

    private transient String source;
    private transient String in;
    private transient String out;
    private transient boolean parent;

    public String getSource() {
        return this.source;
    }

    public void setSource(final String source) {
        this.source = source;
    }

    public String getIn() {
        return this.in;
    }

    public void setIn(final String in) {
        this.in = in;
    }

    public String getOut() {
        return this.out;
    }

    public void setOut(final String out) {
        this.out = out;
    }

    public boolean getParent() {
        return this.parent;
    }

    public void setParent(final boolean parent) {
        this.parent = parent;
    }

    @Override
    public JsonObject toJson() {
        return Ut.serializeJson(this);
    }

    @Override
    public String toString() {
        return "DictEpsilon{" +
                "source='" + this.source + '\'' +
                ", in='" + this.in + '\'' +
                ", out='" + this.out + '\'' +
                ", parent=" + this.parent +
                '}';
    }

    @Override
    public void fromJson(final JsonObject json) {
        if (Ut.notNil(json)) {
            this.source = json.getString("source");
            this.in = json.getString("in");
            this.out = json.getString("out");
            if (json.containsKey("parent")) {
                this.parent = json.getBoolean("parent");
            } else {
                /*
                 * Not configured, it means current dict should be not Self reference
                 */
                this.parent = false;
            }
        }
    }

    public boolean isValid() {
        return Ut.notNil(this.in) && Ut.notNil(this.out) && Ut.notNil(this.source);
    }
}
