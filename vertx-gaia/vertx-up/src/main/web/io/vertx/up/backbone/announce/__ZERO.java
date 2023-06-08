package io.vertx.up.backbone.announce;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.FileUpload;

import java.io.File;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

interface CACHE {

    ConcurrentMap<Class<?>, Rigor> RIGORS = new ConcurrentHashMap<Class<?>, Rigor>() {
        {
            /* JsonObject & JsonArray */
            this.put(JsonObject.class, new JObjectRigor());
            this.put(JsonArray.class, new JArrayRigor());
            /* File & FileUpload for @Codex */
            this.put(File.class, new FileRigor());
            this.put(FileUpload.class, new FileRigor());
        }
    };
}
