package io.vertx.tp.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.daos.RUserGroupDao;
import cn.vertxup.rbac.domain.tables.daos.SGroupDao;
import cn.vertxup.rbac.domain.tables.pojos.RUserGroup;
import cn.vertxup.rbac.domain.tables.pojos.SGroup;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.eon.KName;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class BinderGroup extends AbstractBind<SGroup> {

    BinderGroup(final String sigma) {
        super(sigma);
    }

    @Override
    public Future<JsonArray> bindAsync(final List<SUser> users, final JsonArray inputData) {
        if (users.isEmpty()) {
            return Ux.futureA();
        }
        return this.purgeAsync(users, RUserGroupDao.class, AuthKey.F_USER_ID)
            .compose(nil -> this.mapAsync(inputData, SGroupDao.class, KName.ROLES))
            .compose(roleMap -> {
                /*
                 * Build for each user
                 */
                final List<RUserGroup> relationList = new ArrayList<>();
                users.forEach(user -> {
                    final List<SGroup> groups = roleMap.getOrDefault(user.getUsername(), new ArrayList<>());
                    Ut.itList(groups, (group, index) -> {
                        final RUserGroup relation = new RUserGroup();
                        relation.setGroupId(group.getKey());
                        relation.setUserId(user.getKey());
                        relation.setPriority(index);
                        relationList.add(relation);
                    });
                    /*
                     * Building relation ship
                     */
                    Sc.infoWeb(this.getClass(), "Will build username = {1}, group size = {0}",
                        String.valueOf(groups.size()), user.getUsername());
                });
                return Ux.future(relationList);
            })
            .compose(Ux.Jooq.on(RUserGroupDao.class)::insertAsync)
            .compose(nil -> Ux.futureA(users));
    }

    @Override
    protected Function<SGroup, String> keyFn() {
        return SGroup::getKey;
    }

    @Override
    protected Function<SGroup, String> valueFn() {
        return SGroup::getName;
    }

}
