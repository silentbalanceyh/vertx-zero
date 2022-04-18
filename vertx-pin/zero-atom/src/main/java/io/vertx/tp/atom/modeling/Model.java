package io.vertx.tp.atom.modeling;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.element.DataKey;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.optic.modeling.JsonModel;
import io.vertx.up.experiment.meld.HApp;
import io.vertx.up.experiment.meld.HLinkage;
import io.vertx.up.experiment.meld.HModel;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public interface Model extends HApp, HModel, AoConnect {

    // 下边是静态方法
    static String namespace(final String appName) {
        return Ao.toNamespace(appName);
    }

    static Model instance(final String namespace, final JsonObject data) {
        final Model model = new JsonModel(namespace);
        model.fromJson(data);
        return model;
    }

    /*
     * The API for database pojo directly such as
     *
     * 1. MModel
     * 2. MJoin
     * 3. MAttribute
     */
    MModel dbModel();

    Set<MJoin> dbJoins();

    Set<MAttribute> dbAttributes();

    MAttribute dbAttribute(String attributeName);

    /*
     * The Api for defined modeling interface
     * Such as
     *
     * 1. Schema
     * 2. HAttribute
     * 3. RuleUnique
     */
    Set<Schema> schema();

    Schema schema(String identifier);

    ConcurrentMap<String, Class<?>> typeMap();
}

/**
 * 单名空间
 */
interface AoConnect extends HLinkage {

    /* 从Json中连接Schema：会针对joins做过滤 **/
    Model bind(Set<Schema> schemas);

    /* 从数据库中连接Schema：不考虑joins，直接连接 **/
    void bindDirect(Set<Schema> schemas);

    DataKey key();

    void key(DataKey dataKey);
}

