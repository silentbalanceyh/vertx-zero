package io.vertx.tp.ke.booter;

import io.vertx.core.*;
import io.vertx.tp.ke.refine.Ke;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.tp.plugin.excel.ExcelInfix;
import io.vertx.tp.plugin.excel.atom.ExTable;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.tp.plugin.redis.RedisInfix;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.unity.UxTimer;
import io.vertx.up.util.Ut;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BtLoader {
    private static final Annal LOGGER = Annal.get(BtLoader.class);

    /*
     * Environment Init for Split Booter
     */
    static {
        /* Excel Init */
        ExcelInfix.init(Ux.nativeVertx());
        /* Jooq Init */
        JooqInfix.init(Ux.nativeVertx());
        /* Redis Infix to disabled */
        RedisInfix.disabled();
    }

    static Handler<AsyncResult<Boolean>> handlerComplete(final String folder, final String prefix, final UxTimer timer) {
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

    static Future<Boolean> impAsync(final String folder) {
        final List<Future<String>> futures = new ArrayList<>();
        stream(folder, null).map(BtLoader::importFuture).forEach(futures::add);
        return Ux.thenCombineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    static Future<Boolean> impAsync(final String folder, final String prefix) {
        final List<Future<String>> futures = new ArrayList<>();
        stream(folder, prefix).map(BtLoader::importFuture).forEach(futures::add);
        return Ux.thenCombineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    static void doImport(final String filename, final Handler<AsyncResult<String>> callback) {
        final WorkerExecutor executor = Ux.nativeWorker(filename);
        executor.<String>executeBlocking(
                pre -> {
                    final ExcelClient client = ExcelInfix.createClient();
                    Ke.infoKe(LOGGER, "Excel importing file = {0}", filename);
                    client.importAsync(filename, handler -> {
                        if (handler.succeeded()) {
                            pre.complete(filename);
                        } else {
                            pre.fail(handler.cause());
                        }
                    });
                },
                post -> callback.handle(Future.succeededFuture(post.result()))
        );
    }

    /*
     * Ingest data under `folder` here
     */
    @SuppressWarnings("all")
    static void doIngests(final String folder, final Handler<AsyncResult<Set<ExTable>>> callback) {
        final List<Future> futures = stream(folder, null)
                .map(BtLoader::ingestFuture)
                .collect(Collectors.toList());
        CompositeFuture.join(futures).compose(result -> {
            final List<Set<ExTable>> async = result.list();
            final Set<ExTable> tables = new HashSet<>();
            async.forEach(tables::addAll);
            callback.handle(Future.succeededFuture(tables));
            return Future.succeededFuture(Boolean.TRUE);
        });
    }

    static void doIngest(final String filename, final Handler<AsyncResult<Set<ExTable>>> callback) {
        final ExcelClient client = ExcelInfix.getClient();
        client.ingest(filename, handler -> callback.handle(Future.succeededFuture(handler.result())));
    }

    private static Stream<String> stream(final String folder, final String prefix) {
        return Ut.ioFilesN(folder, null, prefix).stream()
                .filter(BtLoader::ensureFile);
    }

    private static boolean ensureFile(final String filename) {
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

    private static void out(final String filename) {
        Ke.infoKe(LOGGER, "Successfully to finish loading ! data file = {0}", filename);
    }

    private static Future<Set<ExTable>> ingestFuture(final String filename) {
        final Promise<Set<ExTable>> promise = Promise.promise();
        doIngest(filename, handler -> promise.complete(handler.result()));
        return promise.future();
    }

    private static Future<String> importFuture(final String filename) {
        final Promise<String> promise = Promise.promise();
        doImport(filename, handler -> {
            promise.complete(handler.result());
            out(filename);
        });
        return promise.future();
    }
}
