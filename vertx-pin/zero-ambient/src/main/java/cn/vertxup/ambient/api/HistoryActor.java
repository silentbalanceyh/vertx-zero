package cn.vertxup.ambient.api;

import cn.vertxup.ambient.service.ActivityStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;

@Queue
public class HistoryActor {
    @Inject
    private transient ActivityStub activityStub;

    @Address(Addr.History.HISTORIES)
    public Future<JsonArray> fetchHistory(final String identifier, final String key) {
        if (Ut.isNil(identifier) || Ut.isNil(key)) {
            return Ux.future(new JsonArray());
        } else {
            return this.activityStub.fetchActivities(identifier, key);
        }
    }

    @Address(Addr.History.HISTORY_ITEMS)
    public Future<JsonArray> fetchChanges(final String activityId) {
        return this.activityStub.fetchChanges(activityId);
    }
}
