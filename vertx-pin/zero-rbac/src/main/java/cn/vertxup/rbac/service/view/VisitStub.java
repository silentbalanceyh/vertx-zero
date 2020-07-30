package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.pojos.SVisitant;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;

import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public interface VisitStub {

    /*
     * Fetch Visitant by
     * 1) Analyze visitant syntax to calculate condition ( Json format )
     * 2) Combine all conditions that analyzed based on view
     * 3) DataBound stored all attached data here
     */
    Future<ConcurrentMap<String, SVisitant>> fetchVisitant(JsonObject condition);
}
