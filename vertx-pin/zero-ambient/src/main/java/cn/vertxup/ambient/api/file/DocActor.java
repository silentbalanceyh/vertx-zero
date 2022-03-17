package cn.vertxup.ambient.api.file;

import cn.vertxup.ambient.domain.tables.daos.XAttachmentDao;
import cn.vertxup.ambient.service.file.DocStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExIo;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class DocActor {
    @Inject
    private transient DocStub stub;

    @Address(Addr.Doc.DOCUMENT)
    public Future<JsonArray> start(final String type, final String appId) {
        return this.stub.treeAsync(appId, type);
    }

    @Address(Addr.Doc.BY_DIRECTORY)
    public Future<JsonArray> byDirectory(final String directoryId, final XHeader header) {
        /* Directory + Attachment */
        return Ke.channel(ExIo.class, JsonArray::new, io -> io.dirLs(header.getSigma(), directoryId))
            .compose(directory -> {
                final JsonObject condition = Ux.whereAnd();
                condition.put(KName.DIRECTORY_ID, directoryId);
                condition.put(KName.ACTIVE, Boolean.TRUE);
                return this.fetchFileAsync(condition).compose(files -> {
                    directory.addAll(files);
                    return Ux.future(directory);
                });
            });
    }

    @Address(Addr.Doc.BY_KEYWORD)
    public Future<JsonArray> byKeyword(final String keyword, final XHeader header) {
        /* Attachment Only */
        final JsonObject condition = Ux.whereAnd();
        condition.put("name,c", keyword);
        condition.put(KName.SIGMA, header.getSigma());
        condition.put(KName.ACTIVE, Boolean.TRUE);
        return this.fetchFileAsync(condition);
    }

    // --------------------------  Private Method for attachment ---------------------------
    private Future<JsonArray> fetchFileAsync(final JsonObject condition) {
        return Ux.Jooq.on(XAttachmentDao.class).fetchJAsync(condition)
            .compose(Ut.ifJArray(KName.METADATA))
            .compose(files -> {
                Ut.itJArray(files).forEach(file -> file.put(KName.DIRECTORY, Boolean.FALSE));
                return Ux.future(files);
            });
    }

    @Address(Addr.Doc.BY_TRASHED)
    public Future<JsonArray> byTrashed(final XHeader header) {
        /*
         * Directory + Attachment
         * active = false
         * sigma match
         * */
        return Ux.futureA();
    }
}
