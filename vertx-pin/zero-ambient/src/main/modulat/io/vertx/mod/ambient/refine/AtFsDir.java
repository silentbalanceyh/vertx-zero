package io.vertx.mod.ambient.refine;

import cn.vertxup.ambient.service.file.DocBStub;
import cn.vertxup.ambient.service.file.DocBuilder;
import io.horizon.eon.VString;
import io.horizon.spi.business.ExIo;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.atom.AtConfig;
import io.vertx.mod.ambient.init.AtPin;
import io.vertx.mod.ke.cv.em.BizInternal;
import io.vertx.up.boot.di.DiPlugin;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;

import static io.vertx.mod.ambient.refine.At.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AtFsDir {
    private static final Annal LOGGER = Annal.get(AtFsDir.class);
    private static final DiPlugin PLUGIN = DiPlugin.create(AtFs.class);

    /*
     * Here are file upload for directory calculation
     * 1) directory processing first
     * 2) directory generation with ExIo class
     * 3) Add the new information ( directoryId ) into each attachment.
     */
    static Future<JsonArray> fileDir(final JsonArray attachment, final JsonObject params) {

        /*
         * Step 1:
         * Re-calculate `storePath` based on `directory` field value here.
         * The version of `fromExpression` new is advanced feature:
         * 1) When `directory` is empty                         ( Do not Parsing )
         * 2) When `directory` contains "`" character           ( Format Expression )
         * 3) When `directory` does not contain "`" character   ( Do not Parsing )
         *
         * This version is more safer because the system will detect the original input
         * string value to check whether it's could be parsed.
         */
        final String directory = Ut.valueString(attachment, KName.DIRECTORY);
        final String storePath = Ut.fromExpression(directory, params);


        /*
         * Step 2:
         * 1) Extract `appId` from params
         * 2) Fetch the directories based on
         * -- storePath:  Convert to Set<String> ( ladder format of store path )
         * -- sigma:      The uniform sigma field of current process.
         */
        final String sigma = params.getString(KName.SIGMA);
        final List<String> paths = Ut.ioPathSet(storePath);

        return Ux.channel(ExIo.class, () -> null, io -> io.dirTree(sigma, paths).compose(directories -> {


            /*
             * Step 3:
             * Processing the directory tree based on name, the initialize structure:
             *
             * The default tree should be
             * /apps/xc/document
             * /apps/xc/document/XXXX  ( Root )
             *
             * The input may be:
             *
             * /apps/xc/document/XXXX/AA/BB
             *
             * Because in this situation the `AA` and `BB` directory must be valid, in this kind of
             * situation the following two API process this kind of situation
             *
             * 1) seekDirectory     ( Build the Root )
             * 2) dirTree           ( Re-fetch the left directories )
             */
            if (directories.isEmpty()) {
                return seekDirectory(storePath, params);
                // Double Checking for Path Resolution
                // .compose(nil -> io.dirTree(sigma, paths));
            } else {
                return Ux.future(directories);
            }
        }).compose(directoryA -> {


            /*
             * Step 4:
             * Configured directory for storing, for example:
             * 1. /apps/xc/document             ( Root Folder )
             * 2. /apps/xc/document/xxxx        ( store Path of current attachment )
             */
            final JsonObject input = new JsonObject();
            input.put(KName.STORE_PATH, Ut.toJArray(paths));
            input.put(KName.SIGMA, sigma);
            input.put(KName.UPDATED_BY, params.getValue(KName.UPDATED_BY));
            return io.verifyIn(directoryA, input);
        })).compose(directoryJ -> {


            /*
             * Replaced the field
             * - directoryId, refer to I_DIRECTORY record,                      key field
             * - storeWay, refer to I_DIRECTORY record,                         type field
             * - storePath, refer to calculated result here.  I_DIRECTORY storePath + name
             */
            final JsonObject verified = Ut.valueJObject(directoryJ);
            Ut.itJArray(attachment).forEach(content -> {
                content.put(KName.DIRECTORY_ID, verified.getString(KName.KEY));
                content.put(KName.STORE_PATH, Ut.ioPath(storePath, content.getString(KName.NAME)));
                content.put(KName.Attachment.STORE_WAY, verified.getString(KName.TYPE));
            });
            return Ux.future(attachment);
        });
    }

    private static Future<JsonArray> seekDirectory(final String storePath, final JsonObject params) {
        final DocBStub builder = PLUGIN.createSingleton(DocBuilder.class);
        /*
         * appId, type, name
         */
        final String appId = params.getString(KName.APP_ID);
        final String type = BizInternal.TypeEntity.Directory.value();
        /*
         * Calculate the `name`
         */
        final AtConfig config = AtPin.getConfig();
        final String rootPath = config.getStorePath();

        String name = storePath.replace(rootPath, VString.EMPTY);
        name = Ut.ioPathRoot(name);
        LOG.File.info(LOGGER, "Zero will re-initialize directory try to process {0}", storePath);
        LOG.File.info(LOGGER, "The builder parameters: name = {0}, type = {1}, appId = {2}",
            name, type, appId);
        return builder.initialize(appId, type, name);
    }
}
