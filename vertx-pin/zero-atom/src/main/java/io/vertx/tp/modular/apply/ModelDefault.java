package io.vertx.tp.modular.apply;

import io.horizon.constant.em.modeler.ModelType;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.eon.KName;

class ModelDefault implements AoDefault {

    @Override
    public void applyJson(final JsonObject model) {
        Ao.debugUca(this.getClass(), "「DFT」模型输入值: {0}", model.encode());
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
