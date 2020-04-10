package io.vertx.up.uca.stable;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.ZeroException;
import io.vertx.up.exception.demon.ForbiddenFieldException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

public class ForbiddenInsurer extends AbstractInsurer {
    /**
     * @param data input data that should be verified.
     * @param rule rule config data
     * @throws ZeroException Insure exception
     */
    @Override
    public void flumen(final JsonObject data, final JsonObject rule) throws ZeroException {
        // 1. If rule is null, skip
        Fn.onZero(() -> {
            // 2. Extract rule from config.
            if (rule.containsKey(Rules.FORBIDDEN)) {
                final JsonArray fields = Ut.toJArray(rule.getValue(Rules.FORBIDDEN));
                Fn.etJArray(fields, String.class, (field, index) -> {
                    // 3. Check if data contains field.
                    Fn.outZero(data.containsKey(field), this.getLogger(),
                            ForbiddenFieldException.class,
                            this.getClass(), data, field);
                });
            }
        }, rule, data);
    }
}
