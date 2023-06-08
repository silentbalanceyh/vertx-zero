package io.vertx.up.plugin.database;

import io.horizon.eon.em.EmDS;
import org.jooq.SQLDialect;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<EmDS.Category, SQLDialect> DIALECT = new ConcurrentHashMap<>() {
        {
            this.put(EmDS.Category.ORACLE12, SQLDialect.DEFAULT);
            this.put(EmDS.Category.MYSQL, SQLDialect.MYSQL);
            this.put(EmDS.Category.MYSQL8, SQLDialect.MYSQL);
            this.put(EmDS.Category.MYSQL5, SQLDialect.MYSQL);
        }
    };
}
