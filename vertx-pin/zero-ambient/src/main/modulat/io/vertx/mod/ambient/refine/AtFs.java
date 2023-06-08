package io.vertx.mod.ambient.refine;

import io.horizon.specification.storage.HFS;
import io.horizon.spi.business.ExIo;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.atom.AtConfig;
import io.vertx.mod.ambient.init.AtPin;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vertx.mod.ambient.refine.At.LOG;

/**
 * The directory data structure should be as following
 * // <pre><code class="json">
 * {
 *     "directoryId": "I_DIRECTORY key field"
 * }
 * // </code></pre>
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AtFs {
    private static final Annal LOGGER = Annal.get(AtFs.class);

    static Future<JsonObject> fileMeta(final JsonObject appJ) {
        final AtConfig config = AtPin.getConfig();
        if (Objects.nonNull(config)) {
            appJ.put(KName.STORE_PATH, config.getStorePath());
        }
        return Ux.futureJ(appJ).compose(Fn.ofJObject(KName.App.LOGO));
    }

    static Future<Buffer> fileDownload(final JsonArray attachment) {
        if (Ut.isNil(attachment)) {
            return Ux.future(Buffer.buffer());
        } else {
            return splitRun(attachment, (directoryId, fileMap) -> Ux.channel(ExIo.class, Buffer::buffer,
                // Call ExIo `fsDownload`
                io -> io.fsDownload(directoryId, fileMap)
            ));
        }
    }

    static Future<Buffer> fileDownload(final JsonObject attachment) {
        final String directoryId = attachment.getString(KName.DIRECTORY_ID);
        final String filePath = attachment.getString(KName.Attachment.FILE_PATH);
        if (Ut.ioExist(filePath)) {
            // Existing temp file here, it means that you can download faster
            return Ux.future(Ut.ioBuffer(filePath));
        }
        if (Ut.isNil(directoryId)) {
            return Ux.future(Buffer.buffer());
        } else {
            final String storePath = attachment.getString(KName.STORE_PATH);
            return Ux.channel(ExIo.class, Buffer::buffer,

                // Call ExIo `fsDownload`
                io -> io.fsDownload(directoryId, storePath)
            );
        }
    }

    /*
     * Step:
     * 1. Extract `directoryId` first.
     * 2. Build the mapping of `FILE_PATH = STORE_PATH` here.
     * 3. Pass two parameters to `ExIo`
     */
    static Future<JsonArray> fileUpload(final JsonArray attachment) {
        if (Ut.isNil(attachment)) {
            return Ux.futureA();
        } else {
            return splitInternal(attachment, Ux::future,
                remote -> splitRun(remote, (directoryId, fileMap) -> Ux.channel(ExIo.class, () -> remote,

                    // Call ExIo `fsUpload`
                    io -> io.fsUpload(directoryId, fileMap)
                        .compose(removed -> HFS.common().rmAsync(fileMap.keySet()))
                        .compose(removed -> Ux.future(remote))
                )));
        }
    }


    static Future<JsonArray> fileRemove(final JsonArray attachment) {
        if (Ut.isNil(attachment)) {
            return Ux.futureA();
        } else {
            return splitInternal(attachment, local -> {
                final Set<String> files = new HashSet<>();
                Ut.itJArray(local).forEach(each -> files.add(each.getString(KName.Attachment.FILE_PATH)));
                HFS.common().rm(files);
                LOG.File.info(LOGGER, "Deleted Local files: {0}", String.valueOf(files.size()));
                return Ux.future(local);
            }, remote -> splitRun(remote, (directoryId, fileMap) -> Ux.channel(ExIo.class, () -> remote,

                // Call ExIo `fsRemove`
                io -> io.fsRemove(directoryId, fileMap).compose(removed -> Ux.future(remote))
            )));
        }
    }

    private static <T> Future<T> splitRun(
        final JsonArray source,
        final BiFunction<String, ConcurrentMap<String, String>, Future<T>> executor) {
        final ConcurrentMap<String, String> fileMap = new ConcurrentHashMap<>();
        final Set<String> directorySet = new HashSet<>();
        Ut.itJArray(source).forEach(json -> {
            if (json.containsKey(KName.DIRECTORY_ID)) {
                final String filePath = json.getString(KName.Attachment.FILE_PATH);
                final String storePath = json.getString(KName.STORE_PATH);
                if (Ut.isNotNil(filePath) && Ut.isNotNil(storePath)) {
                    directorySet.add(json.getString(KName.DIRECTORY_ID));
                    fileMap.put(filePath, storePath);
                }
            }
        });
        return executor.apply(directorySet.iterator().next(), fileMap);
    }

    private static Future<JsonArray> splitInternal(
        final JsonArray source,
        final Function<JsonArray, Future<JsonArray>> fnLocal,
        final Function<JsonArray, Future<JsonArray>> fnRemote) {
        final JsonArray dataL = new JsonArray();
        final JsonArray dataR = new JsonArray();
        Ut.itJArray(source).forEach(item -> {
            final String directoryId = item.getString(KName.DIRECTORY_ID);
            if (Ut.isNil(directoryId)) {
                dataL.add(item);
            } else {
                dataR.add(item);
            }
        });
        LOG.File.info(LOGGER, "Split Running: Local = {0}, Remote = {1}", dataL.size(), dataR.size());
        final List<Future<JsonArray>> futures = new ArrayList<>();
        if (Ut.isNotNil(dataL)) {
            futures.add(fnLocal.apply(dataL));
        }
        if (Ut.isNotNil(dataR)) {
            futures.add(fnRemote.apply(dataR));
        }
        return Fn.compressA(futures);
    }
}
