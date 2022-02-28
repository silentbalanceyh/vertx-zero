package io.vertx.tp.optic.business;

import cn.vertxup.ambient.domain.tables.daos.XAttachmentDao;
import cn.vertxup.ambient.domain.tables.pojos.XAttachment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.optic.feature.Attachment;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Set;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExAttachment implements Attachment {
    private static final Annal LOGGER = Annal.get(ExAttachment.class);

    @Override
    public Future<JsonArray> createAsync(final JsonArray data) {
        Ut.ifStrings(data, KName.METADATA);
        final List<XAttachment> attachments = Ux.fromJson(data, XAttachment.class);
        return Ux.Jooq.on(XAttachmentDao.class).insertJAsync(attachments);
    }

    @Override
    public Future<JsonArray> saveAsync(final JsonObject condition, final JsonArray data) {
        Ut.ifStrings(data, KName.METADATA);
        final List<XAttachment> attachments = Ux.fromJson(data, XAttachment.class);
        At.infoFile(LOGGER, "Save Operation, condition: {0}", condition);
        return this.removeAsync(condition)
            .compose(nil -> Ux.Jooq.on(XAttachmentDao.class).insertJAsync(attachments));
    }

    @Override
    public Future<Boolean> removeAsync(final JsonObject condition) {
        At.infoFile(LOGGER, "Remove Operation, condition: {0}", condition);
        final UxJooq jq = Ux.Jooq.on(XAttachmentDao.class);
        return jq.<XAttachment>fetchAsync(condition).compose(attachments -> {
            // Remove Actual File on Server System
            final Set<String> files = Ut.elementSet(attachments, XAttachment::getFilePath);
            Ut.ioDelete(files);
            At.infoFile(LOGGER, "Deleted files: {0}", String.valueOf(files.size()));
            return jq.deleteByAsync(condition);
        });
    }

    @Override
    public Future<JsonArray> fetchAsync(final JsonObject condition) {
        At.infoFile(LOGGER, "Fetch Operation, condition: {0}", condition);
        return Ux.Jooq.on(XAttachmentDao.class).fetchJAsync(condition)
            .compose(Ut.ifJArray(KName.METADATA));
    }
}