package io.horizon.util;

import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import org.apache.commons.jexl3.*;

import java.util.Objects;

/**
 * @author lang : 2023/4/28
 */
class HExpression {
    private static final JexlEngine EXPR = new JexlBuilder()
        .cache(4096).silent(false).create();

    /*
     * Input Data Structure
     * {
     *     "field1": "expr1",
     *     "field2": "expr2"
     * }
     *
     * 1. If expr contains ` character, it will be replaced by parsed result.
     * 2. If expr does not contain ` character, it will be kept.
     *
     * Then the method will create new parts of normalized to avoid modify input exprObject
     */
    static JsonObject expression(final JsonObject exprObject, final JsonObject params) {
        // Iterator On Json Object
        final JsonObject parsed = new JsonObject();
        if (HUt.isNotNil(exprObject)) {
            exprObject.fieldNames().forEach(k -> {
                final Object value = exprObject.getValue(k);
                if (value instanceof String) {


                    // 「String」
                    final Object formatted = expressionWith((String) value, params);
                    parsed.put(k, formatted);
                } else if (value instanceof JsonObject) {


                    // 「JsonObject」
                    final JsonObject formatted = expression((JsonObject) value, params);
                    parsed.put(k, formatted);
                } else if (value instanceof JsonArray) {


                    // 「JsonArray」
                    final JsonArray formatted = expression((JsonArray) value, params);
                    parsed.put(k, formatted);
                } else {
                    // 「Keep」Non-String Part include `null`
                    parsed.put(k, value);
                }
            });
        }
        return parsed;
    }

    static JsonArray expression(final JsonArray exprArray, final JsonObject params) {
        final JsonArray normalized = new JsonArray();
        if (HUt.isNotNil(exprArray)) {
            exprArray.forEach(valueElement -> {
                if (valueElement instanceof String) {

                    // Element = String
                    final Object formatted = expressionWith((String) valueElement, params);
                    normalized.add(formatted);
                } else if (valueElement instanceof JsonObject) {


                    // Element = JsonObject
                    final JsonObject formatted = expression((JsonObject) valueElement, params);
                    normalized.add(formatted);
                } else if (valueElement instanceof JsonArray) {


                    // Element = JsonArray
                    final JsonArray formatted = expression((JsonArray) valueElement, params);
                    normalized.add(formatted);
                } else {


                    // Element = Other
                    normalized.add(valueElement);
                }
            });
        }
        return normalized;
    }

    static Object expressionWith(final String valueExpr, final JsonObject params) {
        if (HUt.isNotNil(valueExpr)) {
            final Object valueResult;
            if (valueExpr.contains("`")) {
                // Actual Parsing
                valueResult = expressionT(valueExpr, params);
            } else {
                // 「Keep」Original String
                valueResult = valueExpr;
            }
            return valueResult;
        } else {
            // 「Keep」Empty String
            return valueExpr;
        }
    }

    @SuppressWarnings("unchecked")
    static <T> T expressionT(final String valueExpr, final JsonObject params) {
        try {
            /*
             * cache(4096), the default cache size is 4096.
             * silent(false): silent = false means the exception will be thrown.
             */
            final JexlExpression expression = EXPR.createExpression(valueExpr);
            // Parameter
            final JexlContext context = new MapContext();
            params.fieldNames().forEach(field -> {
                // Here the null should be valid
                final Object value = params.getValue(field);
                context.set(field, value);
            });
            // Ut.itJObject(params, (value, key) -> context.set(key, value));
            // Processed
            final Object result = expression.evaluate(context);
            if (Objects.nonNull(result)) {
                return (T) result;
            } else {
                return null;
            }
        } catch (final JexlException ex) {
            // ex.printStackTrace();    // For Debug
            ex.printStackTrace();
            return null;                // Get null
            // throw new JexlExpressionException(StringUtil.class, expr, ex);
        }
    }
}
