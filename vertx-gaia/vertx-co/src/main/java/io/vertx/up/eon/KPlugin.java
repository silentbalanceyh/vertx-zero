package io.vertx.up.eon;

import io.vertx.up.runtime.ZeroYml;

import javax.inject.Inject;
import javax.inject.infix.*;
import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Default plugin applied to vertx
 */
public interface KPlugin {

    String[] FILES = new String[]{
        ZeroYml._inject,
        ZeroYml._error,
        ZeroYml._server,
        ZeroYml._resolver,
    };
    ConcurrentMap<Class<? extends Annotation>, String> INFIX_MAP =
        new ConcurrentHashMap<Class<? extends Annotation>, String>() {
            {
                this.put(Mongo.class, ZeroYml.inject.mongo);
                this.put(MySql.class, ZeroYml.inject.mysql);
                this.put(Jooq.class, ZeroYml.inject.jooq);
                this.put(Rpc.class, ZeroYml.inject.rpc);
                this.put(Redis.class, ZeroYml.inject.redis);
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
            this.add(ZeroYml.inject.mongo);
            this.add(ZeroYml.inject.mysql);
            this.add(ZeroYml.inject.redis);
            this.add(ZeroYml.inject.rpc);
            this.add(ZeroYml.inject.jooq);
            this.add(ZeroYml.inject.logger);
        }
    };
}
