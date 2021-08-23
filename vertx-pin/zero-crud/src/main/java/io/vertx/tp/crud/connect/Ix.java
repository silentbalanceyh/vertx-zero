package io.vertx.tp.crud.connect;

import io.vertx.core.Future;
import io.vertx.core.MultiMap;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.tp.ke.atom.connect.KJoin;
import io.vertx.tp.ke.atom.connect.KPoint;
import io.vertx.tp.ke.cv.em.JoinMode;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KName;
import io.vertx.up.log.Annal;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;

interface Pool {
    ConcurrentMap<String, IxLinker> LINKER_MAP =
            new ConcurrentHashMap<>();
}

interface IxSwitcher {

    static JsonObject getData(final JsonObject original, final KModule module) {
        /*
         * Safe call because of MoveOn
         */
        final KJoin connect = module.getConnect();
        /*
         * Remove primary key, it will generate new.
         */
        final JsonObject inputData = original.copy();
        final KPoint target = connect.procTarget(original);
        connect.procFilters(original, target, inputData);
        return inputData;
    }

    static JsonObject getCondition(final JsonObject original, final KModule module) {
        /*
         * Safe call because of MoveOn
         */
        final JsonObject filters = new JsonObject();
        final KJoin connect = module.getConnect();
        final KPoint target = connect.procTarget(original);
        connect.procFilters(original, target, filters);
        /*
         * Append `Sigma` Here
         */
        if (original.containsKey(KName.SIGMA)) {
            filters.put("", Boolean.TRUE);
            filters.put(KName.SIGMA, original.getString(KName.SIGMA));
        }
        return filters;
    }

    static Future<Envelop> moveOn(final JsonObject data,
                                  final MultiMap headers,
                                  final KModule module,
                                  final BiFunction<UxJooq, KModule, Future<Envelop>> function) {
        /*
         * Linker data preparing
         */
        final KJoin connect = module.getConnect();
        final Annal LOGGER = Annal.get(IxSwitcher.class);
        if (Objects.isNull(connect)) {
            /*
             * KJoin null, could not identify connect
             */
            Ix.infoDao(LOGGER, "KJoin is null");
            return Ux.future(Envelop.success(data));
        } else {
            final KPoint target = connect.procTarget(data);
            if (Objects.isNull(target) || JoinMode.CRUD != target.modeTarget()) {
                return Ux.future(Envelop.success(data));
            } else {
                assert Objects.nonNull(target.getCrud()) : "Here the 'crud' field could not be null, because of passed 'modeTarget' checking.";
                final KModule joinedModule = IxPin.getActor(target.getCrud());
                final UxJooq dao = IxPin.getDao(joinedModule, headers);
                return function.apply(dao, joinedModule);
            }
        }
    }

    static Future<Envelop> moveEnd(final JsonObject original, final Envelop response,
                                   final KModule config) {
        JsonObject createdJoined = response.data();
        /*
         * Merged two data here,
         * Be careful is that we must overwrite createdJoined
         * instead of data because the original data must be keep
         * Here are some modification of `key` here.
         * Here provide `joinedKey` field for target object.
         */
        if (Objects.isNull(createdJoined)) {
            createdJoined = new JsonObject();
        } else {
            final String joinedField = config.getField().getKey();
            createdJoined.put(KName.JOINED_KEY, createdJoined.getString(joinedField));
        }
        createdJoined.mergeIn(original, true);
        return Ux.future(Envelop.success(createdJoined));
    }
}
