package io.vertx.up.uca.stable;

import io.horizon.eon.em.EmType;
import io.horizon.exception.ProgramException;
import io.vertx.core.json.JsonObject;
import io.vertx.up.exception.daemon.DataTypeWrongException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Function;

/**
 * Type Validation
 */
public class TypedInsurer extends AbstractInsurer {

    private static final ConcurrentMap<EmType.Json, Function<Object, Boolean>>
        FUNS = new ConcurrentHashMap<EmType.Json, Function<Object, Boolean>>() {
        {
            this.put(EmType.Json.BOOLEAN, Ut::isBoolean);
            this.put(EmType.Json.STRING, (input) -> Boolean.TRUE);
            this.put(EmType.Json.INTEGER, Ut::isInteger);
            this.put(EmType.Json.DECIMAL, Ut::isDecimal);
            this.put(EmType.Json.DATE, Ut::isDate);
            this.put(EmType.Json.JOBJECT, Ut::isJObject);
            this.put(EmType.Json.JARRAY, Ut::isJArray);
            this.put(EmType.Json.CLASS, Ut::isClass);
        }
    };

    /**
     * @param data input data that should be verified.
     * @param rule rule config data
     *
     * @throws ProgramException Insure exception
     * @see "STRING | INTEGER | DECIMAL | BOOLEAN | JOBJECT | JARRAY | DATE"
     */
    @Override
    public void flumen(final JsonObject data, final JsonObject rule)
        throws ProgramException {
        // 1. If rule is null, skip
        Fn.bugAt(() -> {
            // 2. extract rule from rule, only required accept
            if (rule.containsKey(Rules.TYPED)) {
                final JsonObject fields = rule.getJsonObject(Rules.TYPED);
                Fn.bugIt(fields, (item, name) -> {
                    // 3. extract key for field definition
                    final EmType.Json key = Ut.toEnum(item.toString(), EmType.Json.class);
                    final Function<Object, Boolean> fnTest
                        = FUNS.getOrDefault(key, (input) -> Boolean.TRUE);
                    // 4. checking handler, the pre-condition is that data contains checked key.
                    if (data.containsKey(name)) {
                        final Object value = data.getValue(name);

                        Fn.outBug(!fnTest.apply(data.getValue(name)), this.getLogger(),
                            DataTypeWrongException.class,
                            this.getClass(), name, value, key);
                    }
                });
            }
        }, rule, data);
    }
}
