package cn.originx.uca.plugin.indent;

import cn.originx.cv.OxCv;
import cn.originx.refine.Ox;
import cn.vertxup.ambient.domain.tables.daos.XCategoryDao;
import cn.vertxup.ambient.domain.tables.pojos.XCategory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.environment.IndentSolver;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KeyIndent implements IndentSolver {

    @Override
    public Future<String> resolve(final JsonObject input, final JsonObject config) {
        final JsonObject data = Ut.sureJObject(input.getJsonObject(KName.DATA));
        final String hitKey = this.key(data, config);
        if (Ut.isNil(data) || Ut.isNil(hitKey)) {
            Ox.Log.warnPlugin(this.getClass(), "未读取到标识信息：{0}, 配置：{1}",
                data.encode(), config.encode());
            return Ux.future(null);
        } else {
            return Ux.Jooq.on(XCategoryDao.class).<XCategory>fetchByIdAsync(hitKey).compose(category -> {
                final String identifier = this.identifier(category);
                Ox.Log.infoPlugin(this.getClass(), "标识选择：key = `{0}`, identifier = `{1}`, data = `{2}`",
                    hitKey, identifier, data.encode());
                return Ux.future(identifier);
            });
        }
    }

    @Override
    public Future<ConcurrentMap<String, JsonArray>> resolve(final JsonObject input, final String identifier, final JsonObject config) {
        final JsonArray dataArray = input.getJsonArray(KName.DATA);
        if (Ut.isNil(dataArray)) {
            return Ux.future(new ConcurrentHashMap<>());
        } else {
            Ox.Log.infoPlugin(this.getClass(), "标识选择输入数据（批量）：data = `{0}`", dataArray.encode());
            final ConcurrentMap<String, JsonArray> sourceMap = Ut.elementGroup(dataArray, (json) -> this.key(json, config));
            // `key` field collect into array
            final JsonArray values = Ut.toJArray(sourceMap.keySet());
            return Ux.Jooq.on(XCategoryDao.class).<XCategory>fetchInAsync(KName.KEY, values)
                // `build` the result grouped values here.
                .compose(categories -> Ux.future(this.map(identifier, categories, sourceMap)));
        }
    }

    /*
     * Multi grouped here for building group data.
     */
    private ConcurrentMap<String, JsonArray> map(final String identifier, final List<XCategory> categories, final ConcurrentMap<String, JsonArray> sourceMap) {
        final ConcurrentMap<String, JsonArray> resultMap = new ConcurrentHashMap<>();
        /*
         * 按 identifier 分组
         */
        final ConcurrentMap<String, XCategory> grouped = Ut.elementMap(categories, XCategory::getKey);
        final JsonArray defaultQueue = new JsonArray();
        sourceMap.forEach((key, data) -> {
            final XCategory category = grouped.get(key);
            final String hitted = this.identifier(category);
            /*
             * 处理 identifier
             */
            if (Ut.isNil(hitted)) {
                defaultQueue.addAll(data);
            } else {
                resultMap.put(hitted, data.copy());
            }
        });
        if (!defaultQueue.isEmpty()) {
            resultMap.put(identifier, defaultQueue);
        }
        return resultMap;
    }


    /*
     * XCategory -> identifier extraction code logical
     */
    private String identifier(final XCategory category) {
        return (Objects.isNull(category) || Ut.isNil(category.getIdentifier())) ? null : category.getIdentifier();
    }


    /*
     * Default data structure
     * {
     *      "categoryFirst",
     *      "categorySecond",
     *      "categoryThird"
     * }
     * But the seeking path is:
     *
     * categoryThird, categorySecond, categoryFirst
     *
     * The configuration json:
     * {
     *      "dim1": "categoryFirst",
     *      "dim2": "categorySecond",
     *      "dim3": "categoryThird"
     * }
     *
     * dim means `dimension`
     */
    private String key(final JsonObject data, final JsonObject config) {
        if (Ut.isNil(data)) {
            // Terminate when data is empty {} or null
            return null;
        }
        // Seeking by category third ( dim3 field )
        String keyField = config.getString(OxCv.Field.DIM_3, OxCv.Field.CATEGORY_THIRD);
        String key = data.getString(keyField);
        if (Ut.isNil(key)) {


            // Seeking by category second ( dim2 field )
            keyField = config.getString(OxCv.Field.DIM_2, OxCv.Field.CATEGORY_SECOND);
            key = data.getString(keyField);
        }

        if (Ut.isNil(key)) {


            // Seeking by category second ( dim1 field )
            keyField = config.getString(OxCv.Field.DIM_1, OxCv.Field.CATEGORY_FIRST);
            key = data.getString(keyField);
        }
        return key;
    }
}
