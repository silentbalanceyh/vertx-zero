package io.vertx.up.uca.yaml;

import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KPlugin;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

/*
 * 「星云」改造点
 * 将原始配置读取分成核心几个部分，检查核心配置执行分流，yaml文件格式如：
 * aeon: 是否启用永世系统（一旦启用则走永世系统流程）
 */
public class ZeroUniform implements Node<JsonObject> {

    private static final Node<ConcurrentMap<String, String>> node
        = Ut.singleton(ZeroLime.class);

    @Override
    public JsonObject read() {
        final JsonObject data = new JsonObject();
        final ConcurrentMap<String, String> keys = node.read();
        final Set<String> skipped = Arrays
            .stream(KPlugin.FILES).collect(Collectors.toSet());
        keys.keySet().stream()
            .filter(item -> !skipped.contains(item))
            .map(key -> ZeroTool.CC_STORAGE.pick(
                () -> Fn.failOr(new JsonObject(), () -> Ut.ioYaml(keys.get(key)), keys.get(key)),
                keys.get(key)
                // Fn.po?l(Storage.CONFIG, keys.get(key), () -> Fn.getJvm(new JsonObject(), () -> Ut.ioYaml(keys.get(key)), keys.get(key)))
            )).forEach(item -> data.mergeIn(item, true));
        /*
            Old Code: ( RxJava2 version )
            Observable.fromIterable(keys.keySet())
            .filter(item -> !skipped.contains(item))
            .map(key -> CC_STORAGE.pick(keys.get(key), () ->
                    Fn.getJvm(new JsonObject(), () -> Ut.ioYaml(keys.get(key)), keys.get(key))
            ))
            .subscribe(item -> data.mergeIn(item, true))
            .dispose();
        */
        return data;
    }
}
