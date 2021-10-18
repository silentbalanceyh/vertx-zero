package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.pojos.SGroup;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;

import java.util.List;

/*
 * Basic Group interface
 *
 */
public interface GroupStub {
    /**
     * R_GROUP_ROLE
     * <p>
     * groupKey -> Relation to role
     */
    Future<JsonArray> fetchRoleIdsAsync(String groupKey);

    JsonArray fetchRoleIds(String groupKey);

    SGroup fetchParent(String groupKey);

    List<SGroup> fetchChildren(String groupKey);

    /*
     * Get groups by : sigma = {xxx}
     */
    Future<JsonArray> fetchGroups(String sigma);
}
