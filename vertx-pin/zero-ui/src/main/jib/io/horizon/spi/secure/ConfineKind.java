package io.horizon.spi.secure;

import io.horizon.spi.modeler.Confine;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import static io.vertx.mod.ui.refine.Ui.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ConfineKind implements Confine {
    /*
     * 这个组件在 ConfineBuiltIn 中的数据结构多了一个维度
     * {
     *     "phase": "AFTER / BEFORE",
     *     "kind": "维度字段名，一般用于分类"
     *     "data": {
     *          "value": "seekKey"
     *     }
     * }
     * 该组件的 data 节点只会计算一个 seekKey，在 S_VISITANT 中 seekKey 是一个唯一键，
     * 最终构造的查询条件为：
     * {
     *      "sigma",
     *      "viewId",
     *      "seekKey"
     * }
     */
    @Override
    public Future<JsonObject> restrict(final JsonObject request, final JsonObject syntax) {
        final String field = Ut.valueString(syntax, KName.KIND);
        if (Ut.isNil(field)) {
            return Ux.futureJ();
        }
        final String valueKind = request.getString(field);
        final JsonObject exprData = Ut.valueJObject(syntax, KName.DATA);
        if (Ut.isNil(exprData) || !exprData.containsKey(valueKind)) {
            return Ux.futureJ();
        }
        final JsonObject condition = new JsonObject();
        // 同时支持两种格式
        final Object value = exprData.getValue(valueKind);
        if (value instanceof final String expr) {
            condition.put(KName.Rbac.SEEK_KEY, Ut.fromExpression(expr, request));
        } else if (value instanceof final JsonObject exprJ) {
            condition.mergeIn(Ut.fromExpression(exprJ, request), true);
        }
        Ut.valueCopy(condition, request, KName.VIEW_ID, KName.SIGMA);
        LOG.View.info(this.getClass(), "( Kind ) Visitant unique query condition: {0}", condition);
        return Ux.future(condition);
    }
}
