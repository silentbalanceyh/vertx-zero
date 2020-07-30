package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public class VisitService implements VisitStub {
    /*
     * Fetch the visitant by `viewId = Visitant`
     *
     */
    @Override
    public Future<ConcurrentMap<String, SVisitant>> fetchVisitant(final JsonObject condition) {
        return null;
    }
}
