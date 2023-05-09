package io.vertx.tp.modular.apply;

import io.modello.eon.em.ModelType;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;

import static io.vertx.tp.atom.refine.Ao.LOG;

class ModelDefault implements AoDefault {

    @Override
    public void applyJson(final JsonObject model) {
        LOG.Uca.debug(this.getClass(), "「DFT」模型输入值: {0}", model.encode());
        /*
         * 默认值：
         * key
         * keyField: 默认key
         * type
         * active
         * language
         * metadata
         */
        AoDefault.apply(model, "type", ModelType.DIRECT.name());
        AoDefault.apply(model, "keyField", KName.KEY);
        AoDefault.apply(model);
    }
}
