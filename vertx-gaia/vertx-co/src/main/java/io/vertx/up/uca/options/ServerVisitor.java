package io.vertx.up.uca.options;

import io.horizon.uca.log.Annal;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.json.JsonObject;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.em.container.ServerType;

import java.util.concurrent.ConcurrentMap;

/**
 * @param <T>
 *
 * @author Lang
 */
public interface ServerVisitor<T> extends Visitor<ConcurrentMap<Integer, T>> {

    default ServerType serverType() {
        return ServerType.HTTP;
    }

    default int serverPort(final JsonObject config) {
        if (null != config) {
            return config.getInteger(KName.PORT, HttpServerOptions.DEFAULT_PORT);
        }
        return HttpServerOptions.DEFAULT_PORT;
    }

    default boolean isServer(final JsonObject item) {
        return this.serverType().match(item.getString(KName.TYPE));
    }

    default Annal logger() {
        return Annal.get(this.getClass());
    }
}
