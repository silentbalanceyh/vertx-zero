package cn.vertxup.ambient.api.file;

import cn.vertxup.ambient.domain.tables.daos.XAttachmentDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.tp.ke.cv.em.FileStatus;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.optic.business.ExIo;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class FileActor {


    @Address(Addr.File.MY_QUEUE)
    public Future<JsonObject> searchMy(final JsonObject query,
                                       final User user) {
        // JsonObject on `my queue` criteria
        final JsonObject qrDefault = Ux.whereAnd();
        qrDefault.put(KName.STATUS, FileStatus.DONE.name());
        qrDefault.put(KName.ACTIVE, Boolean.TRUE);
        qrDefault.put(KName.CREATED_BY, Ux.keyUser(user));
        final JsonObject qrCombine = Ux.whereQrA(query, "$DFT$", qrDefault);
        return Ux.Jooq.on(XAttachmentDao.class).searchAsync(qrCombine);
    }

    @Address(Addr.File.BY_KEY)
    public Future<JsonObject> fileByKey(final String key) {
        return Ux.Jooq.on(XAttachmentDao.class).fetchJByIdAsync(key);
    }

    @Address(Addr.File.BY_DIRECTORY)
    public Future<JsonArray> fileByDirectory(final String directoryId, final XHeader header) {
        /*
         * Directory + Attachment
         */
        final Future<JsonArray> future;
        if (Ut.isNil(directoryId)) {
            future = Ke.channel(ExIo.class, JsonArray::new, io -> io.dirLsR(header.getSigma()));
        } else {
            future = Ke.channel(ExIo.class, JsonArray::new, io -> io.dirLs(directoryId));
        }
        return future.compose(directory -> {
            Ut.itJArray(directory).forEach(each -> {
                each.put(KName.DIRECTORY, Boolean.TRUE);
                Ut.ifJCopy(each, KName.KEY, KName.DIRECTORY_ID);
            });
            return Ux.future(directory);
        }).compose(directory -> {
            final JsonObject condition = Ux.whereAnd();
            condition.put(KName.DIRECTORY_ID, directoryId);
            condition.put(KName.ACTIVE, Boolean.TRUE);
            return this.fetchFileAsync(condition).compose(files -> {
                directory.addAll(files);
                return Ux.future(directory);
            });
        });
    }

    @Address(Addr.File.BY_KEYWORD)
    public Future<JsonArray> fileByKeyword(final String keyword, final XHeader header) {
        final JsonObject condition = Ux.whereAnd();
        condition.put("name,c", keyword);
        condition.put(KName.SIGMA, header.getSigma());
        condition.put(KName.ACTIVE, Boolean.TRUE);
        return this.fetchFileAsync(condition);
    }

    private Future<JsonArray> fetchFileAsync(final JsonObject condition) {
        return Ux.Jooq.on(XAttachmentDao.class).fetchJAsync(condition)
            .compose(Ut.ifJArray(KName.METADATA))
            .compose(files -> {
                Ut.itJArray(files).forEach(file -> file.put(KName.DIRECTORY, Boolean.FALSE));
                return Ux.future(files);
            });
    }
}
