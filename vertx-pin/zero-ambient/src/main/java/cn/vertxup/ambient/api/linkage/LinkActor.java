package cn.vertxup.ambient.api.linkage;

import cn.vertxup.ambient.domain.tables.daos.XLinkageDao;
import cn.vertxup.ambient.service.linkage.LinkStub;
import com.google.inject.Inject;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class LinkActor {

    @Inject
    private transient LinkStub stub;

    @Address(Addr.Linkage.FETCH_BY_TYPE)
    public Future<JsonArray> fetchBy(final String type, final XHeader header) {
        return this.stub.fetchByType(type, header.getSigma());
    }

    @Address(Addr.Linkage.FETCH_TARGET)
    public Future<JsonArray> fetchTarget(final String key) {
        return this.stub.fetchNorm(key, null);
    }

    @Address(Addr.Linkage.FETCH_SOURCE)
    public Future<JsonArray> fetchSource(final String key) {
        return this.stub.fetchNorm(null, key);
    }

    @Address(Addr.Linkage.FETCH_ST)
    public Future<JsonArray> fetchSt(final String key) {
        return this.stub.fetchNorm(key, key);
    }

    @Me
    @Address(Addr.Linkage.SAVE_BATCH_B)
    public Future<JsonArray> batchSaveB(final JsonArray data) {
        return this.stub.saving(data, false);
    }

    @Me
    @Address(Addr.Linkage.SAVE_BATCH_V)
    public Future<JsonArray> batchSaveV(final JsonArray data) {
        return this.stub.saving(data, true);
    }

    @Me
    @Address(Addr.Linkage.ADD_NEW_B)
    public Future<JsonObject> addNewB(final JsonObject data) {
        return this.stub.create(data, false);
    }

    @Me
    @Address(Addr.Linkage.ADD_NEW_V)
    public Future<JsonObject> addNewV(final JsonObject data) {
        return this.stub.create(data, true);
    }

    @Address(Addr.Linkage.FETCH_BY_KEY)
    public Future<JsonObject> byKey(final String key) {
        return Ux.Jooq.on(XLinkageDao.class).fetchJByIdAsync(key);
    }

    @Address(Addr.Linkage.REMOVE_BY_KEY)
    public Future<Boolean> removeKey(final String key) {
        return Ux.Jooq.on(XLinkageDao.class).deleteByIdAsync(key);
    }

}
