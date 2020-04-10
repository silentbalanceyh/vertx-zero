package io.vertx.up.eon;

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
public interface Plugins {

    String INJECT = "inject";

    String SERVER = "server";

    String ERROR = "error";

    String RESOLVER = "resolver";

    String[] DATA = new String[]{
            INJECT, ERROR, SERVER,
            RESOLVER
    };
    ConcurrentMap<Class<? extends Annotation>, String> INFIX_MAP =
            new ConcurrentHashMap<Class<? extends Annotation>, String>() {
                {
                    put(Mongo.class, Infix.MONGO);
                    put(MySql.class, Infix.MYSQL);
                    put(Jooq.class, Infix.JOOQ);
                    put(Rpc.class, Infix.RPC);
                    put(Redis.class, Infix.REDIS);
                }
            };
    Set<Class<? extends Annotation>> INJECT_ANNOTATIONS = new HashSet<Class<? extends Annotation>>() {
        {
            addAll(INFIX_MAP.keySet());
            add(Inject.class);
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
    interface Infix {

        String MONGO = "mongo";

        String MYSQL = "mysql";

        String REDIS = "redis";

        String SESSION = "session";

        String SHARED = "shared";


        String JOOQ = "jooq";
        String RPC = "rpc";
        String SECURE = "secure";

        String LOGGER = "logger";
        /*
         *
         */
        Set<String> STANDAND = new HashSet<String>() {
            {
                add(MONGO);
                add(MYSQL);
                add(REDIS);
                // Could not put session  / shared in to standard
                // this.add(SESSION);
                // this.add(SHARED);

                add(RPC);
                add(JOOQ);
                add(LOGGER);
            }
        };
    }

    interface Default{
        String WALL_MONGO = "io.vertx.tp.plugin.mongo.MongoWall";
        String AGENT_RPC = "io.vertx.up.verticle.ZeroRpcAgent";
        String AGENT_API = "io.vertx.up.verticle.ZeroApiAgent";
    }
}
