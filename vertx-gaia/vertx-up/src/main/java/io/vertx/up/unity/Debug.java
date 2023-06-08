package io.vertx.up.unity;

import io.horizon.exception.WebException;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.core.shareddata.ClusterSerializable;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.function.Function;
import java.util.function.Supplier;

class Debug {

    static void monitorTc(final ClusterSerializable cluster) {
        if (cluster instanceof final JsonObject clusterJ) {
            System.out.println(clusterJ.encodePrettily());
        } else if (cluster instanceof final JsonArray clusterA) {
            System.out.println(clusterA.encodePrettily());
        } else {
            System.out.println(cluster.toString());
        }
    }

    static void monitor(final Object... objects) {
        for (final Object reference : objects) {
            Debug.monitor(reference);
        }
    }

    private static void monitor(final Object object) {
        final StringBuilder builder = new StringBuilder();
        builder.append("\t\t[ ZERO Debug ] ---> Start \n");
        builder.append("\t\t[ ZERO Debug ] object = ").append(object).append("\n");
        if (null != object) {
            builder.append("\t\t[ ZERO Debug ] type = ").append(object.getClass()).append("\n");
            builder.append("\t\t[ ZERO Debug ] json = ").append(Ut.serialize(object)).append("\n");
            builder.append("\t\t[ ZERO Debug ] toString = ").append(object).append("\n");
            builder.append("\t\t[ ZERO Debug ] hashCode = ").append(object.hashCode()).append("\n");
        }
        builder.append("\t\t[ ZERO Debug ] <--- End \n");
        System.err.println(builder);
    }

    static <T> Future<T> debug(final T item) {
        Fn.runAt(() -> Debug.monitor(item), item);
        return Future.succeededFuture(item);
    }

    static <T> T debug(final Throwable error, final Supplier<T> supplier) {
        if (Objects.nonNull(error)) {
            // TODO: Debug for JVM;
            error.printStackTrace();
        }
        return supplier.get();
    }

    static Function<Throwable, Envelop> otherwise() {
        return error -> {
            if (Objects.nonNull(error)) {
                error.printStackTrace();
                if (error instanceof WebException) {
                    return Envelop.failure(((WebException) error));
                } else {
                    return Envelop.failure(error);
                }
            } else {
                return Envelop.ok();
            }
        };
    }

    static <T> Function<Throwable, T> otherwise(final Supplier<T> supplier) {
        return error -> {
            if (Objects.nonNull(error)) {
                error.printStackTrace();
            }
            return supplier.get();
        };
    }
}
