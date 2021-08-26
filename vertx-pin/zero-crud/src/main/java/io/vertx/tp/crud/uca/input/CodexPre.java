package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.atom.Rule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.WebException;
import io.vertx.up.uca.rs.announce.Rigor;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class CodexPre implements Pre {
    @Override
    public Future<JsonObject> inJAsync(final JsonObject data, final IxIn in) {
        /* 1.method, uri */
        final String key = this.getKey(data, in);

        Ix.Log.verify(this.getClass(), "---> Rule: {0}", key);

        final ConcurrentMap<String, List<Rule>> rules = IxPin.getRules(key);
        if (!rules.isEmpty()) {
            /*
             * 2. Validate JsonObject
             */
            final Rigor rigor = Rigor.get(JsonObject.class);
            final WebException error = rigor.verify(rules, data);
            if (null != error) {
                Ix.Log.verify(this.getClass(), "---> Error Code: {0}", String.valueOf(error.getCode()));
                throw error;
            }
        }
        return Ux.future(data);
    }

    private String getKey(final JsonObject data, final IxIn in) {
        final Envelop envelop = in.envelop();
        final KModule module = in.module();
        /* 1.method, uri */
        String uri = envelop.uri();
        if (uri.contains("?")) {
            uri = uri.split("\\?")[0];
        }
        final String method = envelop.method().name();
        /* 2.uri 中处理 key 相关的情况 */
        final String keyField = module.getField().getKey();
        final String keyValue = data.getString(keyField);
        if (Ut.notNil(keyValue)) {
            uri = uri.replace(keyValue, "$" + keyField);
        }
        /* 3.Final Rule */
        return uri.toLowerCase(Locale.getDefault()).replace('/', '.' )
            .substring(1) + Strings.DOT
            + method.toLowerCase(Locale.getDefault());
    }
}
