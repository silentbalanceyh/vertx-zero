package io.vertx.tp.jet.uca.param;

import io.vertx.tp.jet.cv.em.ParamMode;
import io.vertx.tp.optic.jet.JtIngest;
import io.vertx.up.fn.Fn;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;

interface Pool {

    ConcurrentMap<String, JtIngest> POOL_INGEST_QUERY = new ConcurrentHashMap<>();
    ConcurrentMap<String, JtIngest> POOL_INGEST_PATH = new ConcurrentHashMap<>();
    ConcurrentMap<String, JtIngest> POOL_INGEST_BODY = new ConcurrentHashMap<>();
    ConcurrentMap<String, JtIngest> POOL_INGEST_DEFINE = new ConcurrentHashMap<>();
    ConcurrentMap<String, JtIngest> POOL_INGEST_FILE = new ConcurrentHashMap<>();

    ConcurrentMap<ParamMode, Supplier<JtIngest>> INNER_INGEST = new ConcurrentHashMap<ParamMode, Supplier<JtIngest>>() {
        {
            this.put(ParamMode.QUERY, () -> Fn.poolThread(POOL_INGEST_QUERY, QueryIngest::new));
            this.put(ParamMode.BODY, () -> Fn.poolThread(POOL_INGEST_BODY, BodyIngest::new));
            this.put(ParamMode.DEFINE, () -> Fn.poolThread(POOL_INGEST_DEFINE, DefineIngest::new));
            this.put(ParamMode.PATH, () -> Fn.poolThread(POOL_INGEST_PATH, PathIngest::new));
            this.put(ParamMode.FILE, () -> Fn.poolThread(POOL_INGEST_FILE, FileIngest::new));
        }
    };
}
