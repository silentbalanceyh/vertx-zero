package cn.vertxup.rbac.api;

import cn.vertxup.rbac.domain.tables.daos.SPacketDao;
import cn.vertxup.rbac.domain.tables.daos.SPathDao;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
@Queue
public class CriterionActor {

    @Address(Addr.Rule.FETCH_BY_SIGMA)
    public Future<JsonArray> fetchAsync(final XHeader header) {
        return Ux.Jooq.on(SPathDao.class)
                .fetchAsync(KeField.SIGMA, header.getSigma())
                .compose(Ux::fnJArray);
    }

    @Address(Addr.Rule.FETCH_RULE_ITEMS)
    public Future<JsonArray> fetchItems(final String ruleId) {
        return Fn.getEmpty(Ux.futureJArray(), () -> Ux.Jooq.on(SPacketDao.class)
                .fetchAsync("pathId", ruleId)
                .compose(Ux::fnJArray), ruleId);
    }
}
