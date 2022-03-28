package cn.vertxup.ambient.api.file;

import cn.vertxup.ambient.service.file.DocRStub;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.tp.ambient.cv.AtMsg;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExIo;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;

@Queue
public class AttachActor {

    private static final Annal LOGGER = Annal.get(AttachActor.class);

    @Inject
    private transient DocRStub reader;

    @Address(Addr.File.UPLOAD)
    public Future<JsonObject> upload(final JsonObject content, final XHeader header) {
        /*
         * New file processing workflow, here should be careful
         * 1. ADD:
         * -- 1.1. Upload the file to server ( Do not insert record into database )
         * -- 1.2. When add record, based configured the file field in CRUD, insert the record
         *
         * 2. EDIT:
         * -- 2.1. Upload the file to server
         * -- 2.2. Remove all the related attachment and files
         * -- 2.3. Update all the attachments
         */
        At.infoFile(LOGGER, AtMsg.FILE_UPLOAD, content.encodePrettily());
        Ut.ifJObject(content, KName.METADATA);
        /*
         * ExIo to extract from
         *
         * directory ( Code ) -> directoryId
         */
        if (content.containsKey(KName.DIRECTORY)) {
            final String sigma = header.getSigma();
            final String directory = content.getString(KName.DIRECTORY);
            return Ke.channel(ExIo.class, () -> content, io -> io.dirBy(sigma, directory).compose(directoryJ -> {
                /*
                 * Replaced the field
                 * - directoryId, refer to I_DIRECTORY record,                      key field
                 * - storeWay, refer to I_DIRECTORY record,                         type field
                 * - storePath, refer to calculated result here.  I_DIRECTORY storePath + name
                 */
                content.put(KName.DIRECTORY_ID, directoryJ.getString(KName.KEY));
                final String folder = directoryJ.getString(KName.STORE_PATH);
                content.put(KName.STORE_PATH, Ut.ioPath(folder, content.getString(KName.NAME)));
                content.put(KName.Attachment.STORE_WAY, directoryJ.getString(KName.TYPE));
                return Ux.future(content);
            }));
        } else {
            return Ux.future(content);
        }
    }

    @Address(Addr.File.DOWNLOAD)
    public Future<Buffer> download(final JsonObject filters) {
        At.infoFile(LOGGER, AtMsg.FILE_DOWNLOAD, filters.encodePrettily());
        return this.reader.downloadDoc(filters.getString(KName.KEY));
    }
}
