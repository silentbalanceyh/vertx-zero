package io.vertx.tp.ke.atom.specification;

import com.fasterxml.jackson.databind.JsonObjectDeserializer;
import com.fasterxml.jackson.databind.JsonObjectSerializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.exchange.DiConsumer;
import io.vertx.up.commune.exchange.DiSetting;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KTransform implements Serializable {
    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject fabric;

    private KTree tree;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject initial;

    @JsonSerialize(using = JsonObjectSerializer.class)
    @JsonDeserialize(using = JsonObjectDeserializer.class)
    private JsonObject mapping;

    public JsonObject getFabric() {
        return this.fabric;
    }

    public void setFabric(final JsonObject fabric) {
        this.fabric = fabric;
    }

    public KTree getTree() {
        return this.tree;
    }

    public void setTree(final KTree tree) {
        this.tree = tree;
    }

    public JsonObject getInitial() {
        return Ut.sureJObject(this.initial);
    }

    public void setInitial(final JsonObject initial) {
        this.initial = initial;
    }

    public JsonObject getMapping() {
        return Ut.sureJObject(this.mapping);
    }

    public void setMapping(final JsonObject mapping) {
        this.mapping = mapping;
    }

    public ConcurrentMap<String, DiConsumer> epsilon() {
        final JsonObject dictionary = Ut.sureJObject(this.fabric);
        return Ux.dictEpsilon(Ut.sureJObject(dictionary.getJsonObject(KName.EPSILON)));
    }

    public DiSetting source() {
        final JsonObject dictionary = Ut.sureJObject(this.fabric);
        return new DiSetting(Ut.sureJArray(dictionary.getJsonArray(KName.SOURCE)));
    }
}
