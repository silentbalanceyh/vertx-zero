package cn.originx.plugin.indent;

import cn.originx.cv.OxCv;
import cn.vertxup.ambient.domain.tables.daos.XCategoryDao;
import cn.vertxup.ambient.domain.tables.pojos.XCategory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.component.ComponentIndent;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class NameIndent implements ComponentIndent {
    private final static Annal LOGGER = Annal.get(NameIndent.class);
    private final transient ComponentIndent indent = Ut.singleton(KeyIndent.class);

    @Override
    public Future<String> resolve(final JsonObject data, final JsonObject config) {
        return this.resolveDict(data).compose(processed -> this.indent.resolve(processed, config));
    }

    @Override
    public Future<ConcurrentMap<String, JsonArray>> resolve(final JsonObject data, final String identifier, final JsonObject config) {
        return this.resolveDict(data).compose(processed -> this.indent.resolve(processed, identifier, config));
    }

    private Future<JsonObject> resolveDict(final JsonObject input) {
        final JsonObject inputCopy = input.copy();
        final Object data = inputCopy.getValue(KName.DATA);
        return this.resolveDict().compose(map -> {
            /*
             * categoryFirst
             * categorySecond
             * categoryThird
             */
            if (data instanceof JsonObject) {
                final JsonObject jsonRef = (JsonObject) data;
                this.resolveData(jsonRef, map, OxCv.Field.CATEGORY_FIRST);
                this.resolveData(jsonRef, map, OxCv.Field.CATEGORY_SECOND);
                this.resolveData(jsonRef, map, OxCv.Field.CATEGORY_THIRD);
                LOGGER.info("标识规则切换前转换：{0}", jsonRef.encode());
                inputCopy.put(KName.DATA, jsonRef);
            } else if (data instanceof JsonArray) {
                final JsonArray jsonRef = (JsonArray) data;
                Ut.itJArray(jsonRef).forEach(json -> {
                    this.resolveData(json, map, OxCv.Field.CATEGORY_FIRST);
                    this.resolveData(json, map, OxCv.Field.CATEGORY_SECOND);
                    this.resolveData(json, map, OxCv.Field.CATEGORY_THIRD);
                });
                inputCopy.put(KName.DATA, jsonRef);
            }
            return Ux.future(inputCopy);
        });
    }

    private Future<ConcurrentMap<String, String>> resolveDict() {
        final JsonObject condition = new JsonObject();
        condition.put(KName.TYPE, "ci.type");
        return Ux.Jooq.on(XCategoryDao.class).<XCategory>fetchAndAsync(condition)
            //
            .compose(categories -> Ux.future(Ut.elementMap(categories, XCategory::getName, XCategory::getKey)));
    }

    private void resolveData(final JsonObject data,
                             final ConcurrentMap<String, String> dict,
                             final String field) {
        if (Ut.notNil(field)) {
            if (data.containsKey(field)) {
                // 值替换专用
                final String replaced = dict.get(data.getString(field));
                if (Objects.nonNull(replaced)) {
                    data.put(field, replaced);
                }
            }
        }
    }
}
