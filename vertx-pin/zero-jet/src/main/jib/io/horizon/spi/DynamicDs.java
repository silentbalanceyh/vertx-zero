package io.horizon.spi;

import io.macrocosm.specification.program.HArk;
import io.modello.atom.app.KDS;
import io.vertx.core.MultiMap;
import io.vertx.mod.jet.error._501DataSourceException;
import io.vertx.mod.ke.refine.Ke;
import io.vertx.up.commune.config.Database;
import io.vertx.up.fn.Fn;
import io.vertx.up.plugin.database.DS;
import io.vertx.up.plugin.database.DataPool;

import java.util.Objects;

/*
 * Dynamic Data Source
 */
public class DynamicDs implements DS {

    @Override
    public DataPool switchDs(final MultiMap headers) {
        final HArk ark = Ke.ark(headers);
        Fn.out(Objects.isNull(ark), _501DataSourceException.class, this.getClass(), headers.toString());
        return this.getDs(ark);
    }

    @Override
    public DataPool switchDs(final String sigma) {
        final HArk ark = Ke.ark(sigma);
        Fn.out(Objects.isNull(ark), _501DataSourceException.class, this.getClass(), sigma);
        return this.getDs(ark);
    }

    private DataPool getDs(final HArk ark) {
        /*
         * DataPool get here，For each database, it's two
         * 1) Default database with or without auto commit;
         * 2) Remove auto commit to switch to auto commit = true, a new database
         * 3) Auto commit database will be managed by DataPool, it could switch by DataPool itself
         */
        final KDS<Database> ds = ark.database();
        final Database database = ds.dynamic();
        return DataPool.createAuto(database);
    }
}
