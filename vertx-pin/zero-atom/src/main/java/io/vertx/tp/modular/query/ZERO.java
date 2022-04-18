package io.vertx.tp.modular.query;

import io.vertx.up.eon.em.atom.ModelType;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<ModelType, Ingest> INGEST_POOL =
        new ConcurrentHashMap<ModelType, Ingest>() {
            {
                this.put(ModelType.DIRECT, new DirectIngest());
                this.put(ModelType.JOINED, new JoinIngest());
                this.put(ModelType.VIEW, new ViewIngest());
            }
        };
}
