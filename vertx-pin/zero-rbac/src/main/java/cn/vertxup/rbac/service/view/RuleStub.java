package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.pojos.SPath;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">lang</a>
 */
public interface RuleStub {
    /*
     * Process SPath
     */
    Future<JsonArray> procAsync(final List<SPath> paths);

    /*
     * Fetch all views that belong to
     *
     * 1) ownerType: USER | ROLE
     * 2) ownerId: user id | role id
     * 3) keys: resource ids that act as condition
     * 4) view: default view name: DEFAULT
     */
    Future<JsonArray> fetchViews(String ownerType, String ownerId,
                                 JsonArray keys, String view);

    Future<JsonArray> saveViews(String ownerType, String ownerId,
                                JsonArray keys, String view);
}
