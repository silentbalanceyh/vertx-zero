package io.vertx.tp.atom.modeling;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.em.ModelType;
import io.vertx.tp.atom.modeling.config.AoAttribute;
import io.vertx.tp.atom.modeling.element.DataKey;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.optic.modeling.JsonModel;
import io.vertx.up.commune.Json;
import io.vertx.up.commune.element.TypeField;
import io.vertx.up.commune.rule.RuleUnique;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

public interface Model extends AoShared, AoConnect {

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
     * 2. AoAttribute
     * 3. RuleUnique
     */
    Set<Schema> schemata();

    Schema schema(String identifier);

    RuleUnique unique();

    AoAttribute attribute(String attributeName);

    /*
     * The Api for java class type directly
     */
    ModelType type();

    ConcurrentMap<String, Class<?>> typeCls();

    ConcurrentMap<String, TypeField> types();
}

/**
 * 单名空间
 */
interface AoConnect extends AoRelation {

    /* 从Json中连接Schema：会针对joins做过滤 **/
    Model bind(Set<Schema> schemas);

    /* 从数据库中连接Schema：不考虑joins，直接连接 **/
    void bindDirect(Set<Schema> schemas);

    DataKey key();

    void key(DataKey dataKey);
}

interface AoShared extends Serializable, Json {

    String identifier();

    String file();

    String namespace();
}

interface AoRelation {
    /* 直接针对Model设置主键信息 */
    void relation(final String key);
}

