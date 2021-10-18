package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.ke.atom.specification.KField;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
class AgonicImport implements Agonic {
    @Override
    public Future<JsonArray> runAAsync(final JsonArray input, final IxMod in) {
        final KField fieldConfig = in.module().getField();
        final JsonArray matrix = Ix.onMatrix(fieldConfig);
        /*
         * Header And Compress
         */
        final UxJooq jooq = IxPin.jooq(in);
        return Ix.passion(input, in,
            Pre.head()::inAAsync,        /* Header Value */
            this::runCompress            /* Compress */
        ).compose(processed -> Pre.qUk().inAJAsync(processed, in)
            .compose(jooq::fetchJAsync)
            /* Compared Data */
            .compose(original -> Ux.future(Ux.compareJ(original, processed, matrix)))
        ).compose(compared -> this.runSave(compared, in));
    }

    private Future<JsonArray> runSave(final ConcurrentMap<ChangeFlag, JsonArray> compared,
                                      final IxMod in) {
        final List<Future<JsonArray>> combine = new ArrayList<>();
        final JsonArray inserted = compared.getOrDefault(ChangeFlag.ADD, new JsonArray());
        if (!inserted.isEmpty()) {
            combine.add(Agonic.write(ChangeFlag.ADD).runAAsync(inserted, in));
        }
        final JsonArray updated = compared.getOrDefault(ChangeFlag.UPDATE, new JsonArray());
        if (!updated.isEmpty()) {
            combine.add(Agonic.write(ChangeFlag.UPDATE).runAAsync(updated, in));
        }
        return Ux.thenCombineArray(combine);
    }

    private Future<JsonArray> runCompress(final JsonArray source, final IxMod in) {
        final KField fieldConfig = in.module().getField();
        final JsonArray matrix = fieldConfig.getUnique();   // Here the `key` should be ignored
        final JsonArray normalized = Ux.ruleJReduce(source, matrix);
        return Ux.future(normalized);
    }
}
