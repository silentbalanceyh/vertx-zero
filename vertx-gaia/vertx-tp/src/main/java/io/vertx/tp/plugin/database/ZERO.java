package io.vertx.tp.plugin.database;

import io.vertx.up.eon.em.DatabaseType;
import io.vertx.up.uca.cache.Cc;
import org.jooq.SQLDialect;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    Cc<String, DataPool> CC_POOL_DYNAMIC = Cc.open();

    ConcurrentMap<DatabaseType, SQLDialect> DIALECT
        = new ConcurrentHashMap<DatabaseType, SQLDialect>() {
        {
            this.put(DatabaseType.MYSQL5, SQLDialect.MYSQL);
            this.put(DatabaseType.ORACLE12, SQLDialect.DEFAULT);
        }
    };
}
