package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.daos.SPacketDao;
import cn.vertxup.rbac.domain.tables.daos.SPathDao;
import cn.vertxup.rbac.domain.tables.pojos.SPath;
import cn.vertxup.rbac.service.view.RuleStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
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
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.RUN_TYPE, type);
        condition.put(KName.SIGMA, header.getSigma());
        return Ux.Jooq.on(SPathDao.class)
            .<SPath>fetchAsync(condition)
            .compose(this.stub::procAsync);
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
