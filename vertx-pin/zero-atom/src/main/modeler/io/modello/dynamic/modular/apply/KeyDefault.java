package io.modello.dynamic.modular.apply;

import cn.vertxup.atom.domain.tables.pojos.MEntity;
import io.modello.eon.em.EmKey;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import static io.vertx.mod.atom.refine.Ao.LOG;

class KeyDefault implements AoDefault {
    private transient MEntity entity;

    @Override
    public <T> AoDefault mount(final T mounted) {
        if (mounted instanceof MEntity) {
            this.entity = (MEntity) mounted;
        }
        return this;
    }

    @Override
    public void applyJson(final JsonObject key) {
        if (null != this.entity) {
            LOG.Uca.debug(this.getClass(), "「DFT」键输入值：{0}", key.encode());
            /*
             * 默认值:
             * key
             * name
             * entityId：从entity中读取
             * columns：必须反序列化，这个字段不可能被直接序列化出来
             * active
             * language
             * metadata
             */
            // 先计算
            AoDefault.apply(key, "name", this.getName(key));
            AoDefault.apply(key, "entityId", this.entity.getKey());
            // 上述流程完成过后
            final JsonArray columns = this.getColumns(key);
            key.put("columns", columns.encode());
            AoDefault.apply(key);
        } else {
            LOG.Atom.debug(KeyDefault.class, "[OxE] key 传入的实体为空！");
        }
    }

    private String getName(final JsonObject key) {
        // 读取键类型
        final EmKey.Type type = Ut.toEnum(key.getString("type"), EmKey.Type.class);
        final StringBuilder name = new StringBuilder();
        name.append(EmKey.Type.PRIMARY == type ? "PK_" : "UK_");
        name.append(this.entity.getTableName()).append('_');
        // 列名
        final JsonArray columns = this.getColumns(key);
        final Set<String> columnSet = new TreeSet<>();
        columns.stream().map(column -> (String) column).forEach(columnSet::add);
        // 后缀
        name.append(Ut.fromJoin(columnSet, "_"));
        return name.toString().toUpperCase(Locale.getDefault());
    }

    private JsonArray getColumns(final JsonObject key) {
        final Object valueAdapter = key.getValue("columns");
        JsonArray array = new JsonArray();
        if (null != valueAdapter) {
            // 直接使用适配模式构造列
            if (String.class == valueAdapter.getClass()) {
                array = new JsonArray(valueAdapter.toString());
            } else {
                array = key.getJsonArray("columns");
            }
        }
        return array;
    }
}
