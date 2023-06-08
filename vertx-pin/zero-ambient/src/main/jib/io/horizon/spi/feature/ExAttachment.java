package io.horizon.spi.feature;

import cn.vertxup.ambient.domain.tables.daos.XAttachmentDao;
import cn.vertxup.ambient.domain.tables.pojos.XAttachment;
import io.horizon.eon.em.typed.ChangeFlag;
import io.horizon.spi.business.ExIo;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ambient.refine.At;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import static io.vertx.mod.ambient.refine.At.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class ExAttachment implements Attachment {
    private static final Annal LOGGER = Annal.get(ExAttachment.class);


    @Override
    public Future<JsonArray> saveAsync(final JsonObject condition, final JsonArray data) {
        if (Ut.isNil(data)) {
            return Ux.futureA();
        } else {
            final JsonObject params = this.uploadParams(data);
            return this.saveAsync(condition, data, params);
        }
    }

    @Override
    public Future<JsonArray> saveAsync(final JsonObject condition, final JsonArray data, final JsonObject params) {
        /*
         * Do not remove and add here because the existing attachments will be purged from
         * Storage, in this kind of situation, here we should do the actual saving
         *
         * 1) Comparing the old attachments and new attachments
         * 2) Get the ADD / UPDATE / DELETE queue for each channel
         * - 2.1) For DELETE queue: Remove from the system and storage
         * - 2.2) For UPDATE queue: Do nothing
         * - 2.3) For ADD queue: Do uploading on new added attachments.
         *
         * But here should re-design the way to get three queues.
         */
        final UxJooq jq = Ux.Jooq.on(XAttachmentDao.class);
        return jq.fetchJAsync(condition).compose(original -> {
            final ConcurrentMap<ChangeFlag, JsonArray> compared =
                Ux.compareJ(original, data, KName.KEY);
            /*
             * Delete First
             */
            final JsonArray deleted = compared.getOrDefault(ChangeFlag.DELETE, new JsonArray());
            // Delete
            return jq.deleteJAsync(deleted)
                .compose(nil -> At.fileRemove(deleted))
                // Add
                .compose(nil -> {
                    final JsonArray added = compared.getOrDefault(ChangeFlag.ADD, new JsonArray());
                    return this.uploadAsync(added, params);
                })
                // Combine Update and Returned
                .compose(added -> {
                    final JsonArray combine = compared.getOrDefault(ChangeFlag.UPDATE, new JsonArray());
                    if (Ut.isNotNil(added)) {
                        combine.addAll(added);
                    }
                    return Ux.future(combine);
                });
        });
    }

    @Override
    public Future<JsonArray> uploadAsync(final JsonArray data) {
        /*
         * Fix issue of NullPointer when executing AtFs Processing
         */
        final JsonObject params = this.uploadParams(data);
        return this.uploadAsync(data, params);
    }

    private JsonObject uploadParams(final JsonArray data) {
        final String sigma = Ut.valueString(data, KName.SIGMA);
        final String directory = Ut.valueString(data, KName.DIRECTORY);
        final String updatedBy = Ut.valueString(data, KName.UPDATED_BY);
        final JsonObject params = new JsonObject();
        params.put(KName.SIGMA, sigma);
        params.put(KName.DIRECTORY, directory);
        params.put(KName.UPDATED_BY, updatedBy);
        return params;
    }

    @Override
    public Future<JsonArray> uploadAsync(final JsonArray data, final JsonObject params) {
        if (Ut.isNil(data)) {
            return Ux.futureA();
        } else {
            return At.fileDir(data, params).compose(normalized -> {
                // Fix: com.fasterxml.jackson.databind.exc.MismatchedInputException:
                // Cannot deserialize value of type `java.lang.String` from Object value (token `JsonToken.START_OBJECT`)
                Ut.valueToString(normalized, KName.METADATA);
                final List<XAttachment> attachments = Ux.fromJson(normalized, XAttachment.class);
                return Ux.Jooq.on(XAttachmentDao.class).insertJAsync(attachments)

                    // ExIo -> Call ExIo to impact actual file system ( Store )
                    .compose(At::fileUpload)
                    .compose(this::outAsync);
            });
        }
    }

    @Override
    public Future<JsonArray> updateAsync(final JsonArray attachmentJ, final boolean active) {
        Ut.valueToString(attachmentJ, KName.METADATA);
        Ut.itJArray(attachmentJ).forEach(attachment -> {
            attachment.put(KName.UPDATED_AT, Instant.now());
            attachment.put(KName.ACTIVE, active);
        });
        final List<XAttachment> attachments = Ux.fromJson(attachmentJ, XAttachment.class);
        return Ux.Jooq.on(XAttachmentDao.class).updateAsyncJ(attachments)
            .compose(Fn.ofJArray(KName.METADATA));
    }

    @Override
    public Future<JsonArray> removeAsync(final JsonObject condition) {
        return Ux.Jooq.on(XAttachmentDao.class).fetchJAsync(condition)
            .compose(attachments -> this.removeAsyncInternal(condition, attachments));
    }

    @Override
    public Future<JsonArray> purgeAsync(final JsonArray attachment) {
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
        LOG.File.info(LOGGER, "Fetch Operation, condition: {0}", condition);
        return Ux.Jooq.on(XAttachmentDao.class).fetchJOneAsync(condition)

            // ExIo -> Call ExIo to impact actual file system ( Store )
            .compose(At::fileDownload);
    }

    // ----------------- Private Method Interface ----------------------
    private Future<JsonArray> fetchAsyncInternal(final JsonObject condition) {
        LOG.File.info(LOGGER, "Fetch Operation, condition: {0}", condition);
        return Ux.Jooq.on(XAttachmentDao.class).fetchJAsync(condition);
    }

    private Future<JsonArray> outAsync(final JsonArray files) {
        /*
         * Fetch `visit` information
         */
        final Set<String> keys = Ut.valueSetString(files, KName.DIRECTORY_ID);
        return Ux.channel(ExIo.class, () -> files, io -> io.dirBy(keys).compose(map -> {
            Ut.itJArray(files).forEach(file -> {
                final String directoryId = file.getString(KName.DIRECTORY_ID);
                if (Ut.isNotNil(directoryId)) {
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
                    final JsonArray visitMode = Ut.valueJArray(visitJ, KName.VISIT_MODE);
                    if (visitMode.contains(KName.Attachment.W) &&
                        !visitMode.contains(KName.Attachment.X)) {
                        visitMode.add(KName.Attachment.X);
                        visitJ.put(KName.VISIT_MODE, visitMode);
                    }
                    file.mergeIn(visitJ, true);
                }
            });
            return Ux.future(files);
        })).compose(Fn.ofJArray(KName.METADATA)).compose(attachments -> {
            Ut.itJArray(attachments).forEach(file -> file.put(KName.DIRECTORY, Boolean.FALSE));
            return Ux.future(attachments);
        });
    }

    private Future<JsonArray> removeAsyncInternal(final JsonObject condition, final JsonArray attachments) {
        LOG.File.info(LOGGER, "Remove Operation, condition: {0}", condition);
        return Ux.Jooq.on(XAttachmentDao.class).deleteByAsync(condition)

            // ExIo -> Call ExIo to impact actual file system ( Store )
            .compose(removed -> At.fileRemove(attachments))
            .compose(this::outAsync);
    }
}
