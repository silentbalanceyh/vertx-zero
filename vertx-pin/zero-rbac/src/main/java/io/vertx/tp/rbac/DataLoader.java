package io.vertx.tp.rbac;

import io.vertx.core.Vertx;
import io.vertx.core.VertxOptions;
import io.vertx.tp.plugin.excel.ExcelClient;
import io.vertx.tp.plugin.excel.ExcelInfix;
import io.vertx.tp.plugin.jooq.JooqInfix;
import io.vertx.up.log.Annal;

/*
 * Internal specific tool to load the data here.
 * It's for RBAC demo and it depend on zero-excel module
 */
public class DataLoader {

    public static void main(final String[] args) {
        /* Prepared */
        final VertxOptions options = new VertxOptions();
        options.setMaxEventLoopExecuteTime(30000000000L);
        final Vertx vertx = Vertx.vertx(options);
        // Excel
        ExcelInfix.init(vertx);
        JooqInfix.init(vertx);
        /* ExcelClient */
        final ExcelClient client = ExcelInfix.getClient();
        client.loading("plugin/rbac/excel/data.rbac.xlsx", handler -> {
            /* */
            final Annal LOGGER = Annal.get(DataLoader.class);
            LOGGER.info("[ Έξοδος ] Successfully to finish loading !");
        });
    }
}
