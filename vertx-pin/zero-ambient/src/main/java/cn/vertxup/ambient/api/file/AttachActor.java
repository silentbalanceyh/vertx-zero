package cn.vertxup.ambient.api.file;

import cn.vertxup.ambient.domain.tables.daos.XAttachmentDao;
import cn.vertxup.ambient.domain.tables.pojos.XAttachment;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.tp.ambient.cv.AtMsg;
import io.vertx.tp.ambient.refine.At;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.Envelop;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

@Queue
public class AttachActor {

    private static final Annal LOGGER = Annal.get(AttachActor.class);

    @Address(Addr.File.UPLOAD)
    public Future<JsonObject> upload(final Envelop envelop) {
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
        final JsonObject content = envelop.body();
        At.infoFile(LOGGER, AtMsg.FILE_UPLOAD, content.encodePrettily());
        return Ux.future(content);

        /*
        final XAttachment attachment = Ut.deserialize(content, XAttachment.class);
        final String userKey = Ux.keyUser(envelop.user());
        if (Objects.nonNull(userKey)) {
            attachment.setCreatedBy(userKey);
        }
        attachment.setCreatedAt(LocalDateTime.now());

        return Ux.Jooq.on(XAttachmentDao.class)
            .insertAsync(attachment)
            .compose(Ux::futureJ);
        */
    }

    @Address(Addr.File.DOWNLOAD)
    public Future<Buffer> download(final JsonObject filters) {
        At.infoFile(LOGGER, AtMsg.FILE_DOWNLOAD, filters.encodePrettily());
        return Ux.Jooq.on(XAttachmentDao.class)
            .<XAttachment>fetchOneAsync(filters)
            .compose(entity -> {
                Buffer buffer = Buffer.buffer();
                if (Objects.nonNull(entity)) {
                    buffer = Ut.ioBuffer(entity.getFilePath());
                }
                return Ux.future(buffer);
            });
    }
}
