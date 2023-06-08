package io.vertx.mod.ke.refine;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

/**
 * @author lang : 2023-06-07
 */
class _Um extends _Key {
    protected _Um() {
    }

    public static <T, I> void umCreated(final I output, final T input) {
        KeEnv.audit(output, null, input, null, false);
    }

    /*
     * Data Audit
     * - umCreated
     *      - active
     *      - language
     *      - sigma
     *      - metadata
     *      - updatedAt
     *      - updatedBy
     *      - createdAt
     *      - createdBy
     * - umUpdated
     *      - active
     *      - language
     *      - sigma
     *      - metadata
     *      - updatedAt
     *      - updatedBy
     * - umIndent
     *      - XNumber Generation
     *      - Support T and Json
     */

    public static <T, I> void umCreated(final I output, final T input, final String pojo) {
        KeEnv.audit(output, null, input, pojo, false);
    }

    public static <T, I> void umCreated(final I output, final String pojo, final T input) {
        KeEnv.audit(output, pojo, input, null, false);
    }

    public static <T, I> void umCreated(final I output, final String outPojo, final T input, final String inPojo) {
        KeEnv.audit(output, outPojo, input, inPojo, false);
    }

    public static <T, I> void umUpdated(final I output, final T input) {
        KeEnv.audit(output, null, input, null, true);
    }

    public static <T, I> void umUpdated(final I output, final T input, final String pojo) {
        KeEnv.audit(output, null, input, pojo, true);
    }

    public static <T, I> void umUpdated(final I output, final String pojo, final T input) {
        KeEnv.audit(output, pojo, input, null, true);
    }

    public static <T, I> void umUpdated(final I output, final String outPojo, final T input, final String inPojo) {
        KeEnv.audit(output, outPojo, input, inPojo, true);
    }

    public static Future<JsonObject> umIndent(final JsonObject data, final String code) {
        return KeEnv.indent(data, code);
    }

    public static Future<JsonArray> umIndent(final JsonArray data, final String code) {
        return KeEnv.indent(data, code);
    }

    public static <T> Future<T> umIndent(final T input, final Function<T, String> fnSigma,
                                         final String code,
                                         final BiConsumer<T, String> fnConsumer) {
        final String sigma = fnSigma.apply(input);
        return KeEnv.indent(input, sigma, code, fnConsumer);
    }

    public static <T> Future<T> umIndent(final T input, final String sigma,
                                         final String code,
                                         final BiConsumer<T, String> fnConsumer) {
        return KeEnv.indent(input, sigma, code, fnConsumer);
    }

    public static <T> Future<List<T>> umIndent(final List<T> input, final Function<List<T>, String> fnSigma,
                                               final String code,
                                               final BiConsumer<T, String> fnConsumer) {
        final String sigma = fnSigma.apply(input);
        return KeEnv.indent(input, sigma, code, fnConsumer);
    }

    public static <T> Future<List<T>> umIndent(final List<T> input, final String sigma,
                                               final String code,
                                               final BiConsumer<T, String> fnConsumer) {
        return KeEnv.indent(input, sigma, code, fnConsumer);
    }

    public static Future<JsonObject> umJData(final JsonObject config, final JsonObject params) {
        return KeEnv.daoJ(config, params);
    }

    public static Future<JsonObject> umAData(final JsonObject config, final JsonObject params) {
        return KeEnv.daoJ(config, params);
    }

    public static Future<JsonArray> umALink(final String field, final String key, final Class<?> daoCls) {
        return KeEnv.daoR(field, key, daoCls);
    }

    public static <T> Future<List<T>> umALink(final String field, final String key, final Class<?> daoCls,
                                              final Function<T, Integer> priorityFn) {
        return KeEnv.daoR(field, key, daoCls, priorityFn);
    }

    public static Future<JsonObject> umUser(final JsonObject input, final JsonObject config) {
        return KeUser.umUser(input, config);
    }

    public static Future<JsonObject> umUser(final JsonObject input) {
        final JsonObject config = Ut.valueJObject(input, KName.__.USER);
        return KeUser.umUser(input, config);
    }
}
