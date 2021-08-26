package cn.vertxup.ambient.service;

import cn.vertxup.ambient.domain.tables.daos.XCategoryDao;
import cn.vertxup.ambient.domain.tables.daos.XNumberDao;
import cn.vertxup.ambient.domain.tables.daos.XTabularDao;
import cn.vertxup.ambient.domain.tables.pojos.XNumber;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

public class DatumService implements DatumStub {

    @Override
    public Future<JsonArray> tabulars(final String appId, final String type) {
        return this.fetchArray(XTabularDao.class, At.filters(appId, type, null));
    }

    @Override
    public Future<JsonArray> tabularsBySigma(final String sigma, final JsonArray types) {
        return this.fetchArray(XTabularDao.class, At.filtersSigma(sigma, types, null));
    }

    @Override
    public Future<JsonArray> categories(final String appId, final String type) {
        return this.fetchArray(XCategoryDao.class, At.filters(appId, type, null));
    }

    @Override
    public Future<JsonArray> categories(final String appId, final JsonArray types) {
        return this.fetchArray(XCategoryDao.class, At.filters(appId, types, null));
    }

    @Override
    public Future<JsonArray> categoriesBySigma(final String sigma, final String type) {
        return this.fetchArray(XCategoryDao.class, At.filtersSigma(sigma, type, null));
    }

    @Override
    public Future<JsonArray> categoriesBySigma(final String sigma, final JsonArray types) {
        return this.fetchArray(XCategoryDao.class, At.filtersSigma(sigma, types, null));
    }

    @Override
    public Future<JsonObject> tabular(final String appId, final String type, final String code) {
        return Ux.Jooq.on(XTabularDao.class)
            .fetchOneAsync(At.filters(appId, type, code))
            .compose(Ux::futureJ);
    }

    @Override
    public Future<JsonObject> category(final String appId, final String type, final String code) {
        return Ux.Jooq.on(XCategoryDao.class)
            .fetchOneAsync(At.filters(appId, type, code))
            .compose(Ux::futureJ);
    }

    @Override
    public Future<JsonArray> tabulars(final String appId, final JsonArray types) {
        return this.fetchArray(XTabularDao.class, At.filters(appId, types, null));
    }


    @Override
    public Future<JsonArray> numbers(final String appId, final String code, final Integer count) {
        At.infoFlow(this.getClass(), "Number parameters: appId = {0}, code = {1}, count = {2}",
            appId, code, count);
        final JsonObject filters = new JsonObject();
        filters.put("appId", appId);
        filters.put("code", code);
        return this.numbers(filters, count);
    }

    @Override
    public Future<JsonArray> numbersBySigma(final String sigma, final String code, final Integer count) {
        At.infoFlow(this.getClass(), "Number parameters: sigma = {0}, code = {1}, count = {2}",
            sigma, code, count);
        final JsonObject filters = new JsonObject();
        filters.put(KName.SIGMA, sigma);
        filters.put(KName.CODE, code);
        return this.numbers(filters, count);
    }

    @Override
    public Future<JsonArray> codesBySigma(final String sigma, final String identifier, final Integer count) {
        At.infoFlow(this.getClass(), "Number parameters: sigma = {0}, identifier = {1}, count = {2}",
            sigma, identifier, count);
        final JsonObject filters = new JsonObject();
        filters.put(KName.SIGMA, sigma);
        filters.put(KName.IDENTIFIER, identifier);
        return this.numbers(filters, count);
    }

    private Future<JsonArray> numbers(final JsonObject filters, final Integer count) {
        /*
         * XNumber processing
         */
        return Ux.Jooq.on(XNumberDao.class)
            .<XNumber>fetchOneAsync(filters)
            .compose(number -> {
                if (Objects.isNull(number)) {
                    /*
                     * Not found for XNumber
                     */
                    return Ux.future(new JsonArray());
                } else {
                    /*
                     * Generate numbers
                     * 1) Generate new numbers first
                     * 2) Update numbers instead
                     */
                    return At.serialsAsync(number, count)
                        .compose(generation -> Ux.Jooq.on(XNumberDao.class)
                            .updateAsync(number.setCurrent(number.getCurrent() + count))
                            .compose(updated -> Ux.future(new JsonArray(generation))))
                        .otherwise(Ux.otherwise(JsonArray::new));
                }
            });
    }

    private Future<JsonArray> fetchArray(final Class<?> daoCls, final JsonObject filters) {
        return Ux.Jooq.on(daoCls).fetchAndAsync(filters).compose(Ux::futureA)
            .compose(array -> {
                Ut.itJArray(array).forEach(json -> Ke.mount(json, KName.METADATA));
                return Ux.future(array);
            });
    }
}
