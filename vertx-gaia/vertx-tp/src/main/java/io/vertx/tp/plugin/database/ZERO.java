package io.vertx.tp.plugin.database;

import io.horizon.eon.em.app.DsCategory;
import org.jooq.SQLDialect;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<DsCategory, SQLDialect> DIALECT = new ConcurrentHashMap<>() {
        {
            this.put(DsCategory.ORACLE12, SQLDialect.DEFAULT);
            this.put(DsCategory.MYSQL, SQLDialect.MYSQL);
            this.put(DsCategory.MYSQL8, SQLDialect.MYSQL);
            this.put(DsCategory.MYSQL5, SQLDialect.MYSQL);
        }
    };
}
