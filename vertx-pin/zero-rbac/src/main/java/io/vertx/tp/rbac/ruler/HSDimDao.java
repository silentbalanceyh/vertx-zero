package io.vertx.tp.rbac.ruler;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

public class HSDimDao extends HSDimNorm {
    @Override
    protected Future<JsonArray> compile(final HPermit input, final Class<?> daoCls, final JsonObject config) {
        /*
         * Dao 专用维度转换器，执行读取构造新的维度数据用于菜单
         * 输出格式:
         * -- key
         * -- label
         * -- value
         * -- data 这个节点是非静态模式必须带的数据节点，用于前端做维度计算
         */
        return super.compile(input, daoCls, config);
    }
}