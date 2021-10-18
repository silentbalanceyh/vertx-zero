package io.vertx.tp.modular.apply;

import cn.vertxup.atom.domain.tables.pojos.MModel;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.eon.KName;

class JoinDefault implements AoDefault {

    private transient MModel model;

    @Override
    public <T> AoDefault mount(final T mounted) {
        if (mounted instanceof MModel) {
            this.model = (MModel) mounted;
        }
        return this;
    }

    @Override
    public void applyJson(final JsonObject join) {
        if (null != this.model) {
            join.put(KName.NAMESPACE, this.model.getNamespace());
            /*
             * entityKey：默认 key
             * model: 当前model
             * namespace：（重写成当前模型的）
             */
            AoDefault.apply(join, KName.MODEL, this.model.getIdentifier());
            AoDefault.apply(join, "entityKey", KName.KEY);
            Ao.debugUca(this.getClass(), "「DFT」连接Join值: {0}", join.encode());
        } else {
            Ao.debugAtom(this.getClass(), "[OxE] 模型为空！");
        }
    }
}
