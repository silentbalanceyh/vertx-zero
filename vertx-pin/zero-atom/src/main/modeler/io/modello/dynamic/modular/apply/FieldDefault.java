package io.modello.dynamic.modular.apply;

import cn.vertxup.atom.domain.tables.pojos.MEntity;
import io.vertx.core.json.JsonObject;
import io.vertx.up.util.Ut;

import java.util.Locale;

import static io.vertx.mod.atom.refine.Ao.LOG;

class FieldDefault implements AoDefault {
    private transient MEntity entity;

    @Override
    public <T> AoDefault mount(final T mounted) {
        if (mounted instanceof MEntity) {
            this.entity = (MEntity) mounted;
        }
        return this;
    }

    @Override
    public void applyJson(final JsonObject field) {
        if (null != this.entity) {
            LOG.Uca.debug(this.getClass(), "「DFT」实体字段输入值: {0}", field.encode());
            /*
             * key
             * type
             * columnName = name转大写
             * isPrimary = false默认
             * isNullable = false默认
             * length = 255，默认，STRINGx
             * precision = 2，默认，DECIMALx
             * entityId
             */
            AoDefault.apply(field, "type", String.class.getName());
            final String name = field.getString("name");
            if (Ut.isNotNil(name)) {
                AoDefault.apply(field, "columnName", name.toUpperCase(Locale.getDefault()));
            }
            AoDefault.apply(field, "isPrimary", Boolean.FALSE);
            AoDefault.apply(field, "isNullable", Boolean.TRUE);
            // 根据列类型自动处理
            final String columnType = field.getString("columnType");
            if (columnType.startsWith("STRING")) {
                AoDefault.apply(field, "length", 255);
            } else if (columnType.startsWith("DECIMAL")) {
                AoDefault.apply(field, "precision", 2);
            }
            AoDefault.apply(field, "entityId", this.entity.getKey());
            AoDefault.apply(field);
        }
    }
}
