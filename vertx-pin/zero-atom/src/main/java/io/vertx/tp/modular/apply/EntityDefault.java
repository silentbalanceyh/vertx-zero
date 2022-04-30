package io.vertx.tp.modular.apply;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.cv.em.EntityType;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

import java.util.Locale;

class EntityDefault implements AoDefault {

    @Override
    public void applyJson(final JsonObject entity) {
        Ao.debugUca(this.getClass(), "「DFT」实体输入值: {0}", entity.encode());
        /*
         * 默认值：
         * key
         * type
         * tableName
         * active
         * language
         * metadata
         */
        AoDefault.apply(entity, "type", EntityType.ENTITY.name());
        AoDefault.apply(entity, "tableName", this.getTable(entity.getString(KName.IDENTIFIER)));
        AoDefault.apply(entity);
    }

    private String getTable(final String identifier) {
        return Ut.isNil(identifier) ? Strings.EMPTY :
            identifier.replace('.', '_')
                .toUpperCase(Locale.getDefault());
    }
}
