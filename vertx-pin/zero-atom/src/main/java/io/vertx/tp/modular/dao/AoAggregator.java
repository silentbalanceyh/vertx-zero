package io.vertx.tp.modular.dao;

import io.vertx.core.Future;
import io.vertx.up.atom.query.Criteria;

interface AoAggregator {

    Future<Long> countAsync(Criteria criteria);

    Long count(Criteria criteria);
}
