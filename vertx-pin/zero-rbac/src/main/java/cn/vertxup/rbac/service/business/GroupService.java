package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.RGroupRoleDao;
import cn.vertxup.rbac.domain.tables.daos.SGroupDao;
import cn.vertxup.rbac.domain.tables.pojos.RGroupRole;
import cn.vertxup.rbac.domain.tables.pojos.SGroup;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.mod.rbac.cv.AuthKey;
import io.vertx.mod.rbac.cv.AuthMsg;
import io.vertx.up.atom.typed.UArray;
import io.vertx.up.eon.KName;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;

import static io.vertx.mod.rbac.refine.Sc.LOG;

public class GroupService implements GroupStub {

    private static final Annal LOGGER = Annal.get(GroupService.class);

    @Override
    public Future<JsonArray> fetchRoleIdsAsync(final String groupKey) {
        LOG.Auth.info(LOGGER, AuthMsg.RELATION_GROUP_ROLE, groupKey, "Async");
        return Ke.umALink(AuthKey.F_GROUP_ID, groupKey, RGroupRoleDao.class);
    }

    @Override
    public JsonArray fetchRoleIds(final String groupKey) {
        LOG.Auth.info(LOGGER, AuthMsg.RELATION_GROUP_ROLE, groupKey, "Sync");
        final List<RGroupRole> relations = Ux.Jooq.on(RGroupRoleDao.class)
            .fetch(AuthKey.F_GROUP_ID, groupKey);
        return UArray.create(Ux.toJson(relations))
            .remove(AuthKey.F_GROUP_ID).to();
    }

    @Override
    public SGroup fetchParent(final String groupKey) {
        final UxJooq dao = Ux.Jooq.on(SGroupDao.class);
        if (null == dao) {
            return null;
        }
        final SGroup current = dao.fetchById(groupKey);
        return null == current ? null :
            dao.fetchById(current.getParentId());
    }

    @Override
    public List<SGroup> fetchChildren(final String groupKey) {
        final UxJooq dao = Ux.Jooq.on(SGroupDao.class);
        if (null == dao) {
            return new ArrayList<>();
        }
        return dao.fetch(AuthKey.F_PARENT_ID, groupKey);
    }

    @Override
    public Future<JsonArray> fetchGroups(final String sigma) {
        return Ux.Jooq.on(SGroupDao.class)
            /* Fetch by sigma */
            .<SGroup>fetchAsync(KName.SIGMA, sigma)
            /* Get Result */
            .compose(Ux::futureA);
    }
}
