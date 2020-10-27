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
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

class BtLoader {

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

    /*
     * Import batch operation here
     * 1) Import all excel data under `folder`
     * 2) Import all excel files with `prefix` named under `folder`
     * 3) Import all excel data with `Handler` callback
     */
    static void doImports(final String folder) {
        stream(folder).forEach(BtLoader::doImport);
    }

    static void doImports(final String folder, final String prefix) {
        stream(folder)
                .filter(filename -> filename.startsWith(folder + prefix))
                .forEach(BtLoader::doImport);
    }

    static Future<Boolean> impAsync(final String folder) {
        final List<Future<String>> futures = new ArrayList<>();
        stream(folder).map(BtLoader::importFuture).forEach(futures::add);
        return Ux.thenCombineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    static Future<Boolean> impAsync(final String folder, final String prefix) {
        final List<Future<String>> futures = new ArrayList<>();
        stream(folder)
                .filter(filename -> filename.startsWith(folder + prefix))
                .map(BtLoader::importFuture).forEach(futures::add);
        return Ux.thenCombineT(futures).compose(nil -> Future.succeededFuture(Boolean.TRUE));
    }

    @SuppressWarnings("all")
    static void doImports(final String folder, final Handler<AsyncResult<List<String>>> callback) {
        final List<Future> futures = stream(folder)
                .map(BtLoader::importFuture)
                .collect(Collectors.toList());
        CompositeFuture.join(futures).compose(result -> {
            final List<String> async = result.list();
            callback.handle(Future.succeededFuture(async));
            return Future.succeededFuture(Boolean.TRUE);
        });
    }

    /*
     * Import single file data here
     */
    private static void doImport(final String filename) {
        doImport(filename, handler -> out(handler.result()));
    }

    static void doImport(final String filename, final Handler<AsyncResult<String>> callback) {
        /*
         * Build more excel client
         */
        /*
        final ExcelClient client = ExcelInfix.getClient();
        client.importAsync(filename, handler -> {
            out(filename);
            callback.handle(Future.succeededFuture(filename));
        });*/

        final WorkerExecutor executor = Ux.nativeWorker(filename);
        executor.<String>executeBlocking(
                pre -> {
                    final ExcelClient client = ExcelInfix.createClient();
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
        final List<Future> futures = stream(folder)
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

    /*
     * Private methods
     */
    private static Stream<String> stream(final String folder) {
        return Ut.ioFiles(folder).stream()
                .filter(file -> !file.startsWith("~"))
                .map(file -> folder + file);
    }

    private static void out(final String filename) {
        final Annal logger = Annal.get(BtLoader.class);
        Ke.infoKe(logger, "Successfully to finish loading ! data file = {0}", filename);
    }

    private static Future<Set<ExTable>> ingestFuture(final String filename) {
        final Promise<Set<ExTable>> promise = Promise.promise();
        doIngest(filename, handler -> promise.complete(handler.result()));
        return promise.future();
    }

    private static Future<String> importFuture(final String filename) {
        final Promise<String> promise = Promise.promise();
        doImport(filename, handler -> promise.complete(handler.result()));
        return promise.future();
    }
}
