package io.vertx.tp.crud.actor;

import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.atom.IxModule;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.up.atom.Rule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.WebException;
import io.vertx.up.uca.rs.announce.Rigor;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentMap;

/*
 * Validation for body
 */
class VerifyActor extends AbstractActor {

    @Override
    public JsonObject proc(final JsonObject data, final IxModule config) {
        /* 1.method, uri */
        final String key = this.getKey(data, config);

        Ix.infoVerify(this.getLogger(), "---> Rule: {0}", key);

        final ConcurrentMap<String, List<Rule>> rules = IxPin.getRules(key);
        if (!rules.isEmpty()) {
            /*
             * 2. Validate JsonObject
             */
            final Rigor rigor = Rigor.get(JsonObject.class);
            final WebException error = rigor.verify(rules, data);
            if (null != error) {
                Ix.infoVerify(this.getLogger(), "---> Error Code: {0}", String.valueOf(error.getCode()));
                throw error;
            }
        }
        return data;
    }

    private String getKey(final JsonObject data, final IxModule config) {
        final Envelop request = this.getRequest();
        /* 1.method, uri */
        String uri = request.uri();
        final String method = request.method().name();
        /* 2.uri 中处理 key 相关的情况 */
        final String keyField = config.getField().getKey();
        final String keyValue = data.getString(keyField);
        if (Ut.notNil(keyValue)) {
            uri = uri.replace(keyValue, "$" + keyField);
        }
        /* 3.Final Rule */
        return uri.toLowerCase(Locale.getDefault()).replace('/', '.')
                .substring(1) + Strings.DOT
                + method.toLowerCase(Locale.getDefault());
    }
}
