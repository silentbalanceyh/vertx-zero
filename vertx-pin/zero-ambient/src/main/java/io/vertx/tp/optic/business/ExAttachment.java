package io.vertx.tp.optic.business;

import cn.vertxup.ambient.domain.tables.daos.XAttachmentDao;
import cn.vertxup.ambient.domain.tables.pojos.XAttachment;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.refine.At;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.feature.Attachment;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
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
    public Future<JsonArray> saveAsync(final JsonObject condition, final JsonArray data) {
        Ut.ifStrings(data, KName.METADATA);
        return this.removeAsync(condition).compose(nil -> this.uploadAsync(data));
    }

    @Override
    public Future<JsonArray> uploadAsync(final JsonArray data) {
        Ut.ifStrings(data, KName.METADATA);
        final List<XAttachment> attachments = Ux.fromJson(data, XAttachment.class);
        return Ux.Jooq.on(XAttachmentDao.class).insertJAsync(attachments)

            // ExIo -> Call ExIo to impact actual file system ( Store )
            .compose(At::fileUpload)
            .compose(this::outAsync);
    }

    @Override
    public Future<JsonArray> removeAsync(final JsonObject condition) {
        return Ux.Jooq.on(XAttachmentDao.class).fetchJAsync(condition)
            .compose(attachments -> this.removeAsyncInternal(condition, attachments));
    }

    @Override
    public Future<JsonArray> removeAsync(final JsonArray attachment) {
        final JsonObject criteria = new JsonObject();
        criteria.put(KName.KEY + ",i", Ut.toJArray(Ut.valueSetString(attachment, KName.KEY)));
        return this.removeAsyncInternal(criteria, attachment);
    }


    @Override
    public Future<JsonArray> fetchAsync(final JsonObject condition) {
        return this.fetchAsyncInternal(condition).compose(this::outAsync);
    }

    @Override
    public Future<Buffer> downloadAsync(final Set<String> keys) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.KEY + ",i", Ut.toJArray(keys));
        return this.fetchAsyncInternal(condition)

            // ExIo -> Call ExIo to impact actual file system ( Store )
            .compose(At::fileDownload);
    }

    @Override
    public Future<Buffer> downloadAsync(final String key) {
        final JsonObject condition = new JsonObject();
        condition.put(KName.KEY, key);
        At.infoFile(LOGGER, "Fetch Operation, condition: {0}", condition);
        return Ux.Jooq.on(XAttachmentDao.class).fetchJOneAsync(condition)

            // ExIo -> Call ExIo to impact actual file system ( Store )
            .compose(At::fileDownload);
    }

    // ----------------- Private Method Interface ----------------------
    private Future<JsonArray> fetchAsyncInternal(final JsonObject condition) {
        At.infoFile(LOGGER, "Fetch Operation, condition: {0}", condition);
        return Ux.Jooq.on(XAttachmentDao.class).fetchJAsync(condition);
    }

    private Future<JsonArray> outAsync(final JsonArray files) {
        /*
         * Fetch `visit` information
         */
        final Set<String> keys = Ut.valueSetString(files, KName.DIRECTORY_ID);
        return Ke.channel(ExIo.class, () -> files, io -> io.dirBy(keys).compose(map -> {
            Ut.itJArray(files).forEach(file -> {
                final String directoryId = file.getString(KName.DIRECTORY_ID);
                final JsonObject directoryJ = map.getOrDefault(directoryId, new JsonObject());
                final JsonObject visitJ = Ut.elementSubset(directoryJ,
                    KName.VISIT_ROLE,
                    KName.VISIT_GROUP,
                    KName.VISIT,
                    KName.VISIT_MODE
                );
                /*
                 * visitMode switching
                 *
                 * 1. If directory contains "w",
                 * 2. The attachment should append "x" for rename/trash
                 */
                final JsonArray visitMode = visitJ.getJsonArray(KName.VISIT_MODE);
                if (visitMode.contains(KName.Attachment.W) &&
                    !visitMode.contains(KName.Attachment.X)) {
                    visitMode.add(KName.Attachment.X);
                    visitJ.put(KName.VISIT_MODE, visitMode);
                }
                file.mergeIn(visitJ, true);
            });
            return Ux.future(files);
        })).compose(Ut.ifJArray(KName.METADATA)).compose(attachments -> {
            Ut.itJArray(attachments).forEach(file -> file.put(KName.DIRECTORY, Boolean.FALSE));
            return Ux.future(attachments);
        });
    }

    private Future<JsonArray> removeAsyncInternal(final JsonObject condition, final JsonArray attachments) {
        At.infoFile(LOGGER, "Remove Operation, condition: {0}", condition);
        return Ux.Jooq.on(XAttachmentDao.class).deleteByAsync(condition)

            // ExIo -> Call ExIo to impact actual file system ( Store )
            .compose(removed -> At.fileRemove(attachments))
            .compose(this::outAsync);
    }
}
