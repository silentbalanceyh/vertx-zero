package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.experiment.mixture.HAtom;

public abstract class AbstractSDim implements HSDim{
    protected transient HAtom atom;

    @Override
    public HSDim bind(HAtom atom) {
        this.atom = atom;
        return this;
    }

    @Override
    public Future<JsonObject> configure(HPermit input) {
        /*
         * 提取 dmConfig 字段的数据，解析 items 内容，执行维度数据源的提取
         * - items 为 JsonArray
         * - items 为 JsonObject（三种模式）
         */
        return HSDim.super.configure(input);
    }
}
