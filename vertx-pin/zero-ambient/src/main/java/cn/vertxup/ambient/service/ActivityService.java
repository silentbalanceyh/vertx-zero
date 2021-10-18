package cn.vertxup.ambient.service;

import cn.vertxup.ambient.domain.tables.daos.XActivityChangeDao;
import cn.vertxup.ambient.domain.tables.daos.XActivityDao;
import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.domain.tables.pojos.XActivityChange;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ambient.cv.em.ActivityStatus;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ActivityService implements ActivityStub {
    /*
     * Fetch activities from system here
     * 1. modelId = identifier
     * 2. modelKey = key
     * The condition should be uniform here.
     */
    @Override
    public Future<JsonArray> fetchActivities(final String identifier, final String key) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.MODEL_ID, identifier);
        filters.put(KName.MODEL_KEY, key);
        filters.put(KName.ACTIVE, Boolean.TRUE);  // 只搜索合法的
        return Ux.Jooq.on(XActivityDao.class)
            .fetchAndAsync(filters)
            .compose(Ux::futureA)
            .compose(Ut.ifJArray(KName.RECORD_NEW, KName.RECORD_OLD));
            /*  OLD
                 response -> {
                        Ut.itJArray(response).forEach(each -> {
                            Ke.mount(each, KName.RECORD_NEW);
                            Ke.mount(each, KName.RECORD_OLD);
                        });
                return Ux.future(response);
            }
           */
    }

    @Override
    public Future<JsonArray> fetchActivities(final String identifier, final String key,
                                             final String field) {
        final JsonObject filters = new JsonObject();
        filters.put(KName.MODEL_ID, identifier);
        filters.put(KName.MODEL_KEY, key);
        filters.put(KName.ACTIVE, Boolean.TRUE);  // 只搜索合法的
        /*
         * Created By should be synced from system
         * Read all valid activities
         */
        return Ux.Jooq.on(XActivityDao.class).<XActivity>fetchAndAsync(filters).compose(activities -> {
            /*
             * Continue for finding of all fields
             */
            final Set<String> activityIds = activities.stream().map(XActivity::getKey)
                .collect(Collectors.toSet());
            final JsonObject criteria = new JsonObject();
            criteria.put("activityId,i", Ut.toJArray(activityIds));
            criteria.put("fieldName", field);
            return Ux.Jooq.on(XActivityChangeDao.class).fetchAndAsync(criteria).compose(Ux::futureA);
        });
    }

    @Override
    public Future<JsonArray> fetchChanges(final String activityId) {
        return this.fetchChangeList(activityId)
            .compose(Ux::futureA);
    }

    @Override
    public Future<JsonObject> fetchActivity(final String id) {
        return Ux.Jooq.on(XActivityDao.class)
            .<XActivity>fetchByIdAsync(id)
            .compose(Ut.ifNil(JsonObject::new, (activity) -> this.fetchChanges(activity.getKey())
                .compose(changes -> {
                    final JsonObject activityJson = Ux.toJson(activity);
                    /*
                     * Json Serialization for recordNew / recordOld
                     */
                    Ut.ifJObject(activityJson,
                        KName.RECORD_NEW,
                        KName.RECORD_OLD
                    );
                    activityJson.put(KName.CHANGES, changes);
                    return Ux.future(activityJson);
                })));
    }

    private Future<List<XActivityChange>> fetchChangeList(final String activityId) {
        return Ux.Jooq.on(XActivityChangeDao.class).<XActivityChange>fetchAsync(ACTIVITY_ID, activityId);
    }

    @Override
    public Future<JsonArray> saveChanges(final String id, final ActivityStatus status) {
        return this.fetchChangeList(id)
            .compose(changes -> {
                /*
                 * Iterate `ActivityChanges`
                 */
                final List<XActivityChange> original = new ArrayList<>(changes);
                Ut.itList(original, (change, index) -> {
                    final String oldStatus = change.getStatus();
                    final XActivityChange itemRef = changes.get(index);
                    if (Ut.isNil(oldStatus)) {
                        itemRef.setStatus(status.name());
                    } else {
                        if (ActivityStatus.CONFIRMED == status) {
                            /*
                             * -> CONFIRMED
                             * Only `PENDING` allowed, system keeped
                             */
                            final ActivityStatus old =
                                Ut.toEnum(change::getStatus, ActivityStatus.class, ActivityStatus.SYSTEM);
                            if (ActivityStatus.PENDING == old) {
                                itemRef.setStatus(ActivityStatus.CONFIRMED.name());
                            }
                        } else {
                            /*
                             * -> PENDING or SYSTEM
                             * It's allowed directly
                             */
                            itemRef.setStatus(status.name());
                        }

                    }
                });
                return Ux.Jooq.on(XActivityChangeDao.class)
                    .updateAsync(changes)
                    .compose(Ux::futureA);
            });
    }
}
