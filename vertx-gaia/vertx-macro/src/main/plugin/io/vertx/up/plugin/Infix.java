package io.vertx.up.plugin;

import io.horizon.uca.log.Annal;
import io.vertx.core.json.JsonObject;
import io.vertx.up.commune.Ruler;
import io.vertx.up.exception.booting.ConfigKeyMissingException;
import io.vertx.up.fn.Fn;
import io.vertx.up.runtime.ZeroStore;

import java.util.function.Function;

public interface Infix {
    static <R> R init(final String key,
                      final Function<JsonObject, R> executor,
                      final Class<?> clazz) {
        final Annal logger = Annal.get(clazz);
        Fn.outBoot(!ZeroStore.is(key), logger, ConfigKeyMissingException.class,
            clazz, key);
        final JsonObject options = ZeroStore.option(key);
        Fn.outBug(() -> Ruler.verify(key, options), logger);
        return executor.apply(options);
    }

    <T> T get();
}
