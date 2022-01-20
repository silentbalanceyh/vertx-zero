package cn.originx.uca.plugin.semi;

import cn.originx.refine.Ox;
import cn.originx.uca.code.Numeration;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.optic.plugin.BeforePlugin;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.stream.Collectors;

public class BeforeNumber implements BeforePlugin {
    private static final String PLUGIN_NUMBER_SINGLE = "生成配置项编号：{0}";
    private static final String PLUGIN_NUMBER_BATCH = "总共生成配置项编号：{0} 个";
    private transient DataAtom atom;

    @Override
    public BeforePlugin bind(final DataAtom atom) {
        this.atom = atom;
        return this;
    }

    @Override
    public Future<JsonObject> beforeAsync(final JsonObject record, final JsonObject config) {
        final String fieldName = this.beforeField(record, config);
        if (Ut.isNil(fieldName)) {
            // 「不生成」不需要生成序号
            return Ux.future(record);
        }

        // Code 没有，执行编号生成
        return this.numberAsync(config).compose(number -> {
            Ox.Log.infoHub(this.getClass(), PLUGIN_NUMBER_SINGLE, number);
            return Ux.future(record.put(fieldName, number));
        });
    }

    private String beforeField(final JsonObject record, final JsonObject config) {
        final JsonObject configOpt = Ut.sureJObject(config);
        final String fieldNum = configOpt.getString(KName.FIELD);
        if (Ut.isNil(fieldNum)) {
            // 「不生成」配置中不存在 `field` 字段
            return null;
        }
        // 检查数据中是否包含了该字段
        final String inputNum = record.getString(fieldNum);
        if (Ut.isNil(inputNum)) {
            // 只有这样的情况会生成 XNumber 记录
            return fieldNum;
        } else {
            // 「不生成」不需要生成 number
            return null;
        }
    }

    private Future<String> numberAsync(final JsonObject config) {
        final Numeration numeration = Numeration.service(this.atom.sigma());
        final String indent = config.getString(KName.INDENT);
        if (Ut.isNil(indent)) {
            return numeration.atom(this.atom, config);
        } else {
            return numeration.indent(indent, config);
        }
    }

    @Override
    public Future<JsonArray> beforeAsync(final JsonArray records, final JsonObject config) {
        final List<String> fields = this.beforeField(records, config);
        if (fields.isEmpty()) {
            // 不需要生成序号
            return Ux.future(records);
        }
        // 根据数量生成序号
        return this.numberAsync(config, fields.size()).compose(numberQueue -> {
            Ox.Log.infoHub(this.getClass(), PLUGIN_NUMBER_BATCH, numberQueue.size());
            Ut.itJArray(records).forEach(record -> {
                // 双重检查，为没有 field 值的记录填充序号
                final String fieldName = this.beforeField(record, config);
                if (Ut.notNil(fieldName)) {
                    record.put(fieldName, numberQueue.poll());
                }
            });
            return Ux.future(records);
        });
    }

    private Future<Queue<String>> numberAsync(final JsonObject config, final Integer size) {
        final Numeration numeration = Numeration.service(this.atom.sigma());
        final String indent = config.getString(KName.INDENT);
        if (Ut.isNil(indent)) {
            return numeration.atom(this.atom, size, config);
        } else {
            return numeration.indent(indent, size, config);
        }
    }

    private List<String> beforeField(final JsonArray records, final JsonObject config) {
        return Ut.itJArray(records).map(record -> this.beforeField(record, config))
            .filter(Objects::nonNull).collect(Collectors.toList());
    }
}
