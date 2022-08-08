package io.vertx.up.uca.micro.matcher;

import io.vertx.ext.web.RoutingContext;
import io.vertx.servicediscovery.Record;

import java.util.List;

/**
 * Look up matched remote record
 */
public interface Arithmetic {

    Record search(final List<Record> records,
                  final RoutingContext context);
}
