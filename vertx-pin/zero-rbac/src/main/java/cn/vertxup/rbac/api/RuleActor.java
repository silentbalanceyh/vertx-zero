package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.daos.SPacketDao;
import cn.vertxup.rbac.domain.tables.daos.SPathDao;
import cn.vertxup.rbac.domain.tables.pojos.SPacket;
import cn.vertxup.rbac.domain.tables.pojos.SPath;
import cn.vertxup.rbac.service.view.RuleStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScOwner;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.tp.rbac.cv.em.OwnerType;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.atom.secure.Vis;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
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
    public Future<JsonObject> fetchValues(final String ownerId, final SPath path,
                                          final Vis view) {
        /*
         * 查找合法的被影响资源，此处会有很多种变化
         * - 每个Region影响的资源可能是多个值，也可能是一个值
         * - 由于Region在前端读取的时候已经是执行过 type 维度的条件，所以此处不再考虑 type 参数
         *   type 直接从 region 的 runType 中提取
         * - 此处提取时直接按照 region codes + sigma 二者的值来提取 Pocket 定义
         */
        final JsonObject condition = Ux.whereAnd()
            .put(KName.CODE, path.getCode())
            .put(KName.SIGMA, path.getSigma());


        // CODE = ? AND SIGMA = ?
        final OwnerType ownerType = Ut.toEnum(path::getRunType, OwnerType.class, OwnerType.ROLE);
        final ScOwner owner = new ScOwner(ownerId, ownerType);
        owner.bind(view);
        return Ux.Jooq.on(SPacketDao.class)
            .<SPacket>fetchAsync(condition)
            .compose(packets -> this.stub.regionAsync(packets, owner));
    }

    @Address(Addr.Rule.FETCH_RULE_ITEMS)
    public Future<JsonArray> fetchItems(final String ruleId) {
        return Fn.getEmpty(Ux.futureA(), () -> Ux.Jooq.on(SPacketDao.class)
            .fetchAsync("pathId", ruleId)
            .compose(Ux::futureA).compose(Ut.ifJArray(
                /*
                 * rows configuration
                 * When rows contains more than one, it need complex config
                 * format for future calculation
                 *
                 * 1. rowTpl: define basic data structure in our system
                 * 2. rowTplMapping: define mapping information between `source` and `target`
                 */
                "rowTpl",
                "rowTplMapping",
                /*
                 * projection configuration
                 * Freedom data format for projection definition
                 */
                "colConfig",
                /*
                 * criteria configuration
                 *
                 * 1. condTpl: define basic data structure in criteria condition
                 * 2. condTplMapping: define mapping information between `source` and `target`
                 * 3. condConfig: define configuration ( Freedom Format )
                 */
                "condTpl",
                "condTplMapping",
                "condConfig"
            )), ruleId);
    }
}
