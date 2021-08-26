package io.vertx.tp.crud.uca.op;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.tp.crud.init.IxPin;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxIn;
import io.vertx.tp.crud.uca.input.Pre;
import io.vertx.tp.ke.atom.KField;
import io.vertx.up.uca.jooq.UxJooq;
import io.vertx.up.unity.Ux;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AgonicImport implements Agonic {
    @Override
    public Future<JsonArray> runAAsync(final JsonArray input, final IxIn in) {
        final KField fieldConfig = in.module().getField();
        final JsonArray matrix = fieldConfig.getUnique();
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
        ).compose(compared -> {
            final List<Future<JsonArray>> combine = new ArrayList<>();
            return null;
        });
    }

    private Future<JsonArray> runCompress(final JsonArray source, final IxIn in) {
        final KField fieldConfig = in.module().getField();
        final JsonArray matrix = fieldConfig.getUnique();
        final JsonArray normalized = Ux.ruleJReduce(source, matrix);
        return Ux.future(normalized);
    }
}
