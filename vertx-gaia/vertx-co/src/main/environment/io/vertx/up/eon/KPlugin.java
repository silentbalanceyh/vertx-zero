package io.vertx.up.eon;

import io.vertx.up.eon.configure.YmlCore;
import jakarta.inject.Inject;
import jakarta.inject.infix.*;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Default plugin applied to vertx
 */
public interface KPlugin {

    String[] FILE_KEY = new String[]{
        YmlCore.inject.__KEY,
        YmlCore.error.__KEY,
        YmlCore.server.__KEY,
        YmlCore.resolver.__KEY,
    };
    ConcurrentMap<Class<? extends Annotation>, String> INFIX_MAP =
        new ConcurrentHashMap<Class<? extends Annotation>, String>() {
            {
                this.put(Mongo.class, YmlCore.inject.MONGO);
                this.put(MySql.class, YmlCore.inject.MYSQL);
                this.put(Jooq.class, YmlCore.inject.JOOQ);
                this.put(Rpc.class, YmlCore.inject.RPC);
                this.put(Redis.class, YmlCore.inject.REDIS);
            }
        };
    Set<Class<? extends Annotation>> INJECT_ANNOTATIONS = new HashSet<Class<? extends Annotation>>() {
        {
            this.addAll(INFIX_MAP.keySet());
            this.add(Inject.class);
        }
    };

    /*
     * Default supported in different file here.
     * vertx-mongo.yml: MongoClient ( Native )
     * vertx-mysql.yml: MySQLClient ( Native )
     * vertx-jooq.yml: Jooq ( Zero Provided )
     * vertx-rpc.yml: RpcClient ( Zero Provided )
     * vertx-redis.yml: RedisClient ( Native )
     */
    Set<String> INJECT_STANDARD = new HashSet<String>() {
        {
            this.add(YmlCore.inject.MONGO);
            this.add(YmlCore.inject.MYSQL);
            this.add(YmlCore.inject.REDIS);
            this.add(YmlCore.inject.RPC);
            this.add(YmlCore.inject.JOOQ);
            this.add(YmlCore.inject.LOGGER);
        }
    };
}
