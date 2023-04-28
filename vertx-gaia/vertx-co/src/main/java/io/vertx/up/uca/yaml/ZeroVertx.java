package io.vertx.up.uca.yaml;

import io.horizon.eon.VString;
import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.KPlugin;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Set;

/**
 * @author lang
 */
public class ZeroVertx implements Node<JsonObject> {

    @Override
    public JsonObject read() {
        // Not null because execNil
        final JsonObject config = ZeroTool.read(null, true);
        // Injection Lime
        final JsonObject zero = Fn.failOr(new JsonObject(),
            () -> config.getJsonObject(KName.Internal.ZERO), config);
        if (null != zero && zero.containsKey(KName.Internal.LIME)) {
            this.processLime(zero);
        }
        // Return to zero configuration part
        return zero;
    }

    private void processLime(final JsonObject data) {
        Fn.runAt(() -> {
            final String limeStr = data.getString(KName.Internal.LIME);
            final Set<String> sets = Ut.toSet(limeStr, VString.COMMA);
            /*
             * server, inject, error, resolver
             * RxJava2
             */
            Observable.fromArray(KPlugin.FILES)
                .map(item -> item)
                .subscribe(sets::add).dispose();
            data.put(KName.Internal.LIME, Ut.fromJoin(sets));
        }, data);
    }
}
