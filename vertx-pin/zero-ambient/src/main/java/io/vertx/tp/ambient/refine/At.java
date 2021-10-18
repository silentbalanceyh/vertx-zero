package io.vertx.tp.ambient.refine;

import cn.vertxup.ambient.domain.tables.pojos.XNumber;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;
import io.vertx.tp.optic.extension.*;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;

import java.util.List;

/*
 * Tool class available in current service only
 */
public class At {
    /*
     * Log
     */
    public static void infoInit(final Annal logger, final String pattern, final Object... args) {
        AtLog.info(logger, "Init", pattern, args);
    }

    public static void infoFile(final Annal logger, final String pattern, final Object... args) {
        AtLog.info(logger, "File", pattern, args);
    }

    public static void infoApp(final Annal logger, final String pattern, final Object... args) {
        AtLog.info(logger, "Application", pattern, args);
    }

    public static void infoFlow(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        AtLog.info(logger, "Execution", pattern, args);
    }

    public static Init initApp() {
        return Fn.pool(Pool.INIT_POOL, AppInit.class.getName(), AppInit::new);
    }

    public static Init initSource() {
        return Fn.pool(Pool.INIT_POOL, SourceInit.class.getName(), SourceInit::new);
    }

    public static Init initDatabase() {
        return Fn.pool(Pool.INIT_POOL, DatabaseInit.class.getName(), DatabaseInit::new);
    }

    public static Init initData() {
        return Fn.pool(Pool.INIT_POOL, DatumInit.class.getName(), DatumInit::new);
    }

    public static List<String> serials(final XNumber number, final Integer count) {
        return AtSerial.serials(number, count);
    }

    public static Future<List<String>> serialsAsync(final XNumber number, final Integer count) {
        return Ux.future(AtSerial.serials(number, count));
    }

    /*
     * File
     */
    public static JsonObject upload(final String category, final FileUpload fileUpload) {
        return AtEnv.upload(category, fileUpload);
    }

    public static JsonObject filters(final String appId, final String type, final String code) {
        return AtQuery.filters(appId, new JsonArray().add(type), code);
    }

    public static JsonObject filters(final String appId, final JsonArray types, final String code) {
        return AtQuery.filters(appId, types, code);
    }

    public static JsonObject filtersSigma(final String sigma, final String type, final String code) {
        return AtQuery.filtersSigma(sigma, new JsonArray().add(type), code);
    }

    public static JsonObject filtersSigma(final String sigma, final JsonArray types, final String code) {
        return AtQuery.filtersSigma(sigma, types, code);
    }
}
