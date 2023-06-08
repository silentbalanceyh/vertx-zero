package io.vertx.mod.rbac.ruler;

import io.aeon.atom.secure.KPermit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.rbac.ruler.element.HAdmitCompiler;

public class HSDimArea extends HSDimNorm {
    @Override
    @SuppressWarnings("all")
    protected Future<JsonArray> compile(final KPermit permit, final JsonObject qrJ, final JsonObject config) {
        /*
         * Dao 专用维度转换器，执行读取构造新的维度数据用于菜单
         * 输出格式:
         * -- key
         * -- label
         * -- value
         * -- data 这个节点是非静态模式必须带的数据节点，用于前端做维度计算
         */
        /* 提取类型和参数 */
        final HAdmitCompiler compiler = HAdmitCompiler.create(permit, this.getClass());
        /*
         * 注意格式
         * qrJ -> 查询条件（解析之后的）
         * config -> {
         *     "dao": "xxx"
         * }
         * 此处 dao 属性是必备属性
         */
        return compiler.ingest(qrJ, config);
    }
}
