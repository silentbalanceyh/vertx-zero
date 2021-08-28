package io.vertx.tp.crud.connect;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.ke.atom.KModule;
import io.vertx.up.commune.Envelop;
import io.vertx.up.fn.Fn;
import io.vertx.up.unity.Ux;

/*
 * Configuration for linker `connect` configuration
 */
public interface IxLinker {
    static IxLinker create() {
        return Fn.pool(Pool.LINKER_MAP, CreateLinker.class.getName(),
                CreateLinker::new);
    }

    static IxLinker get() {
        return Fn.pool(Pool.LINKER_MAP, GetLinker.class.getName(),
                GetLinker::new);
    }

    static IxLinker delete() {
        return Fn.pool(Pool.LINKER_MAP, DeleteLinker.class.getName(),
                DeleteLinker::new);
    }

    static IxLinker update() {
        return Fn.pool(Pool.LINKER_MAP, UpdateLinker.class.getName(),
                UpdateLinker::new);
    }

    static IxLinker full() {
        return Fn.pool(Pool.LINKER_MAP, UpdateLinker.class.getName(),
                ViewLinker::new);
    }

    /* Default Implementation for JsonArray and JsonObject */

    default Future<Envelop> joinJAsync(final Envelop request, final JsonObject original, final KModule module) {
        return Ux.future(Envelop.success(original));
    }

    default Future<Envelop> joinAAsync(final Envelop request, final JsonArray original, final KModule module) {
        return Ux.future(Envelop.success(original));
    }
}