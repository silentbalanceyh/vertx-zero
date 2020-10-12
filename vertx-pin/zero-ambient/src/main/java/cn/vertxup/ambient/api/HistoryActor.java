package cn.vertxup.ambient.api;

import cn.vertxup.ambient.domain.tables.daos.XActivityDao;
import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.service.ActivityStub;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.Addr;
import io.vertx.tp.ke.cv.KeField;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.up.annotations.Address;
import io.vertx.up.annotations.Queue;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import javax.inject.Inject;
import java.util.Objects;

@Queue
public class HistoryActor {
    @Inject
    private transient ActivityStub activityStub;

    @Address(Addr.History.HISTORIES)
    public Future<JsonArray> fetchHistory(final String identifier, final String key) {
        if (Ut.isNilOr(identifier, key)) {
            return Ux.futureA();
        } else {
            return this.activityStub.fetchActivities(identifier, key);
        }
    }

    @Address(Addr.History.HISTORY_ITEMS)
    public Future<JsonArray> fetchChanges(final String activityId) {
        return this.activityStub.fetchChanges(activityId);
    }

    @Address(Addr.History.HISTORY_BY_FIELDS)
    public Future<JsonArray> fetchChangeBy(final String modelId, final String modelKey,
                                           final String modelField) {
        if (Ut.isNilOr(modelId, modelKey, modelField)) {
            return Ux.futureA();
        } else {
            return this.activityStub.fetchActivities(modelId, modelKey, modelField);
        }
    }

    @Address(Addr.History.ACTIVITY_SEARCH)
    public Future<JsonObject> searchActivities(final JsonObject body) {
        return Ux.Jooq.on(XActivityDao.class).searchAsync(body);
    }

    @Address(Addr.History.ACTIVITY_GET)
    public Future<JsonObject> fetchActivity(final String key) {
        return Ux.Jooq.on(XActivityDao.class).<XActivity>fetchByIdAsync(key).compose(activity -> {
            if (Objects.isNull(activity)) {
                return Ux.futureJ();
            } else {
                return this.activityStub.fetchChanges(activity.getKey()).compose(changes -> {
                    final JsonObject data = Ux.toJson(activity);
                    /*
                     * recordOld -> recordNew
                     * Data that should be deserialized to Json Object
                     */
                    Ke.mount(data, KeField.RECORD_NEW);
                    Ke.mount(data, KeField.RECORD_OLD);
                    data.put("changes", changes);
                    return Ux.future(data);
                });
            }
        });
    }
}
