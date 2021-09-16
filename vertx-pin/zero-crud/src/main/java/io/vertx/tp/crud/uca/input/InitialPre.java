package io.vertx.tp.crud.uca.input;

import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.crud.refine.Ix;
import io.vertx.tp.crud.uca.desk.IxMod;
import io.vertx.tp.ke.atom.specification.KModule;
import io.vertx.tp.ke.atom.specification.KTransform;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.Objects;

/**
 * Support Variable
 *
 * 1. module
 * 2. sigma
 * 3. language
 * 4. appId
 *
 * Other variable should use fixed value instead of expression
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class InitialPre implements Pre {
    @Override
    public Future<JsonArray> inAAsync(final JsonArray data, final IxMod in) {
        // Modify data directly
        Ut.itJArray(data).forEach(each -> this.initial(each, in));
        return Ux.future(data);
    }

    private void initial(final JsonObject data, final IxMod in) {
        final KModule module = in.module();
        // Pre-Condition Checking
        final KTransform transform = module.getTransform();
        if (Objects.isNull(transform)) {
            return;
        }
        final JsonObject initial = transform.getInitial();
        if (Ut.isNil(initial)) {
            return;
        }
        // Arguments Processing
        final JsonObject args = Ix.onParameters(in);
        Ut.<String>itJObject(initial, (expr, field) -> {
            // Append Only
            if (!data.containsKey(field)) {
                if (expr.contains("`")) {
                    // Expression Mode
                    final String value = Ut.fromExpression(expr, args);
                    data.put(field, value);
                } else {
                    // Value Mode
                    data.put(field, expr);
                }
            }
        });
    }
}
