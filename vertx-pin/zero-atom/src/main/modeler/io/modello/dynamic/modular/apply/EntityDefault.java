package io.modello.dynamic.modular.apply;

import io.horizon.eon.VString;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.atom.cv.em.EntityType;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.Locale;

import static io.vertx.mod.atom.refine.Ao.LOG;

class EntityDefault implements AoDefault {

    @Override
    public void applyJson(final JsonObject entity) {
        LOG.Uca.debug(this.getClass(), "「DFT」实体输入值: {0}", entity.encode());
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
        return Ut.isNil(identifier) ? VString.EMPTY :
            identifier.replace('.', '_')
                .toUpperCase(Locale.getDefault());
    }
}
