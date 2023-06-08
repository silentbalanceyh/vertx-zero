package io.vertx.up.uca.stable;

import io.horizon.exception.ProgramException;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.daemon.RequiredFieldException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

/**
 * Required validation
 */
public class RequiredInsurer extends AbstractInsurer {
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
            // 2. extract rule from rule, only required accept
            if (rule.containsKey(Rules.REQUIRED)) {
                final JsonArray fields = Ut.toJArray(rule.getValue(Rules.REQUIRED));
                Fn.bugIt(fields, String.class, (field, index) -> {
                    // 3.Check if data contains field.
                    // Fast throw out
                    Fn.outBug(!data.containsKey(field), this.getLogger(),
                        RequiredFieldException.class,
                        this.getClass(), data, field);
                });
            }
        }, rule, data);
    }
}
