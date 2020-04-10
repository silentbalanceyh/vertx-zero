package io.vertx.up.uca.yaml;

import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Plugins;
import io.vertx.up.eon.Strings;
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
        final JsonObject zero = Fn.getJvm(new JsonObject(),
                () -> config.getJsonObject(Key.ZERO), config);
        if (null != zero && zero.containsKey(Key.LIME)) {
            this.prodcessLime(zero);
        }
        // Return to zero configuration part
        return zero;
    }

    private JsonObject process(final JsonObject data) {
        return Fn.getNull(() -> {
            // 1. Append lime
            if (data.containsKey(Key.LIME)) {
                this.prodcessLime(data);
            }
            return data;
        }, data);
    }

    private void prodcessLime(final JsonObject data) {
        Fn.safeNull(() -> {
            final String limeStr = data.getString(Key.LIME);
            final Set<String> sets = Ut.toSet(limeStr, Strings.COMMA);
            /*
             * server, inject, error, resolver
             * RxJava2
             */
            Observable.fromArray(Plugins.DATA)
                    .map(item -> item)
                    .subscribe(sets::add).dispose();
            data.put(Key.LIME, Ut.fromJoin(sets));
        }, data);
    }
}
