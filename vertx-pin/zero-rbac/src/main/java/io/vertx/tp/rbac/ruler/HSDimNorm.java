package io.vertx.tp.rbac.ruler;

import io.aeon.atom.secure.HPermit;
import io.aeon.experiment.specification.secure.AbstractAdmit;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.bridge.Strings;
import io.vertx.up.eon.bridge.Values;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;

public class HSDimNorm extends AbstractAdmit {

    @Override
    public Future<JsonObject> configure(final HPermit permit, final JsonObject requestJ) {
        return super.configure(permit, requestJ, HPermit::dmJ);
    }

    @Override
    public Future<JsonObject> compile(final HPermit permit, final JsonObject request) {
        return Fn.choiceJ(request, KName.ITEMS,
            itemJ -> {
                final Class<?> daoCls = Ut.valueC(itemJ, KName.DAO, null);
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
                    return Future.succeededFuture(itemJ);
                } else {
                    /*
                     * 2. 后端模式
                     * {
                     *     "items": {
                     *         "dao": "Java对应的Dao类名，为Ux.Jooq.on方法参数",
                     *         "config": "附加配置处理数据源"
                     *     }
                     * }
                     * Adapter转换，和Ui中的Compiler对齐
                     * 1. qrJ 为转换完成的查询条件
                     * 2. config 就为 itemJ 本身
                     */
                    final JsonObject inputJ = permit.dmJ();
                    final JsonObject qrJ = this.configureQr(Ut.valueJObject(inputJ, KName.Rbac.QR), null);
                    return this.compile(permit, qrJ, itemJ.copy())
                        .compose(itemA -> this.normalize(itemA, permit))
                        .compose(Future::succeededFuture);
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
            this::normalize
        );
    }


    protected Future<JsonArray> compile(final HPermit input, final JsonObject qrJ, final JsonObject config) {
        return Future.succeededFuture(new JsonArray());
    }

    /*
     * 解析专用函数
     * 1）静态模式：全程父类解析
     * 2）后端模式：子类读取数据 + 父类解析结果（mapping计算）
     */
    private Future<JsonArray> normalize(final JsonArray itemA) {
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

    private Future<JsonArray> normalize(final JsonArray itemA, final HPermit permit) {
        /*
         * Include Mapping
         * {
         *     "mapping": {}
         * }
         */
        final JsonObject dmJ = permit.dmJ();
        final JsonArray dataA = Ut.valueTo(itemA, dmJ, (converted, json) -> converted.put(KName.DATA, json));
        return Future.succeededFuture(dataA);
    }
}
