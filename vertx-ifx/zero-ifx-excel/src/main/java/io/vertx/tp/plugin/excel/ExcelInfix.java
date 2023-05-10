package io.vertx.tp.plugin.excel;

import io.horizon.uca.cache.Cc;
import io.vertx.core.Vertx;
import io.vertx.up.annotations.Plugin;
import io.vertx.up.plugin.Infix;
import io.vertx.up.unity.Ux;

@Plugin
@SuppressWarnings("all")
public class ExcelInfix implements Infix {

    private static final String NAME = "ZERO_EXCEL_POOL";
    private static final Cc<String, ExcelClient> CC_CLIENT = Cc.open();

    private static void initInternal(final Vertx vertx,
                                     final String name) {
        CC_CLIENT.pick(() -> Infix.init("excel",
            (config) -> ExcelClient.createShared(vertx, config),
            ExcelInfix.class), name);
    }

    public static void init(final Vertx vertx) {
        initInternal(vertx, NAME);
    }

    public static ExcelClient getClient() {
        return CC_CLIENT.store(NAME);
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
