package io.vertx.tp.plugin.excel;

import io.vertx.core.Vertx;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.Infix;
import io.vertx.up.unity.Ux;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Plugin
@SuppressWarnings("all")
public class ExcelInfix implements Infix {

    private static final String NAME = "ZERO_EXCEL_POOL";

    private static final ConcurrentMap<String, ExcelClient> CLIENTS
        = new ConcurrentHashMap<>();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        Fn.pool(CLIENTS, name,
            () -> Infix.init("excel",
                (config) -> ExcelClient.createShared(vertx, config),
                ExcelInfix.class));
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static ExcelClient getClient() {
        return CLIENTS.get(NAME);
    }

    public static ExcelClient createClient() {
        return createClient(Ux.nativeVertx());
    }

    public static ExcelClient createClient(final Vertx vertx) {
        return Infix.init("excel", (config) -> ExcelClient.createShared(vertx, config), ExcelInfix.class);
    }

    @Override
    public ExcelClient get() {
        return getClient();
    }
}
