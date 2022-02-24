package io.vertx.tp.workflow.atom;

import io.vertx.core.json.JsonObject;
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
    private final static String CLS_DAO = "cn.vertxup.ambient.domain.tables.daos.XLinkageDao";

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
                final String clazzCls = config.containsKey(KName.DAO) ? config.getString(KName.DAO) : CLS_DAO;
                final Class<?> clazz = Ut.clazz(clazzCls, null);
                if (Objects.nonNull(clazz)) {
                    this.daoMap.put(field, clazz);
                }


                /*
                 * Second Map
                 */
                if (this.daoMap.containsKey(field)) {
                    final JsonObject query = Ut.sureJObject(config, KName.QUERY);
                    this.queryMap.put(field, query);
                }
            }
        });
    }

    public Set<String> fields() {
        return this.daoMap.keySet();
    }

    public JsonObject condition(final String field) {
        final JsonObject conditionJ = this.queryMap.getOrDefault(field, new JsonObject());
        conditionJ.put(Strings.EMPTY, Boolean.TRUE);
        return conditionJ.copy();
    }

    boolean isDefault(final String field) {
        final Class<?> clazz = this.daoMap.getOrDefault(field, null);
        if (Objects.isNull(clazz)) {
            return false;
        }
        return CLS_DAO.equals(clazz.getName());
    }
}
