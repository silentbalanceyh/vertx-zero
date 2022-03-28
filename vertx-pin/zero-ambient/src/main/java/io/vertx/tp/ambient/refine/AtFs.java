package io.vertx.tp.ambient.refine;

import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExIo;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

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

    static Future<Buffer> fileDownload(final JsonArray attachment) {
        if (Ut.isNil(attachment)) {
            return Ux.future(Buffer.buffer());
        } else {
            return splitRun(attachment, (directoryId, fileMap) -> Ke.channel(ExIo.class, Buffer::buffer,


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
            return Ke.channel(ExIo.class, Buffer::buffer,

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
                remote -> splitRun(remote, (directoryId, fileMap) -> Ke.channel(ExIo.class, () -> remote,

                    // Call ExIo `fsUpload`
                    io -> io.fsUpload(directoryId, fileMap)
                        .compose(removed -> Ux.future(Ut.cmdRm(fileMap.keySet())))
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
                Ut.cmdRm(files);
                At.infoFile(LOGGER, "Deleted Local files: {0}", String.valueOf(files.size()));
                return Ux.future(local);
            }, remote -> splitRun(remote, (directoryId, fileMap) -> Ke.channel(ExIo.class, () -> remote,

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
                if (Ut.notNil(filePath) && Ut.notNil(storePath)) {
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
        At.infoFile(LOGGER, "Split Running: Local = {0}, Remote = {1}", dataL.size(), dataR.size());
        final List<Future<JsonArray>> futures = new ArrayList<>();
        if (Ut.notNil(dataL)) {
            futures.add(fnLocal.apply(dataL));
        }
        if (Ut.notNil(dataR)) {
            futures.add(fnRemote.apply(dataR));
        }
        return Ux.thenCombineArray(futures);
    }
}
