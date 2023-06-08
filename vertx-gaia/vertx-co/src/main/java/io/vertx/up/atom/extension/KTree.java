package io.vertx.up.atom.extension;

import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KTree implements Serializable {
    private String in;
    private String out = KName.KEY;
    private String field = "parentId";
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject region = new JsonObject();

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

    public String getField() {
        return this.field;
    }

    public void setField(final String field) {
        this.field = field;
    }

    public JsonObject getRegion() {
        return this.region;
    }

    public void setRegion(final JsonObject region) {
        this.region = region;
    }

    public JsonObject region(final JsonObject parameters) {
        final JsonObject regionData = new JsonObject();
        Ut.<String>itJObject(this.region, (expr, field) -> {
            final String parsed = Ut.fromExpression(expr, parameters);
            if (Ut.isNotNil(parsed)) {
                regionData.put(field, parsed);
            }
        });
        return regionData;
    }
}
