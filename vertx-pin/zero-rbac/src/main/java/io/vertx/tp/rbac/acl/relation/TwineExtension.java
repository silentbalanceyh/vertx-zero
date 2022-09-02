package io.vertx.tp.rbac.acl.relation;

import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.secure.Twine;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.cv.AuthKey;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.unity.UObject;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.specification.KQr;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJoin;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
 * 关联关系：用户扩展组件
 * 1）当用户和额外的表执行链接时，会启用用户扩展组件，根据类型执行相连
 * 2）目前的分组和规划
 *    用户 + 员工 = 员工账号
 *    用户 + 客户 = 三方账号
 *    用户 + 会员 = 会员账号
 * 3）其配置扩展区间的核心配置如下（以员工为例）
 * src/plugin/rbac/configuration.json 文件
 * {
 *     "....",
 *     "category": {
 *          "employee": {
 *               "classDao": "cn.vertxup.erp.domain.tables.daos.EEmployeeDao",
 *               "condition": {
 *                   "workNumber,!n": ""
 *               },
 *               "mapping": {
 *                   "modelKey": "employeeId"
 *               }
 *          }
 *     },
 *     "initializePassword": "xxxx",
 *     "initialize": {
 *         "scope": "vie.app.xx",
 *         "grantType": "authorization_code"
 *     }
 * }
 *   3.1）此处 employee 代表类型，即 S_USER 中 MODEL_ID 存储的值
 *   3.2）modelKey -> employeeId 为前端提供语义级消费
 *   3.3）initialize 为导入时的模板数据
 */
class TwineExtension implements Twine<SUser> {

    private static final ScConfig CONFIG = ScPin.getConfig();

    /*
     * Fix Issue: https://github.com/silentbalanceyh/ox-engine/issues/1207
     */
    @Override
    public Future<JsonObject> searchAsync(final String identifier, final JsonObject criteria) {
        // KQr 为空，不执行关联查询
        final KQr qr = CONFIG.category(identifier);
        if (Objects.isNull(qr) || !qr.valid()) {
            return Ux.Jooq.on(SUserDao.class).fetchJOneAsync(criteria);
        }
        return TwineQr.normalize(qr, criteria).compose(queryJ -> {
            final UxJoin searcher = Ux.Join.on();
            /*
             * S_USER ( modelKey )
             *    JOIN
             * XXX ( key )
             * 额外步骤
             * */
            searcher.add(SUserDao.class, KName.MODEL_KEY);
            final Class<?> clazz = qr.getClassDao();
            searcher.join(clazz);
            return searcher.searchAsync(queryJ)
                // Connect to `groups`
                .compose(this::connect);
        });
    }

    @Override
    public Future<JsonObject> identAsync(final SUser key) {
        return this.runSingle(key, qr -> {
            final UxJooq jq = Ux.Jooq.on(qr.getClassDao());
            Objects.requireNonNull(jq);
            return jq.fetchJByIdAsync(key.getModelKey());
        });
    }

    @Override
    public Future<JsonObject> identAsync(final SUser key, final JsonObject updatedData) {
        /* User model key */
        return this.runSingle(key, qr -> {
            /* Read Extension information */
            final UxJooq jq = Ux.Jooq.on(qr.getClassDao());
            Objects.requireNonNull(jq);
            return jq.updateJAsync(key.getModelKey(), updatedData);
        });
    }

    @Override
    public Future<JsonArray> identAsync(final Collection<SUser> users) {
        return this.runBatch(this.compress(users)).compose(map -> {
            final JsonArray combineA = new JsonArray();
            final JsonArray extensionA = new JsonArray();
            map.values().forEach(extensionA::addAll);
            final ConcurrentMap<String, JsonObject> mapped = Ut.elementMap(extensionA, KName.KEY);
            // User Iterator / Extension ( key -> JsonObject )
            users.stream().filter(Objects::nonNull).forEach(user -> {
                final JsonObject userJ = Ux.toJson(user);
                final String modelKey = user.getModelKey();
                if (mapped.containsKey(modelKey)) {
                    final JsonObject extensionJ = mapped.getOrDefault(modelKey, new JsonObject());
                    final KQr qr = CONFIG.category(user.getModelId());
                    combineA.add(this.combine(userJ, extensionJ, qr));
                } else {
                    combineA.add(userJ);
                }
            });
            return Ux.future(combineA);
        });
    }

    // ------------------ Private Method Processing --------------------
    private Future<ConcurrentMap<String, JsonArray>> runBatch(final List<SUser> users) {
        final ConcurrentMap<String, List<SUser>> grouped = Ut.elementGroup(users, SUser::getModelId, item -> item);
        final ConcurrentMap<String, Future<JsonArray>> futureMap = new ConcurrentHashMap<>();
        grouped.forEach((modelId, groupList) -> {
            final KQr qr = CONFIG.category(modelId);
            futureMap.put(modelId, this.runBatch(groupList, qr));
        });
        return Fn.combineM(futureMap);
    }

