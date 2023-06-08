package io.modello.dynamic.modular.phantom;

import io.horizon.spi.ambient.AoRefine;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.cv.AoCache;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

/**
 * 使用继承模式，方便做名字上的区分
 * Refine负责初始化的时候数据导入
 * Modeler负责在执行业务流程时的元数据读取
 */
public interface AoModeler extends AoRefine {

    static AoModeler init() {
        return AoCache.CC_MODELER.pick(InitModeler::new, InitModeler.class.getName());
        // return Fn.po?l(AoCache.POOL_MODELER, InitModeler.class.getName(), InitModeler::new);
    }

    static AoModeler attribute() {
        return AoCache.CC_MODELER.pick(AttributeModeler::new, AttributeModeler.class.getName());
        // return Fn.po?l(AoCache.POOL_MODELER, AttributeModeler.class.getName(), AttributeModeler::new);
    }

    static AoModeler join() {
        return AoCache.CC_MODELER.pick(JoinModeler::new, JoinModeler.class.getName());
        // return Fn.po?l(AoCache.POOL_MODELER, JoinModeler.class.getName(), JoinModeler::new);
    }

    static AoModeler entity() {
        return AoCache.CC_MODELER.pick(EntityModeler::new, EntityModeler.class.getName());
        // return Fn.po?l(AoCache.POOL_MODELER, EntityModeler.class.getName(), EntityModeler::new);
    }

    static AoModeler scatter() {
        return AoCache.CC_MODELER.pick(ScatterModeler::new, ScatterModeler.class.getName());
        // return Fn.po?l(AoCache.POOL_MODELER, ScatterModeler.class.getName(), ScatterModeler::new);
    }

    static AoModeler field() {
        return AoCache.CC_MODELER.pick(FieldModeler::new, FieldModeler.class.getName());
        // return Fn.po?l(AoCache.POOL_MODELER, FieldModeler.class.getName(), FieldModeler::new);
    }

    static AoModeler key() {
        return AoCache.CC_MODELER.pick(KeyModeler::new, KeyModeler.class.getName());
        // return Fn.po?l(AoCache.POOL_MODELER, KeyModeler.class.getName(), KeyModeler::new);
    }

    static AoModeler index() {
        return AoCache.CC_MODELER.pick(IndexModeler::new, IndexModeler.class.getName());
        // return Fn.po?l(AoCache.POOL_MODELER, IndexModeler.class.getName(), IndexModeler::new);
    }

    static JsonObject getEntity(final JsonObject schemaJson) {
        JsonObject entity = schemaJson.getJsonObject(KName.ENTITY);
        if (null == entity) {
            entity = new JsonObject();
        }
        return entity;
    }

    static JsonArray getSchemata(final JsonObject modelJson) {
        JsonArray schemata = modelJson.getJsonArray(KName.Modeling.SCHEMATA);
        if (null == schemata) {
            schemata = new JsonArray();
        }
        final JsonArray entities = new JsonArray();
        Ut.itJArray(schemata).forEach(entityJson -> entities.add(new JsonObject().put(KName.ENTITY, entityJson)));
        return entities;
    }

    JsonObject executor(JsonObject input);
}
