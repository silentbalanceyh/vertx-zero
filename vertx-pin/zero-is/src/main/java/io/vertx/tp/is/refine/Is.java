package io.vertx.tp.is.refine;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.is.uca.command.Fs;
import io.vertx.up.log.Annal;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Is {

    public static JsonObject directoryIn(final JsonObject input) {
        return IsDir.input(input);
    }

    public static JsonArray directoryIn(final JsonArray input) {
        return IsDir.input(input);
    }

    public static Future<JsonObject> directoryOut(final JsonObject output) {
        return IsDir.output(output);
    }

    public static Future<JsonArray> directoryOut(final JsonArray output) {
        return IsDir.output(output);
    }

    /*
     * X_DIRECTORY Operation
     */
    public static Future<JsonArray> directoryQr(final JsonObject condition) {
        return IsDir.query(condition);
    }

    public static Future<List<IDirectory>> directoryQr(final IDirectory directory) {
        return IsDir.query(directory);
    }

    public static Future<List<IDirectory>> directoryQr(final JsonArray data, final String storeField, final boolean strict) {
        return IsDir.query(data, storeField, strict);
    }

    /*
     * X_DIRECTORY `runComponent` execution
     */
    public static Future<JsonObject> fsRun(final JsonObject data, final Function<Fs, Future<JsonObject>> fsRunner) {
        return IsFs.run(data, fsRunner);
    }

    public static Future<JsonArray> fsRun(final JsonArray data, final BiFunction<Fs, JsonArray, Future<JsonArray>> fsRunner) {
        return IsFs.run(data, fsRunner);
    }

    public static Future<JsonArray> fsDocument(final JsonArray data, final JsonObject config) {
        return IsFs.document(data, config);
    }

    public static class Log {

        public static void infoInit(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IsLog.info(logger, "Init", message, args);
        }

        public static void infoWeb(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IsLog.info(logger, "Web", message, args);
        }

        public static void infoFile(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IsLog.warn(logger, "File/Directory", message, args);
        }

        public static void warnPath(final Class<?> clazz, final String message, final Object... args) {
            final Annal logger = Annal.get(clazz);
            IsLog.warn(logger, "Path", message, args);
        }
    }
}
