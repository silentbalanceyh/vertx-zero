package io.vertx.up.uca.stable;

import io.horizon.exception.ProgramException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.daemon.ForbiddenFieldException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

public class ForbiddenInsurer extends AbstractInsurer {
    /**
     * @param data input data that should be verified.
     * @param rule rule config data
     *
     * @throws ProgramException Insure exception
     */
    @Override
    public void flumen(final JsonObject data, final JsonObject rule) throws ProgramException {
        // 1. If rule is null, skip
        Fn.bugAt(() -> {
            // 2. Extract rule from config.
            if (rule.containsKey(Rules.FORBIDDEN)) {
                final JsonArray fields = Ut.toJArray(rule.getValue(Rules.FORBIDDEN));
                Fn.bugIt(fields, String.class, (field, index) -> {
                    // 3. Check if data contains field.
                    Fn.outBug(data.containsKey(field), this.getLogger(),
                        ForbiddenFieldException.class,
                        this.getClass(), data, field);
                });
            }
        }, rule, data);
    }
}
