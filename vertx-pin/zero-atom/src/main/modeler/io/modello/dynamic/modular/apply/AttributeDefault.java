package io.modello.dynamic.modular.apply;

import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.modello.eon.em.EmAttribute;
import io.vertx.core.json.JsonObject;

import static io.vertx.mod.atom.refine.Ao.LOG;

class AttributeDefault implements AoDefault {

    private transient MModel model;

    @Override
    public <T> AoDefault mount(final T mounted) {
        if (mounted instanceof MModel) {
            this.model = (MModel) mounted;
        }
        return this;
    }

    @Override
    public void applyJson(final JsonObject attribute) {
        if (null != this.model) {
            LOG.Uca.debug(this.getClass(), "「DFT」模型属性输入值：{0}", attribute.encode());
            /*
             * 默认值:
             * key
             * type
             * active
             * source：为1有默认值，如果为2必须包含
             * sourceField：没指定则和 name 相同
             * language
             * metadata
             */
            AoDefault.apply(attribute, "sourceField", attribute.getValue("name"));
            AoDefault.apply(attribute, "type", EmAttribute.Type.INTERNAL.name());
            AoDefault.apply(attribute, "modelId", this.model.getKey());
            AoDefault.apply(attribute);
        }
    }
}
