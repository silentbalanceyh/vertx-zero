package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.daos.SPacketDao;
import cn.vertxup.rbac.domain.tables.daos.SPathDao;
import cn.vertxup.rbac.domain.tables.pojos.SPath;
import cn.vertxup.rbac.service.view.RuleStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

import javax.inject.Inject;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Queue
public class CriterionActor {

    @Inject
    private transient RuleStub stub;

    @Address(Addr.Rule.FETCH_BY_SIGMA)
    public Future<JsonArray> fetchAsync(final XHeader header) {
        return Ux.Jooq.on(SPathDao.class)
                .<SPath>fetchAsync(KeField.SIGMA, header.getSigma())
                .compose(this.stub::procAsync);
    }

    @Address(Addr.Rule.FETCH_RULE_ITEMS)
    public Future<JsonArray> fetchItems(final String ruleId) {
        return Fn.getEmpty(Ux.futureA(), () -> Ux.Jooq.on(SPacketDao.class)
                .fetchAsync("pathId", ruleId)
                .compose(Ux::futureA).compose(Ke.mounts(
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
