package cn.vertxup.ambient.service.file;

import cn.vertxup.ambient.domain.tables.daos.XAttachmentDao;
import cn.vertxup.ambient.domain.tables.pojos.XAttachment;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExIo;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class DocWriter implements DocWStub {
    @Override
    public Future<JsonArray> upload(final JsonArray documentA) {
        return null;
    }

    @Override
    public Future<JsonObject> rename(final JsonObject documentJ) {
        return null;
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
        return this.runSplit(documentA,
            // Update all attachmentA
            (attachmentA) -> this.updateAttachment(attachmentA, Boolean.FALSE),
            (directoryA, attachmentA) -> {
                final ConcurrentMap<String, String> fileMap = Ut.elementMap(attachmentA, KName.STORE_PATH, KName.DIRECTORY_ID);
                return Ke.channel(ExIo.class, () -> documentA, stub -> stub.trashIn(directoryA, fileMap));
            }
        );
    }

    private Future<JsonArray> updateAttachment(final JsonArray attachmentJ, final boolean active) {
        Ut.ifStrings(attachmentJ, KName.METADATA);
        Ut.itJArray(attachmentJ).forEach(attachment -> {
            attachment.put(KName.UPDATED_AT, Instant.now());
            attachment.put(KName.ACTIVE, active);
        });
        final List<XAttachment> attachments = Ux.fromJson(attachmentJ, XAttachment.class);
        return Ux.Jooq.on(XAttachmentDao.class).updateAsyncJ(attachments)
            .compose(Ut.ifJArray(KName.METADATA));
    }

    @Override
    public Future<JsonArray> trashOut(final JsonArray documentA) {
        return this.runSplit(documentA,
            // Update all attachmentA
            (attachmentA) -> this.updateAttachment(attachmentA, Boolean.TRUE),
            (directoryA, attachmentA) -> {
                final ConcurrentMap<String, String> fileMap = Ut.elementMap(attachmentA, KName.STORE_PATH, KName.DIRECTORY_ID);
                return Ke.channel(ExIo.class, () -> documentA, stub -> stub.trashOut(directoryA, fileMap));
            }
        );
    }

    @Override
    public Future<JsonArray> trashKo(final JsonArray documentA) {
        return null;
    }

    /*
     * The critical method to split Json Array by `directory`
     * - directory = true,        Directory Processing
     * - directory = false,       Attachment Processing
     */
    private Future<JsonArray> runSplit(
        final JsonArray source,
        final Function<JsonArray, Future<JsonArray>> fnAttachment,
        final BiFunction<JsonArray, JsonArray, Future<JsonArray>> fnDirectory) {
        final JsonArray attachmentJ = new JsonArray();
        final JsonArray directoryJ = new JsonArray();
        Ut.itJArray(source).forEach(item -> {
            final Boolean directory = item.getBoolean(KName.DIRECTORY, Boolean.TRUE);
            if (directory) {
                directoryJ.add(item);
            } else {
                attachmentJ.add(item);
            }
        });
        return Ux.future(directoryJ)
            // XAttachment First
            .compose(fnAttachment)
            // Then IDirectory
            .compose(processed -> fnDirectory.apply(directoryJ, processed));
    }
}
