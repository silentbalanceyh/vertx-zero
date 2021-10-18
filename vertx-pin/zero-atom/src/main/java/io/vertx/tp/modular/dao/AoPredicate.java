package io.vertx.tp.modular.dao;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Criteria;

interface AoPredicate {

    Future<Boolean> existAsync(Criteria criteria);

    Boolean exist(Criteria criteria);

    Future<Boolean> existAsync(JsonObject criteria);

    Boolean exist(JsonObject criteria);

    Future<Boolean> missAsync(Criteria criteria);

    Boolean miss(Criteria criteria);

    Future<Boolean> missAsync(JsonObject criteria);

    Boolean miss(JsonObject criteria);
}
