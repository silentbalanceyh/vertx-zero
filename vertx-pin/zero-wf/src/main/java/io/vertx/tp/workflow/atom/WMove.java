package io.vertx.tp.workflow.atom;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.refine.Wf;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.uca.sectio.AspectConfig;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class WMove implements Serializable {
    private final transient ConcurrentMap<String, WMoveRule> rules = new ConcurrentHashMap<>();
    private final transient JsonObject params = new JsonObject();
    /*
     * The data structure is as following:
     * {
     *     "node": {
     *         "data": {
     *         },
     *         "rule": [
     *             WMoveRule,
     *             WMoveRule
     *         ],
     *         "aspect": AspectConfig,
     *         "fork": {
     *         }
     *     }
     * }
     *
     * 1) When `Standard` Mode, the `fork` configuration will not be used.
     * 2) When `Multi` Mode, the `fork` configuration will not be used.
     * 3) 「Valid」When `Fork/Join` Mode, the `fork` configuration will help to create Todo Tickets
     */
    private final transient String node;
    private final transient ConcurrentMap<String, String> data = new ConcurrentHashMap<>();
    private final transient AspectConfig aspect;

    private final transient JsonObject fork = new JsonObject();

    private WMove(final String node, final JsonObject config) {
        // Node Name
        this.node = node;
        // Config for Camunda Engine
        final JsonObject data = Ut.valueJObject(config.getJsonObject(KName.DATA));
        Ut.<String>itJObject(data, (value, field) -> this.data.put(field, value));

        // Processing for left rules
        final JsonArray rules = Ut.valueJArray(config.getJsonArray(KName.RULE));
        Ut.itJArray(rules).forEach(json -> {
            final WMoveRule rule = Ux.fromJson(json, WMoveRule.class);
            if (rule.valid()) {
                this.rules.put(rule.key(), rule);
            } else {
                Wf.Log.warnMove(this.getClass(), "Rule invalid: {0}", rule.toString());
            }
        });

        // Processing for Aspect
        this.aspect = AspectConfig.create(Ut.valueJObject(config, KName.ASPECT));

        // Processing for Fork
        this.fork.mergeIn(Ut.valueJObject(config, KName.FORK));
    }

    public static WMove create(final String node, final JsonObject config) {
        return new WMove(node, config);
    }

    public static WMove empty() {
        return new WMove(null, new JsonObject());
    }

    public WMove stored(final JsonObject request) {
        // Clean Params
        this.params.clear();
        this.data.forEach((to, from) -> this.params.put(to, request.getValue(from)));
        return this;
    }

    public WMoveRule ruleFind() {
        final Set<String> keys = new TreeSet<>();
        this.params.fieldNames().forEach(field -> {
            final Object value = this.params.getValue(field);
            if (Objects.nonNull(value)) {
                final String key = field + Strings.EQUAL + value;
                keys.add(key);
            }
        });
        final String key = Ut.fromJoin(keys);
        final WMoveRule rule = this.rules.getOrDefault(key, null);
        Wf.Log.infoMove(this.getClass(), "[ Rule ] The node `{0}` rule processed: {1}", this.node, rule);
        return rule;
    }

    public AspectConfig configAop() {
        return this.aspect;
    }

    public JsonObject parameters() {
        return this.params;
    }

    public JsonObject fork() {
        return this.fork;
    }
}
