package io.vertx.mod.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.daos.OUserDao;
import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.horizon.atom.common.Refer;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.mod.rbac.refine.Sc;
import io.vertx.up.commune.record.Apt;
import io.vertx.up.eon.KName;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static io.vertx.mod.rbac.refine.Sc.LOG;

class IdcService extends AbstractIdc {

    IdcService(final String sigma) {
        super(sigma);
    }

    @Override
    public Future<JsonArray> saveAsync(final JsonArray user, final String by) {
        /*
         * 绑定一个用户输入，输入中可能包含
         * - roles      = R1, R2, R3
         * - groups     = G1, G2, G3
         */
        final Refer inputArray = new Refer();
        return this.runPre(user)
            // 按 username 压缩
            .compose(this::compress)
            // 压缩后和引用同步
            .compose(inputArray::future)
            // 读取原来的数据
            .compose(this::capture)
            .compose(original -> {
                /*
                 * Unique `username` ensure in database
                 */
                final Apt apt = Apt.create(original, inputArray.get());
                final Apt created = Ke.compmared(apt, KName.USERNAME, by);
                /*
                 * Split doing
                 */
                return Ke.atomyFn(this.getClass(), created).apply(
                    // Insert
                    inserted -> this.createAsync(inserted).compose(users -> this.connect(users, inputArray.get())),
                    // Update
                    updated -> this.updateAsync(updated).compose(users -> this.connect(users, inputArray.get()))
                );
            });
    }

    @SuppressWarnings("all")
    private Future<JsonArray> connect(final List<SUser> users, final JsonArray inputData) {
        return Fn.passion(inputData,
            // roles = R1, R2, R3
            inputA -> IdcBinder.role(this.sigma).bindAsync(users, inputA),
            // groups = G1, G2, G3
            inputA -> IdcBinder.group(this.sigma).bindAsync(users, inputA)
        );
    }

    private Future<JsonArray> capture(final JsonArray compress) {
        final JsonObject condition = Ux.whereAnd();
        condition.put(KName.USERNAME + ",i", Ut.toJArray(Ut.valueSetString(compress, KName.USERNAME)));
        condition.put(KName.SIGMA, this.sigma);
        LOG.Web.info(this.getClass(), "Unique filters: {0}", condition.encode());
        return Ux.Jooq.on(SUserDao.class).fetchJAsync(condition);
    }

    private Future<JsonArray> compress(final JsonArray inputData) {
        final JsonArray compressed = new JsonArray();
        final Set<String> nameSet = new HashSet<>();
        Ut.itJArray(inputData).forEach(each -> {
            if (!nameSet.contains(each.getString(KName.USERNAME))) {
                compressed.add(each);
                nameSet.add(KName.USERNAME);
            } else {
                LOG.Web.info(this.getClass(), "User ( username = {0} ) duplicated and will be ignored: {1}",
                    each.getString(KName.USERNAME), each.encode());
            }
        });
        return Ux.future(compressed);
    }

    private Future<List<SUser>> createAsync(final JsonArray userJson) {
        final Refer refer = new Refer();
        return this.model(userJson)
            .compose(processed -> Sc.valueAuth(processed, this.sigma))
            .compose(Ux.Jooq.on(SUserDao.class)::insertAsync)
            .compose(refer::future)
            .compose(Sc::valueAuth)
            .compose(Ux.Jooq.on(OUserDao.class)::insertAsync)
            .compose(ou -> Ux.future(refer.get()));
    }

    private Future<List<SUser>> updateAsync(final JsonArray userJson) {
        final List<SUser> users = Ux.fromJson(userJson, SUser.class);
        users.forEach(user -> user.setActive(Boolean.TRUE));
        return Ux.Jooq.on(SUserDao.class).updateAsync(users);
    }
}
