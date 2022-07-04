package io.vertx.tp.ambient.refine;

import cn.vertxup.ambient.service.file.DocRStub;
import cn.vertxup.ambient.service.file.DocReader;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.atom.AtConfig;
import io.vertx.tp.ambient.init.AtPin;
import io.vertx.tp.ke.cv.em.BizInternal;
import io.vertx.tp.optic.business.ExIo;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.di.DiPlugin;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
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
    private static final DiPlugin PLUGIN = DiPlugin.create(AtFs.class);

    static Future<JsonObject> fileMeta(final JsonObject appJ) {
        final AtConfig config = AtPin.getConfig();
        if (Objects.nonNull(config)) {
            appJ.put(KName.STORE_PATH, config.getStorePath());
        }
        return Ux.futureJ(appJ).compose(Ut.ifJObject(KName.App.LOGO));
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
            }, remote -> splitRun(remote, (directoryId, fileMap) -> Ux.channel(ExIo.class, () -> remote,

                // Call ExIo `fsRemove`
                io -> io.fsRemove(directoryId, fileMap).compose(removed -> Ux.future(remote))
            )));
        }
    }

    /*
     * Here are file upload for directory calculation
     * 1) directory processing first
     * 2) directory generation with ExIo class
     * 3) Add the new information ( directoryId ) into each attachment.
     */
    static Future<JsonArray> fileDir(final JsonArray attachment, final JsonObject params) {
        final String directory = Ut.valueString(attachment, KName.DIRECTORY);
        /*
         *  String parsing on `directory` for final processing
         */
        final String storePath;
        if (directory.contains("`")) {
            storePath = Ut.fromExpression(directory, params);
        } else {
            storePath = directory;
        }
        final JsonObject input = new JsonObject();
        /*
         * Configured directory for storing, for example:
         * 1. /apps/xc/document             ( Root Folder )
         * 2. /apps/xc/document/xxxx        ( store Path of current attachment )
         */
        final String sigma = params.getString(KName.SIGMA);
        input.put(KName.STORE_PATH, Ut.toJArray(Ut.ioPathLadder(storePath)));
        input.put(KName.SIGMA, sigma);
        input.put(KName.UPDATED_BY, params.getValue(KName.UPDATED_BY));

        /*
         * appId from params
         */
        return Ux.channel(ExIo.class, () -> null, io -> io.dirTree(storePath, sigma)
            .compose(directories -> {
                if (directories.isEmpty()) {
                    At.infoFile(LOGGER, "Zero will re-initialize directory try to find {0}", storePath);
                    final DocRStub reader = PLUGIN.createComponent(DocReader.class);
                    final String appId = params.getString(KName.APP_ID);
                    // Default value of Zero
                    return reader.treeDir(appId, BizInternal.TypeEntity.Directory.value())
                        .compose(nil -> io.dirTree(storePath, sigma));
                } else {
                    return Ux.future(directories);
                }
            })
            .compose(directories -> io.verifyIn(directories, input))
        ).compose(directoryJ -> {
            final JsonObject verified = Ut.valueJObject(directoryJ);
            Ut.itJArray(attachment).forEach(content -> {
                /*
                 * Replaced the field
                 * - directoryId, refer to I_DIRECTORY record,                      key field
                 * - storeWay, refer to I_DIRECTORY record,                         type field
                 * - storePath, refer to calculated result here.  I_DIRECTORY storePath + name
                 */
                content.put(KName.DIRECTORY_ID, verified.getString(KName.KEY));
                content.put(KName.STORE_PATH, Ut.ioPath(storePath, content.getString(KName.NAME)));
                content.put(KName.Attachment.STORE_WAY, verified.getString(KName.TYPE));
            });
            return Ux.future(attachment);
        });
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
