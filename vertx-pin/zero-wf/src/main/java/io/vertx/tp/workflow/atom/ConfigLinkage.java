package io.vertx.tp.workflow.atom;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.workflow.uca.modeling.Respect;
import io.vertx.tp.workflow.uca.modeling.RespectFile;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
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

    private final transient ConcurrentMap<String, Class<?>> daoMap = new ConcurrentHashMap<>();
    private final transient ConcurrentMap<String, JsonObject> queryMap = new ConcurrentHashMap<>();

    ConfigLinkage(final JsonObject linkageJ) {
        /*
         * field: {
         *      "disabled": "UI Enable Or Not"
         *      "config": {
         *          "dao": "default is linkage dao"
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
            final JsonObject config = Ut.sureJObject(json, KName.CONFIG);

            if (Ut.notNil(config)) {
                /*
                 * First Map
                 *
                 * field = Dao
                 */
                final Class<?> clazz = Ut.clazz(config.getString("respect"), null);
                if (Objects.isNull(clazz)) {
                    this.daoMap.put(field, RespectFile.class);
                } else {
                    this.daoMap.put(field, clazz);
                }


                /*
                 * Second Map
                 */
                if (this.daoMap.containsKey(field)) {
                    final JsonObject query = Ut.sureJObject(config, KName.QUERY);
                    query.put(Strings.EMPTY, Boolean.TRUE);
                    this.queryMap.put(field, query);
                }
            }
        });
    }

    public Set<String> fields() {
        return this.daoMap.keySet();
    }

    public JsonObject query(final String field) {
        final JsonObject conditionJ = this.queryMap.getOrDefault(field, new JsonObject());
        conditionJ.put(Strings.EMPTY, Boolean.TRUE);
        return conditionJ.copy();
    }

    public Respect respect(final String field) {
        /*
         * HashCode
         * 1. Respect Class Name
         * 2. Condition ( Hash Code )
         * 3. Field Name
         */
        return null;
    }
}
