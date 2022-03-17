package cn.vertxup.ambient.api.file;

import cn.vertxup.ambient.service.file.DocStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;

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
        return this.stub.fetchDoc(header.getSigma(), directoryId);
    }

    @Address(Addr.Doc.BY_KEYWORD)
    public Future<JsonArray> byKeyword(final String keyword, final XHeader header) {
        /* Attachment Only */
        return this.stub.searchDoc(header.getSigma(), keyword);
    }

    // --------------------------  Private Method for attachment ---------------------------

    @Address(Addr.Doc.BY_TRASHED)
    public Future<JsonArray> byTrashed(final XHeader header) {
        /*
         * Directory + Attachment
         * active = false
         * sigma match
         * */
        return this.stub.fetchTrash(header.getSigma());
    }
}
