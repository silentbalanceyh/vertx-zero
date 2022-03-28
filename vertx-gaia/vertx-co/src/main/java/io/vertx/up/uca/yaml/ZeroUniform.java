package io.vertx.up.uca.yaml;

import io.reactivex.Observable;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.Plugins;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

public class ZeroUniform implements Node<JsonObject> {

    private static final Node<ConcurrentMap<String, String>> node
        = Ut.singleton(ZeroLime.class);

    @Override
    public JsonObject read() {
        final JsonObject data = new JsonObject();
        final ConcurrentMap<String, String> keys = node.read();
        final Set<String> skipped = Arrays
            .stream(Plugins.DATA).collect(Collectors.toSet());
        // RxJava2 version
        Observable.fromIterable(keys.keySet())
            .filter(item -> !skipped.contains(item))
            .map(key -> Fn.pool(Storage.CONFIG, keys.get(key),
                () -> Fn.getJvm(new JsonObject(), () -> Ut.ioYaml(keys.get(key)),
                    keys.get(key))))
            .subscribe(item -> data.mergeIn(item, true))
            .dispose();
        return data;
    }
}
