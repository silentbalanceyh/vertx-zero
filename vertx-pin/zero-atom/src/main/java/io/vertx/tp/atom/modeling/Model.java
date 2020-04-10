package io.vertx.tp.atom.modeling;

import cn.vertxup.atom.domain.tables.pojos.MAttribute;
import cn.vertxup.atom.domain.tables.pojos.MJoin;
import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.em.ModelType;
import io.vertx.tp.atom.modeling.rule.RuleUnique;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.optic.modeling.JsonModel;

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

    MModel getModel();

    Set<MJoin> getJoins();

    Set<MAttribute> getAttributes();

    MAttribute getAttribute(String attributeName);

    Set<Schema> getSchemata();

    ModelType getType();

    Schema getSchema(String identifier);

    RuleUnique getUnique();

    /*
     * 特殊时间格式
     */
    ConcurrentMap<String, Class<?>> types();
}

