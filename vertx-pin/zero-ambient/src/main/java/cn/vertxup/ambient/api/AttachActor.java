package cn.vertxup.ambient.api;

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
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

@Queue
public class AttachActor {

    private static final Annal LOGGER = Annal.get(AttachActor.class);

    @Address(Addr.File.UPLOAD)
    public Future<JsonObject> upload(final JsonObject content) {
        At.infoFile(LOGGER, AtMsg.FILE_UPLOAD, content.encodePrettily());
        final XAttachment attachment = Ut.deserialize(content, XAttachment.class);
        return Ux.Jooq.on(XAttachmentDao.class)
            .insertAsync(attachment)
            .compose(Ux::futureJ);
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
