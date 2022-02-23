package cn.originx.uca.plugin.indent;

import cn.originx.cv.OxCv;
import cn.vertxup.ambient.domain.tables.daos.XCategoryDao;
import cn.vertxup.ambient.domain.tables.pojos.XCategory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.environment.IndentSolver;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NameIndent implements IndentSolver {
    private final transient IndentSolver indent = Ut.singleton(KeyIndent.class);

    @Override
    public Future<String> resolve(final JsonObject data, final JsonObject config) {
        return this.resolveDict(data, config).compose(processed -> this.indent.resolve(processed, config));
    }

    @Override
    public Future<ConcurrentMap<String, JsonArray>> resolve(final JsonObject data, final String identifier, final JsonObject config) {
        return this.resolveDict(data, config).compose(processed -> this.indent.resolve(processed, identifier, config));
    }

    private Future<JsonObject> resolveDict(final JsonObject input, final JsonObject config) {
        // Fetch map data from stored
        return this.sourceMap().compose(map -> {
            final JsonObject inputCopy = input.copy();
            final Object data = inputCopy.getValue(KName.DATA);
            if (data instanceof JsonObject) {
                final JsonObject jsonRef = (JsonObject) data;
                this.resolveData(jsonRef, map, config);
                inputCopy.put(KName.DATA, jsonRef);
            } else if (data instanceof JsonArray) {
                final JsonArray jsonRef = (JsonArray) data;
                Ut.itJArray(jsonRef).forEach(json -> this.resolveData(json, map, config));
                inputCopy.put(KName.DATA, jsonRef);
            }
            return Ux.future(inputCopy);
        });
    }

    private Future<ConcurrentMap<String, String>> sourceMap() {
        final JsonObject condition = new JsonObject();
        condition.put(KName.TYPE, "ci.type");
        return Ux.Jooq.on(XCategoryDao.class).<XCategory>fetchAndAsync(condition)
            .compose(categories -> Ux.future(Ut.elementMap(categories, XCategory::getName, XCategory::getKey)));
    }

    private void resolveData(final JsonObject data, final ConcurrentMap<String, String> dict, final JsonObject config) {
        final String first = config.getString(OxCv.Field.DIM_1, OxCv.Field.CATEGORY_FIRST);
        final String second = config.getString(OxCv.Field.DIM_2, OxCv.Field.CATEGORY_SECOND);
        final String third = config.getString(OxCv.Field.DIM_3, OxCv.Field.CATEGORY_THIRD);
        this.resolveData(data, dict, first);
        this.resolveData(data, dict, second);
        this.resolveData(data, dict, third);
    }

    private void resolveData(final JsonObject data, final ConcurrentMap<String, String> dict, final String field) {
        if (Ut.notNil(field) && data.containsKey(field)) {
            // Replace the field value with input source here.
            final String replaced = dict.get(data.getString(field));
            if (Objects.nonNull(replaced)) {
                data.put(field, replaced);
            }
        }
    }
}
