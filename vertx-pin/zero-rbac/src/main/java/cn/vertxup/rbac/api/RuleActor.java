package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.daos.SPathDao;
import cn.vertxup.rbac.domain.tables.pojos.SPath;
import cn.vertxup.rbac.service.view.RuleStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.auth.User;
import io.vertx.tp.rbac.atom.ScOwner;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Me;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.query.Sorter;
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
    public Future<JsonArray> fetchRegions(final String type, final XHeader header) {
        // RUN_TYPE = ? AND SIGMA = ?
        final JsonObject condition = Ux.whereAnd()
            .put(KName.RUN_TYPE, type)
            .put(KName.SIGMA, header.getSigma());

        return Ux.Jooq.on(SPathDao.class)
            .<SPath>fetchAsync(condition, Sorter.create(KName.UI_SORT, true))
            .compose(Ux::futureA);
    }

    @Address(Addr.Rule.FETCH_REGION_DEFINE)
    public Future<JsonObject> fetchRegion(final String key) {
        return Ux.Jooq.on(SPathDao.class)
            .<SPath>fetchByIdAsync(key)
            .compose(this.stub::regionAsync);
    }

    /*
     * 最终响应的数据格式根据 iConfig 中的定义执行
     */
    @Address(Addr.Rule.FETCH_REGION_VALUES)
    public Future<JsonObject> fetchValues(final String ownerId, final JsonObject pathJ,
                                          final Vis view) {
        final ScOwner owner = new ScOwner(ownerId, pathJ.getString(KName.RUN_TYPE));
        owner.bind(view);
        return this.stub.regionAsync(pathJ, owner);
    }


    @Me
    @Address(Addr.Rule.SAVE_REGION)
    public Future<JsonObject> saveRegion(final String pathCode, final JsonObject viewData,
                                         final User user) {
        /*
         * {
         *     "owner":         ??,
         *     "ownerType":     ??,
         *     "name":          ??,
         *     "position":      ??,
         *     "active":        true,
         *     "language":      cn,
         *     "sigma":         xxx,
         *     "resource": {
         *         "code1": {
         *              "rows":          ??,
         *              "projection":    ??,
         *              "criteria":      ??,
         *              "view":          ??,
         *              "position":      ??
         *         }
         *     }
         * }
         * 转换
         * {
         *     "resource-code1": {
         *     },
         *     "resource-code2": {
         *     }
         * }
         * -- 追加 owner, ownerType, ( view -> name ), position, updatedBy
         */
        final JsonObject resourceBody = new JsonObject();
        final JsonObject resourceI = Ut.valueJObject(viewData, KName.RESOURCE);
        final String userKey = Ux.keyUser(user);
        Ut.<JsonObject>itJObject(resourceI, (eachJ, field) -> {
            final JsonObject resourceJ = eachJ.copy();
            Ut.elementCopy(resourceJ, viewData,
                KName.SIGMA, KName.OWNER, KName.OWNER_TYPE
            );
            resourceJ.put(KName.NAME, resourceJ.getValue(KName.VIEW));
            resourceJ.put(KName.UPDATED_BY, userKey);
            resourceBody.put(field, resourceJ);
        });
        // CODE = ? AND SIGMA = ?
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.SIGMA, Ut.valueString(viewData, KName.SIGMA));
        condition.put(KName.CODE, pathCode);
        return this.stub.regionAsync(condition, resourceBody);
    }
}