    private Future<JsonArray> runBatch(final List<SUser> users, final KQr qr) {
        final Set<String> keys = users.stream()
            .map(SUser::getModelKey)
            .collect(Collectors.toSet());
        if (keys.isEmpty()) {
            return Ux.futureA();
        } else {
            final UxJooq jq = Ux.Jooq.on(qr.getClassDao());
            Objects.requireNonNull(jq);
            final JsonObject condition = new JsonObject();
            condition.put(KName.KEY + ",i", Ut.toJArray(keys));
            return jq.fetchJAsync(condition);
        }
    }

    /*
     * Fetch Extension Table Record, joined by
     * S_USER
     * - MODEL_ID
     * - MODEL_KEY
     *
     * Here are the specification of extension model that will be combine to S_USER
     * The default usage of `modelId` is as:
     * - res.employee: The account of E_EMPLOYEE of OA system.
     * - ht.member:    The member of the website here for usage in future.
     *
     * The joined part should be came from HAtom in the layer system of EMF framework instead.
     */
    private Future<JsonObject> runSingle(final SUser user, final Function<KQr, Future<JsonObject>> executor) {
        if (Objects.isNull(user)) {
            /* Input SUser object is null, could not find S_USER record in your database */
            Sc.infoWeb(this.getClass(), AuthMsg.EXTENSION_EMPTY + " Null SUser");
            return Ux.futureJ();
        }

        if (Objects.isNull(user.getModelKey()) || Objects.isNull(user.getModelId())) {
            /*
             * There are two fields in S_USER table: MODEL_ID & MODEL_KEY
             * This branch means that MODEL_KEY is null, you could not do any Extension part.
             * Returned SUser json data format only.
             */
            Sc.infoWeb(this.getClass(), AuthMsg.EXTENSION_EMPTY + " Null modelKey");
            return Ux.futureJ(user);
        }

        /*
         * Extract the extension part for default running here.
         * 1) The KQr must exist in ScConfig and you can search the KQr by `modelId`
         * 2) The KQr must be valid:  classDao mustn't be null here
         */
        final KQr qr = CONFIG.category(user.getModelId());
        if (Objects.isNull(qr) || !qr.valid()) {
            Sc.infoWeb(this.getClass(), AuthMsg.EXTENSION_EMPTY + " Extension {0} Null", user.getModelId());
            return Ux.futureJ(user);
        }


        /*
         * Simple situation for extension information processing
         * 1. User Extension `executor` bind to UxJooq running
         * 2. Zero extension provide the configuration part and do executor
         * 3. Returned data format is Json of Extension
         */
        Sc.infoWeb(this.getClass(), AuthMsg.EXTENSION_BY_USER, user.getModelKey());
        return executor.apply(qr).compose(extensionJ -> {
            final JsonObject userJ = Ux.toJson(user);
            if (Ut.isNil(extensionJ)) {
                return Ux.future(userJ);
            } else {
                return Ux.future(this.combine(userJ, extensionJ, qr));
            }
        });
    }

    private Future<JsonObject> connect(final JsonObject pagination) {
        final JsonArray users = Ut.valueJArray(pagination, KName.LIST);
        final Set<String> userKeys = Ut.valueSetString(users, KName.KEY);
        return Junc.refRights().identAsync(userKeys).compose(relations -> {
            // 分组
            final ConcurrentMap<String, JsonArray> grouped =
                Ut.elementGroup(relations, AuthKey.F_USER_ID);
            final JsonArray replaced = Ut.elementZip(users, KName.KEY, KName.GROUPS, grouped, AuthKey.F_GROUP_ID);
            pagination.put(KName.LIST, replaced);
            return Ux.future(pagination);
        });
    }

    private List<SUser> compress(final Collection<SUser> users) {
        /*
         * Filtered:
         * 1. SUser is not null
         * 2. modelId / modelKey is Ok
         * 3. KQr is valid configured in ScConfig
         */
        return users.stream()
            .filter(Objects::nonNull)
            .filter(user -> Objects.nonNull(user.getModelId()))
            .filter(user -> Objects.nonNull(user.getModelKey()))
            .filter(user -> {
                final KQr qr = CONFIG.category(user.getModelId());
                return Objects.nonNull(qr) && qr.valid();
            })
            .toList();
    }

    private JsonObject combine(final JsonObject userJ, final JsonObject extensionJ, final KQr qr) {
        final UObject combine = UObject.create(userJ.copy()).append(extensionJ);
        /*
         * mapping
         * modelKey -> targetField
         */
        final JsonObject mapping = qr.getMapping();
        Ut.<String>itJObject(mapping, (to, from) -> combine.convert(from, to));
        return combine.to();
    }
}
