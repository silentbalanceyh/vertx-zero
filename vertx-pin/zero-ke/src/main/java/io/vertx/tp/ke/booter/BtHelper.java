package io.vertx.tp.ke.booter;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.core.WorkerExecutor;
import io.vertx.up.util.Ut;

import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BtHelper {

    private static final Vertx VERTX;

    static {
        /* Prepare another vert.x instance */
        final VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTime(3000000000000L);
        options.setMaxWorkerExecuteTime(3000000000000L);
        VERTX = Vertx.vertx(options);
    }

    static Vertx getVertx() {
        return VERTX;
    }

    static WorkerExecutor getWorker(final String name) {
        return VERTX.createSharedWorkerExecutor(name, 2, 5, TimeUnit.MINUTES);
    }

    static Set<String> ioFiles(final String folder, final String prefix) {
        final Stream<String> stream = Ut.ioFiles(folder).stream()
                .filter(file -> !file.startsWith("~"));
        if (Ut.isNil(prefix)) {
            return stream
                    .map(file -> folder + file)
                    .collect(Collectors.toSet());
        } else {
            return stream.filter(file -> file.startsWith(prefix))
                    .map(file -> folder + file)
                    .collect(Collectors.toSet());
        }
    }
}
