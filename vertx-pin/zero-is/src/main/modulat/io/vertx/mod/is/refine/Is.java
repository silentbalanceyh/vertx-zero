package io.vertx.mod.is.refine;

import cn.vertxup.integration.domain.tables.pojos.IDirectory;
import io.horizon.atom.common.Kv;
import io.horizon.uca.log.Log;
import io.horizon.uca.log.LogModule;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.is.uca.command.Fs;
import io.vertx.up.eon.KName;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class Is {
    /*
     * Trash Path calculation
     */
    public static Kv<String, String> trashIn(final IDirectory directory) {
        Objects.requireNonNull(directory);
        return IsDir.trash(directory.getStorePath());
    }

    public static Kv<String, String> trashIn(final JsonObject directoryJ) {
        final String path = directoryJ.getString(KName.STORE_PATH);
        Objects.requireNonNull(path);
        return IsDir.trash(path);
    }

    public static ConcurrentMap<String, String> trashIn(final Set<String> pathSet) {
        return IsDir.trash(pathSet);
    }

    public static Kv<String, String> trashOut(final IDirectory directory) {
        Objects.requireNonNull(directory);
        return IsDir.rollback(directory.getStorePath());
    }

    public static Kv<String, String> trashOut(final JsonObject directoryJ) {
        final String path = directoryJ.getString(KName.STORE_PATH);
        Objects.requireNonNull(path);
        return IsDir.rollback(path);
    }

    public static ConcurrentMap<String, String> trashOut(final Set<String> pathSet) {
        return IsDir.rollback(pathSet);
    }

    public static JsonObject dataIn(final JsonObject input) {
        return IsDir.input(input);
    }

    public static JsonArray dataIn(final JsonArray input) {
        return IsDir.input(input);
    }

    public static Future<JsonObject> dataOut(final JsonObject output) {
        return IsDir.output(output);
    }

    public static Future<JsonArray> dataOut(final JsonArray output) {
        return IsDir.output(output);
    }

    /*
     * X_DIRECTORY Operation
     */
    public static Future<List<IDirectory>> directoryQr(final JsonObject condition) {
        return IsDir.query(condition);
    }

    public static Future<List<IDirectory>> directoryQr(final IDirectory directory) {
        return IsDir.query(directory);
    }

    public static Future<List<IDirectory>> directoryQr(final JsonArray data, final String storeField, final boolean strict) {
        return IsDir.query(data, storeField, strict);
    }

    public static Future<IDirectory> directoryBranch(final String key, final String updatedBy) {
        return IsDir.updateBranch(key, updatedBy);
    }

    public static Future<IDirectory> directoryLeaf(final JsonArray directoryJ, final JsonObject params) {
        return IsDir.updateLeaf(directoryJ, params);
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

    public static Future<ConcurrentMap<Fs, Set<String>>> fsGroup(final ConcurrentMap<String, String> fileMap) {
        return IsFs.group(fileMap);
    }

    public static <V> ConcurrentMap<Fs, V> fsGroup(final ConcurrentMap<String, V> map, final Predicate<V> fnKo) {
        return IsFs.group(map, fnKo);
    }

    public static ConcurrentMap<Fs, Set<String>> fsCombine(final ConcurrentMap<Fs, Set<String>> directoryMap,
                                                           final ConcurrentMap<Fs, Set<String>> fileMap) {
        return IsFs.combine(directoryMap, fileMap);
    }

    public static Future<Fs> fsComponent(final String directoryId) {
        return IsFs.component(directoryId);
    }

    public static Future<JsonArray> fsDocument(final JsonArray data, final JsonObject config) {
        return IsFs.document(data, config);
    }

    public interface LOG {
        String MODULE = "Ολοκλήρωση";

        LogModule Init = Log.modulat(MODULE).program("Init");
        LogModule Web = Log.modulat(MODULE).program("Web");
        LogModule File = Log.modulat(MODULE).program("File/Directory");
        LogModule Path = Log.modulat(MODULE).program("Path");
    }
}
