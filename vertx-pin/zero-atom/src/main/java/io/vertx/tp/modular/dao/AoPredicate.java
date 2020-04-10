package io.vertx.tp.modular.dao;

import io.vertx.core.Future;
import io.vertx.up.atom.query.Criteria;

interface AoPredicate {

    Future<Boolean> existingAsync(Criteria criteria);

    Boolean existing(Criteria criteria);

    Future<Boolean> missingAsync(Criteria criteria);

    Boolean missing(Criteria criteria);
}
