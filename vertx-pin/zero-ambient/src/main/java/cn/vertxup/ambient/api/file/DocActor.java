package cn.vertxup.ambient.api.file;

import cn.vertxup.ambient.service.file.DocBStub;
import cn.vertxup.ambient.service.file.DocRStub;
import cn.vertxup.ambient.service.file.DocWStub;
import io.vertx.core.Future;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.mod.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;
import jakarta.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class DocActor {
    @Inject
    private transient DocRStub reader;
    @Inject
    private transient DocWStub writer;
    @Inject
    private transient DocBStub builder;

    // ---------------------- Read Operation -------------------------
    @Address(Addr.Doc.DOCUMENT)
    public Future<JsonArray> start(final String type, final String appId) {
        return this.builder.initialize(appId, type);
    }

    @Address(Addr.Doc.BY_DIRECTORY)
    public Future<JsonArray> byDirectory(final String directoryId, final XHeader header) {
        /* Directory + Attachment */
        return this.reader.fetchDoc(header.getSigma(), directoryId);
    }

    @Address(Addr.Doc.BY_KEYWORD)
    public Future<JsonArray> byKeyword(final String keyword, final XHeader header) {
        /* Attachment Only */
        return this.reader.searchDoc(header.getSigma(), keyword);
    }

    @Address(Addr.Doc.BY_TRASHED)
    public Future<JsonArray> byTrashed(final XHeader header) {
        /*
         * Directory + Attachment
         * active = false
         * sigma match
         * */
        return this.reader.fetchTrash(header.getSigma());
    }

    @Address(Addr.File.DOWNLOADS)
    public Future<Buffer> download(final JsonArray keys) {
        return this.reader.downloadDoc(Ut.toSet(keys));
    }

    // ---------------------- Write Operation -------------------------

    @Address(Addr.File.RENAME)
    public Future<JsonObject> rename(final JsonObject documentJ, final User user) {
        final String userKey = Ux.keyUser(user);
        documentJ.put(KName.UPDATED_BY, userKey);
        return this.writer.rename(documentJ);
    }

    @Address(Addr.File.UPLOAD_CREATION)
    @Me
    public Future<JsonArray> upload(final JsonArray documentA) {
        return this.writer.upload(documentA);
    }

    @Address(Addr.Doc.DOCUMENT_TRASH)
    public Future<JsonArray> trashIn(final JsonArray documentA, final User user) {
        final String userKey = Ux.keyUser(user);
        Ut.itJArray(documentA).forEach(document -> document.put(KName.UPDATED_BY, userKey));
        return this.writer.trashIn(documentA);
    }

    @Address(Addr.Doc.DOCUMENT_PURGE)
    public Future<JsonArray> trashKo(final JsonArray documentA) {
        return this.writer.trashKo(documentA);
    }

    @Address(Addr.Doc.DOCUMENT_ROLLBACK)
    public Future<JsonArray> trashOut(final JsonArray documentA, final User user) {
        final String userKey = Ux.keyUser(user);
        Ut.itJArray(documentA).forEach(document -> document.put(KName.UPDATED_BY, userKey));
        return this.writer.trashOut(documentA);
    }


}
