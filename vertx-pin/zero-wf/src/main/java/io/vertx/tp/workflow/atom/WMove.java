package io.vertx.tp.workflow.atom;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WMove implements Serializable {
    private final transient ConcurrentMap<String, WMoveRule> rules = new ConcurrentHashMap();
    /*
     * The data structure is as following:
     * {
     *     "node": {
     *         "data": {
     *         },
     *         "rule": [
     *             WMoveRule,
     *             WMoveRule
     *         ]
     *     }
     * }
     */
    private transient String node;
    private transient ConcurrentMap<String, String> data = new ConcurrentHashMap<>();

    private WMove(final String node, final JsonObject config) {
        // Node Name
        this.node = node;
        // Config for Camunda Engine
        final JsonObject data = Ut.sureJObject(config.getJsonObject(KName.DATA));
        Ut.<String>itJObject(data, (value, field) -> data.put(field, value));

        // Processing for left rules
        final JsonArray rules = Ut.sureJArray(config.getJsonArray(KName.RULE));
        Ut.itJArray(rules).forEach(json -> {
            final WMoveRule rule = Ux.fromJson(json, WMoveRule.class);
            if (rule.valid()) {
                this.rules.put(rule.key(), rule);
            } else {
                Wf.Log.warnMove(this.getClass(), "Rule invalid: {0}", rule.toString());
            }
        });
    }

    public static WMove create(final String node, final JsonObject config) {
        return new WMove(node, config);
    }

    public static WMove empty() {
        return new WMove(null, new JsonObject());
    }

    public String getNode() {
        return this.node;
    }

    public void setNode(final String node) {
        this.node = node;
    }

    public ConcurrentMap<String, String> getData() {
        return this.data;
    }

    public void setData(final ConcurrentMap<String, String> data) {
        this.data = data;
    }

    public WMoveRule rule(final String field, final Object value) {
        final String key = field + Strings.EQUAL + value;
        return this.rules.getOrDefault(key, null);
    }
}
