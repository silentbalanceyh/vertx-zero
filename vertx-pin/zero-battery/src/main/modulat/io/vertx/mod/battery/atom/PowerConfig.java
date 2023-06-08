package io.vertx.mod.battery.atom;

import com.fasterxml.jackson.databind.JsonArrayDeserializer;
import com.fasterxml.jackson.databind.JsonArraySerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonArray;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PowerConfig implements Serializable {

    @JsonSerialize(using = JsonArraySerializer.class)
    @JsonDeserialize(using = JsonArrayDeserializer.class)
    private transient JsonArray builtIn = new JsonArray();

    public JsonArray getBuiltIn() {
        return this.builtIn;
    }

    public void setBuiltIn(final JsonArray builtIn) {
        this.builtIn = builtIn;
    }

    public Set<String> buildIn() {
        return Ut.toSet(this.builtIn);
    }
}
