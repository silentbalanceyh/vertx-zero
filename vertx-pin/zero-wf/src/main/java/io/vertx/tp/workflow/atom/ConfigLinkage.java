package io.vertx.tp.workflow.atom;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.uca.modeling.Respect;
import io.vertx.tp.workflow.uca.modeling.RespectLink;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.fn.Fn;
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

    private final static ConcurrentMap<String, Respect> POOL_RESPECT = new ConcurrentHashMap<>();

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
        Ut.<JsonObject>itJObject(linkageJ, (json, field) -> {
            final JsonObject config = Ut.valueJObject(json, KName.CONFIG);

            if (Ut.notNil(config)) {
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
                    query.put(Strings.EMPTY, Boolean.TRUE);
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
        return Fn.pool(POOL_RESPECT, hashKey, () -> Ut.instance(respectCls, query));
    }
}
