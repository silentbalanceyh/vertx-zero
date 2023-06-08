package io.macrocosm.atom.context;

import io.horizon.eon.em.EmApp;
import io.horizon.exception.web._501NotSupportException;
import io.macrocosm.specification.app.HAmbient;
import io.macrocosm.specification.program.HArk;
import io.vertx.core.json.JsonObject;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 标准化应用容器池，用于在运行环境中存储应用程序基本信息，按照租户本身存在的内容执行梯度化处理：
 * <pre><code>
 *     1. app / tenant / owner
 *     2. app / tenant /
 *     3. app {@link HArk}
 * </code></pre>
 *
 * @author lang : 2023-06-05
 */
public class KAmbient implements HAmbient {
    private final ConcurrentMap<String, JsonObject> configuration = new ConcurrentHashMap<>();
    private final KAmbientContext context = new KAmbientContext();
    private final KAmbientRuntime vector = new KAmbientRuntime();
    private EmApp.Mode mode;

    private KAmbient() {
        this.mode = EmApp.Mode.CUBE;
    }

    public static HAmbient of() {
        return new KAmbient();
    }

    @Override
    public EmApp.Mode mode() {
        return this.mode;
    }

    @Override
    public HArk running(final String key) {
        Objects.requireNonNull(key);
        final String keyFind = this.vector.keyFind(key);
        return this.context.running(keyFind);
    }

    @Override
    public HArk running() {
        if (EmApp.Mode.CUBE != this.mode) {
            throw new _501NotSupportException(this.getClass());
        }
        return this.context.running();
    }

    @Override
    public JsonObject extension(final String name) {
        return this.configuration.getOrDefault(name, new JsonObject());
    }

    @Override
    public ConcurrentMap<String, HArk> app() {
        return this.context.app();
    }


    @Override
    public synchronized HAmbient registry(final String extension, final JsonObject configuration) {
        this.configuration.put(extension, configuration);
        return this;
    }

    @Override
    public synchronized HAmbient registry(final HArk ark) {
        // 1. 注册应用
        this.mode = this.context.registry(ark);
        // 2. 运行时绑定
        this.vector.registry(ark, this.mode);
        return this;
    }
}
