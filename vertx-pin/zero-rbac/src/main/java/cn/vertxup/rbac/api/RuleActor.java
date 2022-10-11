package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.daos.SPathDao;
import cn.vertxup.rbac.domain.tables.pojos.SPath;
import cn.vertxup.rbac.service.view.RuleStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScOwner;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.tp.rbac.cv.em.OwnerType;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@Queue
public class RuleActor {

    @Inject
    private transient RuleStub stub;

    @Address(Addr.Rule.FETCH_REGION)
    public Future<JsonArray> fetchRegion(final String type, final XHeader header) {

        // RUN_TYPE = ? AND SIGMA = ?
        final JsonObject condition = Ux.whereAnd()
            .put(KName.RUN_TYPE, type)
            .put(KName.SIGMA, header.getSigma());


        return Ux.Jooq.on(SPathDao.class)
            .<SPath>fetchAsync(condition)
            .compose(this.stub::regionAsync);
    }

    /*
     * 最终响应的数据格式根据 iConfig 中的定义执行
     */
    @Address(Addr.Rule.FETCH_REGION_VALUES)
    public Future<JsonObject> fetchValues(final String ownerId, final JsonObject pathJ,
                                          final Vis view) {
        final OwnerType ownerType = Ut.toEnum(() -> pathJ.getString(KName.RUN_TYPE),
            OwnerType.class, OwnerType.ROLE);
        final ScOwner owner = new ScOwner(ownerId, ownerType);
        owner.bind(view);

        return this.stub.regionAsync(pathJ, owner);
    }

    @Me
    @Address(Addr.Rule.SAVE_REGION)
    public Future<JsonObject> saveRegion(final String path, final JsonObject viewData) {
        return Ux.futureJ();
    }
}
