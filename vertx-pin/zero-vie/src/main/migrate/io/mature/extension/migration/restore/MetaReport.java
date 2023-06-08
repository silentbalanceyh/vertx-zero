package io.mature.extension.migration.restore;

import cn.vertxup.ambient.domain.tables.daos.XCategoryDao;
import cn.vertxup.ambient.domain.tables.pojos.XCategory;
import io.horizon.eon.em.Environment;
import io.macrocosm.specification.app.HApp;
import io.mature.extension.migration.AbstractStep;
import io.modello.atom.app.KDS;
import io.modello.dynamic.modular.jdbc.Pin;
import io.modello.dynamic.modular.metadata.AoBuilder;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.config.Database;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MetaReport extends AbstractStep {
    private AoBuilder builder;

    public MetaReport(final Environment environment) {
        super(environment);
    }

    private AoBuilder getBuilder() {
        if (null == this.builder) {
            final KDS<Database> ds = this.ark.database();
            this.builder = Pin.getInstance().getBuilder(ds.dynamic());
        }
        return this.builder;
    }

    @Override
    public Future<JsonObject> procAsync(final JsonObject config) {
        this.banner("002.3-1. 字段修改部分信息");
        this.builder = this.getBuilder();
        final HApp app = this.ark.app();
        return Ux.Jooq.on(XCategoryDao.class).<XCategory>fetchAsync(KName.SIGMA, app.option(KName.SIGMA))
            .compose(categories -> {
                /*
                 * 元素结构：JsonObject
                 * {
                 *     "identifier": {
                 *          "same": true,
                 *          "oldName": "",
                 *          "oldType": "",
                 *          "oldLen": "",
                 *          "newName": "",
                 *          "newType": "",
                 *          "newLen": ""
                 *     }
                 * }
                 */
                final List<Future<JsonObject>> futures = new ArrayList<>();
                categories
                    .stream()
                    .filter(category -> Objects.nonNull(category.getIdentifier()))
                    .map(this::procAsync)
                    .forEach(futures::add);
                return Fn.combineA(futures);
            })
            .compose(combined -> {
                final String folder = this.ioRoot(config);
                final String file = folder + "report/types/data.json";
                return this.writeAsync(combined, file).compose(nil -> Ux.future(config));
            });
    }

    private Future<JsonObject> procAsync(final XCategory category) {
        return Ux.future(new JsonObject());
        // return this.combineSchema(category.getIdentifier()).compose(schema -> Ux.future(this.builder.report(schema)));
    }
/*
    private Future<Schema> combineSchema(final String identifier) {
        final EntityActor entityActor = new EntityActor();
        return entityActor.retrieveByIdentifier(identifier)
            .compose(entity -> {
                // 按 schema 的格式重新拼接
                final JsonObject schemaEntity = new JsonObject()
                    .put(KName.ENTITY, entity)
                    .put(KName.Modeling.KEYS, Optional.ofNullable(entity.getJsonArray(KName.Modeling.KEYS)).orElse(new JsonArray()))
                    .put(KName.Modeling.FIELDS, Optional.ofNullable(entity.getJsonArray(KName.Modeling.FIELDS)).orElse(new JsonArray()));
                final Schema schema = Ao.toSchema(this.app.getName(), schemaEntity);
                return Ux.future(schema);
            });
    }*/
}
