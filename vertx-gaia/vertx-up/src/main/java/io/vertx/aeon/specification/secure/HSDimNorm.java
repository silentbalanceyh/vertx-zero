package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;

public class HSDimNorm extends AbstractAdmit {

    @Override
    public Future<JsonObject> configure(final HPermit permit) {
        /*
         * 提取 dmConfig 字段的数据，解析 items 内容，执行维度数据源的提取
         * - items 为 JsonArray
         * - items 为 JsonObject（三种模式）
         *
         * {
         *     "items":   JOBJECT | JARRAY,
         *     "mapping": shapeMapping,
         *     "qr":      shapeQr
         * }
         */
        final JsonObject request = permit.dmJ();
        final JsonObject qrJ = Ut.valueJObject(request, KName.Rbac.QR);
        request.put(KName.Rbac.QR, this.valueQr(qrJ, null));
        // 维度计算一旦存在直接调用 compile
        return Future.succeededFuture(request);
    }

    @Override
    public Future<JsonObject> compile(final HPermit permit, final JsonObject request) {
        return Fn.choiceJ(request, KName.ITEMS,
            itemJ -> {
                final Class<?> daoCls = Ut.valueCI(request, KName.DAO, null);
                if (Objects.isNull(daoCls)) {
                    /*
                     * 1. 前端模式（直接返回，无子类逻辑）
                     * {
                     *     "items": {
                     *         "source": "字典名称",
                     *         "label":  "显示字段名",
                     *         "value":  "值字段名",
                     *         "key":    "键字段名"
                     *     }
                     * }
                     */
                    return Future.succeededFuture(request);
                } else {
                    /*
                     * 2. 后端模式
                     * {
                     *     "items": {
                     *         "dao": "Java对应的Dao类名，为Ux.Jooq.on方法参数",
                     *         "config": "附加配置处理数据源"
                     *     }
                     * }
                     */
                    final JsonObject config = Ut.valueJObject(itemJ, KName.CONFIG);
                    return this.compile(permit, daoCls, config).compose(Future::succeededFuture);
                }
            },
            /*
             * 3. 静态模式（无子类逻辑）
             * {
             *     "items": [
             *         "value,label,key"
             *     ]
             * }
             */
            itemA -> {
                final JsonArray data = new JsonArray();
                Ut.itJArray(itemA, String.class, (itemStr, index) -> {
                    final String[] split = itemStr.split(Strings.COMMA);
                    if (Values.TWO <= split.length) {
                        // 2 == length
                        final JsonObject itemJ = new JsonObject();
                        itemJ.put(KName.VALUE, split[Values.IDX]);
                        itemJ.put(KName.LABEL, split[Values.IDX_1]);
                        // 3 == length
                        if (Values.TWO < split.length) {
                            itemJ.put(KName.KEY, split[Values.IDX_2]);
                        } else {
                            itemJ.put(KName.KEY, itemJ.getValue(KName.VALUE));
                        }
                        data.add(itemJ);
                    }
                });
                return Future.succeededFuture(data);
            }
        );
    }

    protected Future<JsonArray> compile(final HPermit input, final Class<?> daoCls, final JsonObject config) {
        return Future.succeededFuture(new JsonArray());
    }
}
