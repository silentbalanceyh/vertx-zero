package io.vertx.tp.modular.dao;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Criteria;

interface AoAggregator {

    Future<Long> countAsync(Criteria criteria);

    Future<Long> countAsync(JsonObject criteria);

    Long count(Criteria criteria);

    Long count(JsonObject criteria);
}
