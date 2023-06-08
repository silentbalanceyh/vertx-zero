package io.vertx.mod.workflow.atom.configuration;

import io.horizon.eon.VString;
import io.horizon.uca.cache.Cc;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.workflow.refine.Wf;
import io.vertx.mod.workflow.uca.modeling.Respect;
import io.vertx.mod.workflow.uca.modeling.RespectLink;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.io.Serializable;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class ConfigLinkage implements Serializable {
    private final static Cc<String, Respect> CC_RESPECT = Cc.open();

    private final transient ConcurrentMap<String, Class<?>> respectMap = new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, JsonObject> queryMap = new ConcurrentHashMap<>();

    ConfigLinkage(final JsonObject linkageJ) {
        /*
         * field: {
         *      "disabled": "UI Enable Or Not"
         *      "config": {
         *          "respect": "default is linkage dao"
         *      },
         *      "message": {
         *      },
         *      "editor": {
         *          "tree": ...,
         *          "initial": ...,
         *          "search": ...,
         *          "ajax"
         *      },
         *      "table": {
         *      }
         * }
         */
        final JsonObject parsedJ = Wf.outLinkage(linkageJ);
        Ut.<JsonObject>itJObject(parsedJ, (json, field) -> {
            final JsonObject config = Ut.valueJObject(json, KName.CONFIG);

            if (Ut.isNotNil(config)) {
                /*
                 * First Map
                 *
                 * field = Dao
                 */
                final Class<?> clazz = Ut.clazz(config.getString("respect"), null);
                if (Objects.isNull(clazz)) {
                    this.respectMap.put(field, RespectLink.class);
                } else {
                    this.respectMap.put(field, clazz);
                }


                /*
                 * Second Map
                 */
                if (this.respectMap.containsKey(field)) {
                    final JsonObject query = Ut.valueJObject(config, KName.QUERY);
                    query.put(VString.EMPTY, Boolean.TRUE);
                    this.queryMap.put(field, query);
                }
            }
        });
    }

    public Set<String> fields() {
        return this.respectMap.keySet();
    }

    public Respect respect(final String field) {
        /*
         * HashCode
         * 1. Respect Class Name
         * 2. Condition ( Hash Code )
         * 3. Field Name
         */
        final JsonObject query = this.queryMap.getOrDefault(field, new JsonObject());
        final Class<?> respectCls = this.respectMap.get(field);
        final String hashKey = Ut.encryptMD5(field + query.hashCode() + respectCls.getName());
        return CC_RESPECT.pick(() -> Ut.instance(respectCls, query), hashKey);
        // Fn.p?ol(POOL_RESPECT, hashKey, () -> Ut.instance(respectCls, query));
    }
}
