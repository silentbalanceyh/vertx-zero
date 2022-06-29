package io.vertx.tp.plugin.stomp.websocket;

import io.vertx.ext.stomp.StompServerHandler;
import io.vertx.ext.stomp.StompServerOptions;
import io.vertx.up.uca.cache.Cc;
import io.vertx.up.util.Ut;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Mixer {

    Cc<String, Mixer> CC_MIXER = Cc.openThread();

    static Mixer instance(final Class<?> clazz, final Object... args) {
        return CC_MIXER.pick(() -> Ut.instance(clazz, args), clazz.getName());
    }

    <T> T mount(StompServerHandler handler, StompServerOptions options);

    default <T> T mount(final StompServerHandler handler) {
        return this.mount(handler, null);
    }
}
