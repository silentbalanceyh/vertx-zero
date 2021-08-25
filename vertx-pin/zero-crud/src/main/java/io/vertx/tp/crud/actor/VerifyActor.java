package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.atom.Rule;
import io.vertx.up.exception.WebException;
import io.vertx.up.uca.rs.announce.Rigor;

import java.util.List;
import java.util.concurrent.ConcurrentMap;

/*
 * Validation for body
 */
class VerifyActor extends AbstractActor {

    @Override
    public JsonObject proc(final JsonObject data, final KModule config) {
        /* 1.method, uri */
        final String key = null;

        final ConcurrentMap<String, List<Rule>> rules = IxPin.getRules(key);
        if (!rules.isEmpty()) {
            /*
             * 2. Validate JsonObject
             */
            final Rigor rigor = Rigor.get(JsonObject.class);
            final WebException error = rigor.verify(rules, data);
            if (null != error) {
                throw error;
            }
        }
        return data;
    }
}
