package io.vertx.tp.workflow.atom.runtime;

import cn.zeroup.macrocosm.cv.em.MoveMode;
import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WMoveWay implements Serializable {

    private MoveMode type = MoveMode.Standard;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject config = new JsonObject();

    public MoveMode getType() {
        return this.type;
    }

    public void setType(final MoveMode type) {
        this.type = type;
    }

    public JsonObject getConfig() {
        return this.config;
    }

    public void setConfig(final JsonObject config) {
        this.config = config;
    }
}
