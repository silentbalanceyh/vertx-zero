package cn.vertxup.rbac.api;

import cn.vertxup.rbac.service.business.GroupStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.rbac.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;

import javax.inject.Inject;

@Queue
public class GroupActor {

    @Inject
    private transient GroupStub groupStub;

    @Address(Addr.Group.GROUP_SIGMA)
    public Future<JsonArray> fetchGroups(final String sigma) {
        return groupStub.fetchGroups(sigma);
    }
}
