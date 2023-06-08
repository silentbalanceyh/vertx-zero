package cn.vertxup.ambient.service.file;

import cn.vertxup.ambient.domain.tables.daos.XAttachmentDao;
import cn.vertxup.ambient.domain.tables.pojos.XAttachment;
import io.horizon.atom.common.Kv;
import io.horizon.spi.business.ExIo;
import io.horizon.spi.feature.Attachment;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ke.error._400FileNameInValidException;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.vertx.mod.ambient.refine.At.LOG;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DocWriter implements DocWStub {
    private static final Annal LOGGER = Annal.get(DocWriter.class);

    @Inject
    private transient Attachment attachment;

    @Override
    public Future<JsonArray> upload(final JsonArray documentA) {
        /*
         * This method is used in document management only, it means that
         * the document management will put required field `directory` instead of
         * other field to linkage directory here.
         *
         * This directory will be parsed in AtFs instead of other method to be sure all
         * the directoryId be valid, if the directoryId is invalid, the system will not
         * move the files to actual storage, instead the template file storage will take
         * place.
         */
        return Ux.channel(Attachment.class, () -> documentA, file -> file.uploadAsync(documentA));
    }

    @Override
    public Future<JsonObject> rename(final JsonObject documentJ) {
        // isFileName Checking
        final String name = documentJ.getString(KName.NAME);
        if (!Ut.isFileName(name)) {
            return Fn.outWeb(_400FileNameInValidException.class, this.getClass());
        }
        final String key = documentJ.getString(KName.KEY);
        final UxJooq jq = Ux.Jooq.on(XAttachmentDao.class);
        return jq.<XAttachment>fetchByIdAsync(key).compose(attachment -> {
            final String from = attachment.getStorePath();
            final JsonObject documentData = documentJ.copy();
            Ut.valueToString(documentData, KName.METADATA, KName.VISIT_MODE);
            final XAttachment updated = Ux.updateT(attachment, documentData);
            return jq.updateAsync(updated).compose(processed -> {
                final String to = processed.getStorePath();
                return Ux.future(Kv.create(from, to));
            }).compose(kv -> {
                final String directoryId = attachment.getDirectoryId();
                final JsonObject directoryJ = new JsonObject();
                directoryJ.put(KName.KEY, directoryId);
                directoryJ.put(KName.UPDATED_BY, documentJ.getString(KName.UPDATED_BY));
                return Ux.channel(ExIo.class, () -> documentJ, io -> io.rename(directoryJ, kv)
                    .compose(nil -> Ux.future(documentJ)));
            });
        });
    }

    /*
     *  Move to trash instead of actual operation
     *  -- 1. Directory Operation
     *  -- 2. Attachment Operation
     *  The trash data structure is as following:
     *  {
     *      "key": "UUID Primary Key",
     *      "directory": "true for Directory, false for Attachment",
     *      "storePath": "Actual Stored Path here"
     *  }
     */
    @Override
    public Future<JsonArray> trashIn(final JsonArray documentA) {
        return this.trashSplit(documentA,
            // Update all attachmentA
            (attachmentA) -> this.attachment.updateAsync(attachmentA, Boolean.FALSE),
            (directoryA, attachmentA) -> {
                final ConcurrentMap<String, String> fileMap = Ut.elementMap(attachmentA, KName.STORE_PATH, KName.DIRECTORY_ID);
                return Ux.channel(ExIo.class, () -> documentA, fs -> fs.trashIn(directoryA, fileMap));
            }
        );
    }

    @Override
    public Future<JsonArray> trashOut(final JsonArray documentA) {
        return this.trashSplit(documentA,
            // Update all attachmentA
            (attachmentA) -> this.attachment.updateAsync(attachmentA, Boolean.TRUE),
            (directoryA, attachmentA) -> {
                final ConcurrentMap<String, String> fileMap = Ut.elementMap(attachmentA, KName.STORE_PATH, KName.DIRECTORY_ID);
                return Ux.channel(ExIo.class, () -> documentA, fs -> fs.trashOut(directoryA, fileMap));
            }
        );
    }

    @Override
    public Future<JsonArray> trashKo(final JsonArray documentA) {
        return this.trashSplit(documentA,
            // Delete Record Only
            this.attachment::purgeAsync,
            (directoryA, attachmentA) -> {
                final ConcurrentMap<String, String> fileMap = Ut.elementMap(attachmentA, KName.STORE_PATH, KName.DIRECTORY_ID);
                return Ux.channel(ExIo.class, () -> documentA,
                    // Kill Directory and All Sub Files
                    fs -> fs.purge(directoryA, fileMap).compose(this::trashKoDeep));
            });
    }

    // ----------------------------- Private Method -------------------------

    private Future<JsonArray> trashKoDeep(final JsonArray directory) {
        final JsonObject children = Ux.whereOr();
        Ut.itJArray(directory).forEach(json -> {
            final String storePath = json.getString(KName.STORE_PATH);
            if (Ut.isNotNil(storePath)) {
                final JsonObject child = Ux.whereAnd();
                child.put(KName.STORE_PATH + ",s", json.getString(KName.STORE_PATH));
                child.put(KName.ACTIVE, Boolean.FALSE);
                children.put("$" + json.getString(KName.CODE) + "$", child);
            }
        });
        return Ux.Jooq.on(XAttachmentDao.class).deleteByAsync(children)
            .compose(nil -> Ux.future(directory));
    }

    /*
     * The critical method to split Json Array by `directory`
     * - directory = true,        Directory Processing
     * - directory = false,       Attachment Processing
     */
    private Future<JsonArray> trashSplit(
        final JsonArray source,
        final Function<JsonArray, Future<JsonArray>> fnAttachment,
        final BiFunction<JsonArray, JsonArray, Future<JsonArray>> fnDirectory) {
        final JsonArray attachmentJ = new JsonArray();
        final JsonArray directoryJ = new JsonArray();
        Ut.itJArray(source).forEach(item -> {
            // Default Document Should be Attachment
            final Boolean directory = item.getBoolean(KName.DIRECTORY, Boolean.FALSE);
            if (directory) {
                directoryJ.add(item);
            } else {
                attachmentJ.add(item);
            }
        });
        LOG.File.info(LOGGER, "Split Running: Document = {0}, Directory = {1}", attachmentJ.size(), directoryJ.size());
        // XAttachment First
        if (Ut.isNotNil(directoryJ)) {
            return Ux.future(attachmentJ).compose(fnAttachment)
                // Then IDirectory
                .compose(processed -> fnDirectory.apply(directoryJ, processed).compose(directory -> {
                    // Response ( Attachments + Directory )
                    final JsonArray documents = new JsonArray();
                    documents.addAll(processed);
                    documents.addAll(directory);
                    return Ux.future(documents);
                }));
        } else {
            return Ux.future(attachmentJ).compose(fnAttachment);
        }
    }
}
