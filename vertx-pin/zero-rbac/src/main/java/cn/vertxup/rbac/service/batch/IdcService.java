package cn.vertxup.rbac.service.batch;

import cn.vertxup.rbac.domain.tables.daos.OUserDao;
import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.OUser;
import cn.vertxup.rbac.domain.tables.pojos.SRole;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.error._403TokenGenerationException;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.Refer;
import io.vertx.up.atom.record.Apt;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

class IdcService extends AbstractIdc {

    IdcService(final String sigma) {
        super(sigma);
    }

    @Override
    public Future<JsonArray> saveAsync(final JsonArray user, final String by) {

        final Refer dataArray = new Refer();
        final Refer roleRef = new Refer();
        return this.runPre(user)
            .compose(dataArray::future)
            .compose(nil -> IdcRole.create(this.sigma).fetchAsync())
            .compose(roleRef::future)
            .compose(verified -> {
                final JsonArray filtered = new JsonArray();
                final Set<String> nameSet = new HashSet<>();
                Ut.itJArray(user).forEach(each -> {
                    if (!nameSet.contains(each.getString(KName.USERNAME))) {
                        filtered.add(each);
                        nameSet.add(KName.USERNAME);
                    } else {
                        Sc.infoWeb(this.getClass(), "User ( username = {0} ) duplicated and will be ignored: {1}",
                            each.getString(KName.USERNAME), each.encode());
                    }
                });
                /*
                 * Batch user fetching
                 */
                final JsonObject condition = new JsonObject();
                condition.put(KName.USERNAME + ",i", Ut.toJArray(Ut.mapString(filtered, KName.USERNAME)));
                condition.put(KName.SIGMA, this.sigma);
                condition.put(Strings.EMPTY, Boolean.TRUE);
                Sc.infoWeb(this.getClass(), "Unique filters: {0}", condition.encode());
                return Ux.Jooq.on(SUserDao.class).fetchAsync(condition)
                    .compose(Ux::futureA)
                    .compose(original -> {
                        /*
                         * Unique `username` ensure in database
                         */
                        final Apt apt = Apt.create(original, filtered);
                        final Apt created = Ke.compmared(apt, KName.USERNAME, by);
                        /*
                         * ConcurrentMap<String, Role> roles here
                         */
                        final ConcurrentMap<String, List<SRole>> roleMap = IdcRole.create(this.sigma)
                            .toMap(dataArray.get(), roleRef.get());
                        /*
                         * Split doing
                         */
                        return Ke.atomyFn(this.getClass(), created).apply(
                            /*
                             * Insert
                             */
                            inserted -> this.createAsync(inserted, roleMap),
                            /*
                             * Update
                             */
                            updated -> this.updateAsync(updated, roleMap)
                        );
                    });
            });
    }

    private Future<JsonArray> createAsync(final JsonArray userJson,
                                          final ConcurrentMap<String, List<SRole>> roleMap) {
        return this.model(userJson).compose(processed -> {
            final List<SUser> users = Ux.fromJson(processed, SUser.class);
            users.forEach(user -> {
                user.setKey(UUID.randomUUID().toString());
                user.setActive(Boolean.TRUE);
                /* 12345678 */
                final String initPwd = Sc.valuePassword();
                user.setPassword(initPwd);
                user.setSigma(this.sigma);
                user.setLanguage("cn");
            });
            return Ux.Jooq.on(SUserDao.class).insertAsync(users)
                .compose(this::createToken)
                .compose(updated -> IdcRole.create(this.sigma).saveRel(updated, roleMap));
        });
    }

    private Future<List<SUser>> createToken(final List<SUser> users) {
        if (users.isEmpty()) {
            /*
             * Now inserted.
             */
            return Ux.future(new ArrayList<>());
        } else {
            final Set<String> sigmaSet = users.stream().map(SUser::getSigma)
                .collect(Collectors.toSet());
            if (sigmaSet.size() == 1) {
                return this.credential(() -> Ux.future(users), credential -> {
                    /*
                     * OUser processing ( Batch Mode )
                     */
                    final List<OUser> ousers = new ArrayList<>();
                    users.stream().map(user -> new OUser()
                            .setActive(Boolean.TRUE)
                            .setKey(UUID.randomUUID().toString())
                            .setClientId(user.getKey())
                            .setClientSecret(Ut.randomString(64))
                            .setScope(credential.getRealm())
                            .setLanguage(credential.getLanguage())
                            .setGrantType(credential.getGrantType()))
                        .forEach(ousers::add);
                    return Ux.Jooq.on(OUserDao.class).insertAsync(ousers)
                        .compose(created -> Ux.future(users));
                });
            } else {
                return Future.failedFuture(new _403TokenGenerationException(this.getClass(), sigmaSet.size()));
            }
        }
    }

    private Future<JsonArray> updateAsync(final JsonArray userJson,
                                          final ConcurrentMap<String, List<SRole>> roleMap) {
        final List<SUser> users = Ux.fromJson(userJson, SUser.class);
        users.forEach(user -> user.setActive(Boolean.TRUE));
        return Ux.Jooq.on(SUserDao.class).updateAsync(users)
            .compose(updated -> IdcRole.create(this.sigma).saveRel(updated, roleMap));
    }
}
