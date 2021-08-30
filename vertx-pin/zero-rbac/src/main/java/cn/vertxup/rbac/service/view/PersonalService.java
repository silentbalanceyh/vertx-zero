package cn.vertxup.rbac.service.view;

import cn.vertxup.rbac.domain.tables.daos.SViewDao;
import cn.vertxup.rbac.domain.tables.pojos.SView;
import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.cv.em.OwnerType;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class PersonalService implements PersonalStub {
    @Override
    public Future<List<SView>> byUser(final String resourceId, final String ownerId) {
        final JsonObject criteria = Ux.whereAnd();
        criteria.put("ownerType", OwnerType.USER.name());
        criteria.put("owner", ownerId);
        criteria.put(KName.RESOURCE_ID, resourceId);
        return Ux.Jooq.on(SViewDao.class).fetchAsync(criteria);
    }

    @Override
    public Future<SView> create(final JsonObject data) {
        Ut.ifString(data, Qr.KEY_CRITERIA, Qr.KEY_PROJECTION, "rows");
        final SView view = Ut.deserialize(data, SView.class);
        if (data.containsKey(KName.USER)) {
            view.setCreatedBy(data.getString(KName.USER));
            view.setUpdatedBy(data.getString(KName.USER));
        }
        view.setCreatedAt(LocalDateTime.now());
        view.setUpdatedAt(LocalDateTime.now());
        view.setKey(UUID.randomUUID().toString());
        view.setActive(Boolean.TRUE);
        return Ux.Jooq.on(SViewDao.class).insertAsync(view);
    }

    @Override
    public Future<Boolean> delete(final Set<String> keys) {
        if (1 == keys.size()) {
            final String key = keys.iterator().next();
            return Ux.Jooq.on(SViewDao.class).deleteByIdAsync(key);
        } else {
            final JsonObject criteria = Ux.whereKeys(keys);
            return Ux.Jooq.on(SViewDao.class).deleteByAsync(criteria);
        }
    }

    @Override
    public Future<SView> byId(final String key) {
        return Ux.Jooq.on(SViewDao.class).fetchByIdAsync(key);
    }

    @Override
    public Future<SView> update(final String key, final JsonObject data) {
        return this.byId(key).compose(view -> {
            if (Objects.isNull(view)) {
                return Ux.future();
            } else {
                final JsonObject serialized = Ut.serializeJson(view);
                Ut.ifString(data, Qr.KEY_CRITERIA, Qr.KEY_PROJECTION, "rows");
                if (data.containsKey(KName.USER)) {
                    view.setUpdatedBy(data.getString(KName.USER));
                    view.setUpdatedAt(LocalDateTime.now());
                }
                serialized.mergeIn(data);
                return Ux.Jooq.on(SViewDao.class).updateAsync(view);
            }
        });
    }
}
