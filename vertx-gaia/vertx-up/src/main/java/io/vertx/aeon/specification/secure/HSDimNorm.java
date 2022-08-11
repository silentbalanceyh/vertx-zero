package io.vertx.aeon.specification.secure;

import io.vertx.aeon.atom.secure.HPermit;
import io.vertx.aeon.atom.secure.Hoi;
import io.vertx.aeon.specification.app.HES;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.eon.Values;
import io.vertx.up.experiment.mixture.HAtom;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;

public class HSDimNorm implements HSDim {
    protected transient HAtom atom;
    protected transient Hoi owner;

    @Override
    public HSDim bind(final HAtom atom) {
        this.atom = atom;
        this.owner = HES.caller(atom.sigma());
        return this;
    }

    @Override
    public Future<JsonObject> configure(final HPermit input) {
        /*
         * 提取 dmConfig 字段的数据，解析 items 内容，执行维度数据源的提取
         * - items 为 JsonArray
         * - items 为 JsonObject（三种模式）
         */
        final JsonObject inputJ = input.dmJ();
        return Fn.choiceJ(inputJ, KName.ITEMS,
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
             *
             * 2. 后端模式
             * {
             *     "items": {
             *         "dao": "Java对应的Dao类名，为Ux.Jooq.on方法参数",
             *         "config": "附加配置处理数据源"
             *     }
             * }
             */
            itemJ -> this.configure(input, inputJ),
            /*
             * 3. 静态模式（无子类逻辑）
             * {
             *     "items": [
             *         "value,label,key"
             *     ]
             * }
             */
            this::configure
        );
    }

    private Future<ClusterSerializable> configure(final HPermit input, final JsonObject inputJ) {
        final Class<?> daoCls = Ut.valueCI(inputJ, KName.DAO, null);
        if (Objects.isNull(daoCls)) {
            // 前端模式，返回JsonObject
            return Future.succeededFuture(inputJ);
        } else {
            // 后端模式，返回JsonArray
            return this.configure(input, daoCls).compose(Future::succeededFuture);
        }
    }

    protected Future<JsonArray> configure(final HPermit input, final Class<?> daoCls) {
        return Future.succeededFuture(new JsonArray());
    }

    private Future<JsonArray> configure(final JsonArray inputA) {
        final JsonArray data = new JsonArray();
        Ut.itJArray(inputA, String.class, (itemStr, index) -> {
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
}
