package cn.vertxup.rbac.service.business;

import cn.vertxup.rbac.domain.tables.daos.SUserDao;
import cn.vertxup.rbac.domain.tables.pojos.SUser;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.rbac.atom.ScConfig;
import io.vertx.tp.rbac.cv.AuthMsg;
import io.vertx.tp.rbac.init.ScPin;
import io.vertx.tp.rbac.refine.Sc;
import io.vertx.up.atom.query.engine.Qr;
import io.vertx.up.atom.unity.UObject;
import io.vertx.up.eon.KName;
import io.vertx.up.experiment.specification.KQr;
import io.vertx.up.fn.Fn;
import io.vertx.up.uca.jooq.UxJoin;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class UserExtension {

    private static final ScConfig CONFIG = ScPin.getConfig();

    public static Future<JsonObject> updateExtension(final SUser user, final JsonObject params) {
        /* User model key */
        return runExtension(user, qr -> {
            /* Read Extension information */
            final UxJooq jq = Ux.Jooq.on(qr.getClassDao());
            Objects.requireNonNull(jq);
            return jq.updateJAsync(user.getModelKey(), params);
        });
    }

    // ======================= Fetching =======================

    /*
     * Fix Issue: https://github.com/silentbalanceyh/ox-engine/issues/1207
     */
    public static Future<JsonObject> searchAsync(final KQr qr, final JsonObject query, final boolean isQr) {
        // The original `criteria` from query part, fix Null Pointer Issue
        final JsonObject queryJ = query.copy();
        final JsonObject criteria = Ut.valueJObject(queryJ, Qr.KEY_CRITERIA);
        // Qr Json
        final JsonObject condition;
        if (isQr) {
            condition = Ux.whereAnd();
            condition.mergeIn(qr.getCondition());
            if (Ut.notNil(criteria)) {
                // java.lang.IndexOutOfBoundsException: Index 0 out of bounds for length 0
                // Sub Query Tree Must not be EMPTY
                condition.put("$IN$", criteria);
            }
        } else {
            condition = new JsonObject();
            if (Ut.notNil(criteria)) {
                condition.mergeIn(criteria.copy());
            }
        }
        queryJ.put(Qr.KEY_CRITERIA, condition);

        final UxJoin searcher = Ux.Join.on();
        /*
         * S_USER ( modelKey )
         *    JOIN
         * XXX ( key )
         * */
        searcher.add(SUserDao.class, KName.MODEL_KEY);
        final Class<?> clazz = qr.getClassDao();
        searcher.join(clazz);
        return searcher.searchAsync(queryJ);
    }

    public static Future<JsonObject> fetchAsync(final SUser user) {
        /* User model key */
        return runExtension(user, qr -> {
            /* Read Extension information */
            final UxJooq jq = Ux.Jooq.on(qr.getClassDao());
            Objects.requireNonNull(jq);
            return jq.fetchJByIdAsync(user.getModelKey());
        });
    }

    public static Future<JsonArray> fetchAsync(final List<SUser> users) {
        /*
         * Filtered:
         * 1. SUser is not null
         * 2. modelId / modelKey is Ok
         * 3. KQr is valid configured in ScConfig
         */
        final List<SUser> compress = users.stream()
            .filter(Objects::nonNull)
            .filter(user -> Objects.nonNull(user.getModelId()))
            .filter(user -> Objects.nonNull(user.getModelKey()))
            .filter(user -> {
                final KQr qr = CONFIG.category(user.getModelId());
                return Objects.nonNull(qr) && qr.valid();
            })
            .collect(Collectors.toList());

        return runExtension(compress).compose(map -> {
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
                    combineA.add(combine(userJ, extensionJ, qr));
                } else {
                    combineA.add(userJ);
                }
            });
            return Ux.future(combineA);
        });
    }


    // ======================= Private Extension =======================
    private static Future<ConcurrentMap<String, JsonArray>> runExtension(final List<SUser> users) {
        final ConcurrentMap<String, List<SUser>> grouped = Ut.elementGroup(users, SUser::getModelId, item -> item);
        final ConcurrentMap<String, Future<JsonArray>> futureMap = new ConcurrentHashMap<>();
        grouped.forEach((modelId, groupList) -> {
            final KQr qr = CONFIG.category(modelId);
            futureMap.put(modelId, runExtension(groupList, qr));
        });
        return Fn.combineM(futureMap);
    }

    private static Future<JsonArray> runExtension(final List<SUser> users, final KQr qr) {
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
    private static Future<JsonObject> runExtension(final SUser user, final Function<KQr, Future<JsonObject>> executor) {
        if (Objects.isNull(user)) {
            /* Input SUser object is null, could not find S_USER record in your database */
            Sc.infoWeb(UserExtension.class, AuthMsg.EXTENSION_EMPTY + " Null SUser");
            return Ux.futureJ();
        }

        if (Objects.isNull(user.getModelKey()) || Objects.isNull(user.getModelId())) {
            /*
             * There are two fields in S_USER table: MODEL_ID & MODEL_KEY
             * This branch means that MODEL_KEY is null, you could not do any Extension part.
             * Returned SUser json data format only.
             */
            Sc.infoWeb(UserExtension.class, AuthMsg.EXTENSION_EMPTY + " Null modelKey");
            return Ux.futureJ(user);
        }

        /*
         * Extract the extension part for default running here.
         * 1) The KQr must exist in ScConfig and you can search the KQr by `modelId`
         * 2) The KQr must be valid:  classDao mustn't be null here
         */
        final KQr qr = CONFIG.category(user.getModelId());
        if (Objects.isNull(qr) || !qr.valid()) {
            Sc.infoWeb(UserExtension.class, AuthMsg.EXTENSION_EMPTY + " Extension {0} Null", user.getModelId());
            return Ux.futureJ(user);
        }

        /*
         * Simple situation for extension information processing
         * 1. User Extension `executor` bind to UxJooq running
         * 2. Zero extension provide the configuration part and do executor
         * 3. Returned data format is Json of Extension
         */
        Sc.infoWeb(UserExtension.class, AuthMsg.EXTENSION_BY_USER, user.getModelKey());
        return executor.apply(qr).compose(extensionJ -> {
            final JsonObject userJ = Ux.toJson(user);
            if (Ut.isNil(extensionJ)) {
                return Ux.future(userJ);
            } else {
                return Ux.future(combine(userJ, extensionJ, qr));
            }
        });
    }

    private static JsonObject combine(final JsonObject userJ, final JsonObject extensionJ, final KQr qr) {
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
