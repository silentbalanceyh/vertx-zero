package io.vertx.mod.jet.uca.param;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.mod.jet.JetThanatos;
import io.vertx.up.commune.Envelop;
import io.vertx.up.eon.KWeb;
import io.vertx.up.util.Ut;

import java.util.Iterator;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/*
 * Internal validation for body data here
 * 1. paramContained
 * 2. paramRequired
 */
class Verifier {

    static Envelop validateContained(final Class<?> clazz, final JsonObject data, final Set<String> paramContained) {
        Envelop envelop = null;
        if (null != paramContained && !paramContained.isEmpty()) {
            final JetThanatos verifier = JetThanatos.create(clazz);
            final Object value = data.getValue(KWeb.ARGS.PARAM_BODY);
            if (null == value) {
                /*
                 * 400, bad request, Contained existing rule value, null value should be throw out
                 *  */
                envelop = verifier.to400RequiredParam(paramContained.iterator().next());
            } else {
                if (Ut.isJObject(value)) {
                    /*
                     * Original value is JsonObject type here
                     */
                    envelop = execute((JsonObject) value, paramContained, verifier::to400RequiredParam);
                } else if (Ut.isJArray(value)) {
                    /*
                     * Original value is JsonArray type
                     */
                    final JsonArray prepared = ((JsonArray) value);
                    final int size = prepared.size();
                    for (int idx = 0; idx < size; idx++) {
                        final Object element = prepared.getValue(idx);
                        if (element instanceof JsonObject) {
                            /*
                             * Continue validation is valid for element that belong to JsonObject only
                             */
                            envelop = execute((JsonObject) element, paramContained, verifier::to400RequiredParam);
                            if (Objects.nonNull(envelop)) {
                                break;
                            }
                        }
                    }
                } else {
                    /*
                     * Because data could not be converted to valid structure here
                     */
                    envelop = verifier.to400RequiredParam(paramContained.iterator().next());
                }
            }
        }
        return envelop;
    }

    static Envelop validateRequired(final Class<?> clazz, final JsonObject data, final Set<String> paramRequired) {
        Envelop envelop = null;
        if (null != paramRequired && !paramRequired.isEmpty()) {
            final JetThanatos verifier = JetThanatos.create(clazz);
            envelop = execute(data, paramRequired, verifier::to400RequiredParam);
        }
        return envelop;
    }

    @SuppressWarnings("all")
    private static Envelop execute(final JsonObject data, final Set<String> requires, final Function<String, Envelop> errorSupplier) {
        Envelop result = null;
        final Iterator<String> it = requires.iterator();
        while (it.hasNext()) {
            final String required = it.next();
            result = execute(data, required, errorSupplier);
            if (null != result) {
                // Exception found
                break;
            }
        }
        return result;
    }

    /*
     * Validate whether data contains `required` attribute
     * 1. If contains return null
     * 2. Otherwise
     * - 2.1. If not contains, return 400
     * - 2.2. If string, check null/empty/blank, if so return 400
     */
    private static Envelop execute(final JsonObject data, final String required, final Function<String, Envelop> errorSupplier) {
        // 「Success」
        if (Ut.isNil(required)) {
            return null;
        }
        // 「Failure」
        if (null == data) {
            return errorSupplier.apply(required);
        }
        final Object value = data.getValue(required);
        // 「Failure」
        if (null == value) {
            return errorSupplier.apply(required);
        }
        // 「Failure」
        if (Ut.isNil(value.toString())) {
            return errorSupplier.apply(required);
        }
        // 「Success」
        return null;
    }
}
