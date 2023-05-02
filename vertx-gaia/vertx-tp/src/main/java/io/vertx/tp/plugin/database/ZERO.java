package io.vertx.tp.plugin.database;

import io.horizon.eon.em.app.DatabaseType;
import org.jooq.SQLDialect;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<DatabaseType, SQLDialect> DIALECT = new ConcurrentHashMap<>() {
        {
            this.put(DatabaseType.ORACLE12, SQLDialect.DEFAULT);
            this.put(DatabaseType.MYSQL, SQLDialect.MYSQL);
            this.put(DatabaseType.MYSQL8, SQLDialect.MYSQL);
            this.put(DatabaseType.MYSQL5, SQLDialect.MYSQL);
        }
    };
}
