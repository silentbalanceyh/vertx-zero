package io.vertx.quiz.nova;

import cn.vertxup.crud.api.*;
import io.vertx.core.Future;
import io.vertx.up.commune.Envelop;
import io.vertx.up.util.Ut;
import io.vertx.quiz.cv.QzApi;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

interface Actors {
    PostActor POST = Ut.singleton(PostActor.class);
    GetActor GET = Ut.singleton(GetActor.class);
    PutActor PUT = Ut.singleton(PutActor.class);
    DeleteActor DELETE = Ut.singleton(DeleteActor.class);
    QueryActor QUERY = Ut.singleton(QueryActor.class);
}

interface Pool {

    ConcurrentMap<Class<?>, Qz> QZ_POOL = new ConcurrentHashMap<>();

    ConcurrentMap<QzApi, Function<Envelop, Future<Envelop>>> METHOD_POOL =
            new ConcurrentHashMap<QzApi, Function<Envelop, Future<Envelop>>>() {
                {
                    put(QzApi.POST_ACTOR, Actors.POST::create);
                    put(QzApi.GET_ACTOR_KEY, Actors.GET::getById);
                    put(QzApi.PUT_ACTOR_KEY, Actors.PUT::update);
                    put(QzApi.DELETE_ACTOR_KEY, Actors.DELETE::delete);

                    put(QzApi.POST_ACTOR_SEARCH, Actors.QUERY::search);
                    put(QzApi.POST_ACTOR_MISSING, Actors.QUERY::missing);
                    put(QzApi.POST_ACTOR_EXISTING, Actors.QUERY::existing);

                    put(QzApi.POST_BATCH_UPDATE, Actors.PUT::updateBatch);
                    put(QzApi.POST_BATCH_DELETE, Actors.DELETE::deleteBatch);
                }
            };
}
