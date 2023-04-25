package io.horizon.spi;

import io.vertx.core.MultiMap;
import io.vertx.tp.error._501DataSourceException;
import io.vertx.tp.jet.atom.JtApp;
import io.horizon.spi.environment.Ambient;
import io.vertx.tp.plugin.database.DS;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.commune.config.Database;
import io.vertx.up.fn.Fn;

import java.util.Objects;

/*
 * Dynamic Data Source
 */
@SuppressWarnings("all")
public class DynamicDs implements DS {
    @Override
    public DataPool switchDs(final MultiMap headers) {
        /*
         * Get app
         */
        final JtApp app = Ambient.getCurrent(headers);
        /*
         * If app is not null
         */
        Fn.out(Objects.isNull(app), _501DataSourceException.class, this.getClass(), headers.toString());
        return this.getDs(app);
    }

    public DataPool switchDs(final String sigma) {
        final JtApp app = Ambient.getApp(sigma);
        /*
         * If app is not null
         */
        Fn.out(Objects.isNull(app), _501DataSourceException.class, this.getClass(), sigma);
        return this.getDs(app);
    }

    private DataPool getDs(final JtApp app) {
        /*
         * DataPool get here，For each database, it's two
         * 1) Default database with or without auto commit;
         * 2) Remove auto commit to switch to auto commit = true, a new database
         * 3) Auto commit database will be managed by DataPool, it could switch by DataPool itself
         */
        final Database database = app.getSource();
        return DataPool.createAuto(database);
    }
}
