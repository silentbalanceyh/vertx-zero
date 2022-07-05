package io.vertx.tp.ke.booter;

import io.vertx.core.AsyncResult;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.Promise;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.tp.plugin.excel.ExcelInfix;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.tp.plugin.redis.RedisInfix;
import io.vertx.up.fn.Fn;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxTimer;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

class BtLoader {

    /*
     * Environment Init for Split Booter
     */
    static {
        /* Jooq Init */
        JooqInfix.init(Ux.nativeVertx());
        /* Excel Init */
        ExcelInfix.init(Ux.nativeVertx());
        /* Redis Infix to disabled */
        RedisInfix.disabled();
    }

    static Future<Boolean> loadAsync(final String folder) {
        final List<Future<String>> futures = new ArrayList<>();
        stream(folder, null).map(BtKit::complete).forEach(futures::add);
        return Fn.arrangeT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    static Future<Boolean> loadAsync(final String folder, final String prefix) {
        final List<Future<String>> futures = new ArrayList<>();
        stream(folder, prefix).map(BtKit::complete).forEach(futures::add);
        return Fn.arrangeT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    private static Stream<String> stream(final String folder, final String prefix) {
        return Ut.ioFilesN(folder, null, prefix).stream()
            .filter(BtKit::ensure);
    }
}

class BtKit {
    private static final Annal LOGGER = Annal.get(BtKit.class);

    static Handler<AsyncResult<Boolean>> complete(final String folder, final String prefix, final UxTimer timer) {
        return handler -> {
            if (handler.succeeded()) {
                if (Objects.isNull(prefix)) {
                    Ke.infoKe(LOGGER, "The data folder `{0}` has been imported successfully!", folder);
                } else {
                    Ke.infoKe(LOGGER, "The data folder `{0}` with `{1}` has been imported successfully!", folder, prefix);
                }
                timer.end(System.currentTimeMillis());
                Ke.infoKe(LOGGER, "TOTAL EXECUTION TIME = The total execution time = {0}!", timer.value());
                System.exit(0);
            } else {
                handler.cause().printStackTrace();
            }
        };
    }

    static Future<String> complete(final String filename) {
        final Promise<String> promise = Promise.promise();
        execute(filename, handler -> {
            Ke.infoKe(LOGGER, "Successfully to finish loading ! data file = {0}", filename);
            promise.complete(handler.result());
        });
        return promise.future();
    }

    private static void execute(final String filename, final Handler<AsyncResult<String>> callback) {
        final Future<String> future = Ux.nativeWorker(filename, pre -> {
            final ExcelClient client = ExcelInfix.createClient();
            Ke.infoKe(LOGGER, "Excel importing file = {0}", filename);
            client.importAsync(filename, handler -> {
                if (handler.succeeded()) {
                    pre.complete(filename);
                } else {
                    pre.fail(handler.cause());
                }
            });
        });
        future.onComplete(callback);
    }

    static boolean ensure(final String filename) {
        // File not null
        if (Ut.isNil(filename)) {
            return false;
        }
        // Ignore "~" start
        if (filename.contains("~")) {
            return false;
        }
        // Excel only
        return filename.endsWith("xlsx") || filename.endsWith("xls");
    }
}
