package io.vertx.up.commune.exchange;

import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Copyable;
import io.vertx.up.commune.Json;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * #「Co」Directory Consumer
 *
 * Zero Atom Usage for directory consuming, but there exist critical points that you must **understand**:
 *
 * 1. Each field should have one unique meaning when it stored into ox engine as model's attribute, it means that you could
 * not use the same name to identify different business concept.
 * 2. The field definition must be same in different model when it connect to directory, all the definition of this
 * attribute must be the same.
 *
 * For example
 *
 * Situation 1: ( Correct )
 *
 * The field `email` means email in `USER` model, when it belong to `EMPLOYEE` model, it means `email` only, if you want
 * to add working email into `EMPLOYEE` and it's different from `email`, you could not use `email` field but another
 * different one.
 *
 * Situation 2: ( Wrong )
 * The field `zone` consuming directory in your NETWORK model, it means the first level zone, in another model SERVER,
 * the field `zone` means the second level zone, they are different concept in your system, the solution should be
 * rename `zone` to `zonesub` or other field name to identify different concept.
 *
 * In total
 *
 * You should focus on RULE 4:
 *
 * 1. When the field names are the same, it must be the same concept.
 * 2. When the field names are different, it could be the same concept.
 * 3. When the field names are different, it should be different concept.
 * 4. 「Limitation」When the field names are the same, it mustn't be different concept.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DiConsumer implements Serializable, Json, Copyable<DiConsumer> {

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

    @Override
    public DiConsumer copy() {
        final DiConsumer consumer = new DiConsumer();
        final JsonObject data = this.toJson().copy();
        consumer.fromJson(data);
        return consumer;
    }

    public boolean isValid() {
        return Ut.notNil(this.in) && Ut.notNil(this.out) && Ut.notNil(this.source);
    }
}
