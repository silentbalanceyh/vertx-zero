package io.modello.dynamic.modular.query;

import io.modello.eon.em.EmModel;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface Pool {

    ConcurrentMap<EmModel.Type, Ingest> INGEST_POOL =
        new ConcurrentHashMap<EmModel.Type, Ingest>() {
            {
                this.put(EmModel.Type.DIRECT, new DirectIngest());
                this.put(EmModel.Type.JOINED, new JoinIngest());
                this.put(EmModel.Type.VIEW, new ViewIngest());
            }
        };
}
