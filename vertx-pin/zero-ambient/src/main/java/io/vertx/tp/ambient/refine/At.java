package io.vertx.tp.ambient.refine;

import cn.vertxup.ambient.domain.tables.pojos.XActivityRule;
import cn.vertxup.ambient.domain.tables.pojos.XNumber;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.optic.extension.*;
import io.vertx.up.commune.wffs.Regulation;
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

    public static void infoApp(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        AtLog.info(logger, "Application", pattern, args);
    }

    public static void infoFlow(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        AtLog.info(logger, "Execution", pattern, args);
    }

    public static void infoTabb(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        AtLog.info(logger, "Tabb", pattern, args);
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

    public static List<String> generate(final XNumber number, final Integer count) {
        return AtSerial.generate(number, count);
    }

    public static Future<List<String>> generateAsync(final XNumber number, final Integer count) {
        return Ux.future(AtSerial.generate(number, count));
    }

    public static XNumber serialAdjust(final XNumber number, final Integer count) {
        return AtSerial.adjust(number, count);
    }

    public static Future<Buffer> fileDownload(final JsonArray attachment) {
        return AtFs.fileDownload(attachment);
    }

    public static Future<Buffer> fileDownload(final JsonObject attachment) {
        return AtFs.fileDownload(attachment);
    }

    public static Future<JsonArray> fileUpload(final JsonArray attachment) {
        return AtFs.fileUpload(attachment);
    }

    public static Future<JsonArray> fileRemove(final JsonArray attachment) {
        return AtFs.fileRemove(attachment);
    }

    // Activity Rule Normalization
    public static Future<Regulation> ruleRegulation(final List<XActivityRule> rules) {
        return AtExpr.ruleRegulation(rules);
    }
}
