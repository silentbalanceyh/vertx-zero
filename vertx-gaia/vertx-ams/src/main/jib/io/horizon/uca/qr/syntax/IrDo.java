package io.horizon.uca.qr.syntax;

import io.horizon.util.HUt;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.List;
import java.util.function.BiConsumer;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface IrDo {

    /**
     * Create new QrDo reference.
     *
     * @param data {@link io.vertx.core.json.JsonObject} Input json object.
     *
     * @return {@link IrDo}
     */
    static IrDo create(final JsonObject data) {
        return new IrAnalyzer(data);
    }

    /**
     * Check whether json object is complex
     * 1. When any one value is `JsonObject`, it's true.
     * 2. otherwise the result is false.
     *
     * @param source {@link io.vertx.core.json.JsonObject} input json
     *
     * @return {@link java.lang.Boolean}
     */
    static boolean isComplex(final JsonObject source) {
        return IrAnalyzer.isComplex(source);
    }

    @SuppressWarnings("all")
    static JsonArray combine(final Object newItem, final Object oldItem, final Boolean isAnd) {
        // The operator will not be changed.
        final JsonArray newSet = (JsonArray) newItem;
        final JsonArray oldSet = (JsonArray) oldItem;
        final List result = isAnd ?
            // Two collection and
            HUt.elementIntersect(newSet.getList(), oldSet.getList()) :
            // Two collection union
            HUt.elementUnion(newSet.getList(), oldSet.getList());
        return new JsonArray(result);
    }

    /**
     * Save new condition to LINEAR component.
     * The parameters are following format:
     * 1. fieldExpr is format of `field,op`, it contains two parts.
     * 2. fieldExpr is format of `field`, the default op is =.
     *
     * @param fieldExpr {@link java.lang.String}
     * @param value     {@link java.lang.Object}
     *
     * @return {@link IrDo}
     */
    IrDo save(String fieldExpr, Object value);

    /**
     * @param fieldExpr {@link java.lang.String} Removed fieldExpr
     * @param fully     {@link java.lang.Boolean} Removed fully or ?
     *
     * @return {@link IrDo}
     */
    IrDo remove(String fieldExpr, boolean fully);

    /**
     * @param fieldExpr {@link java.lang.String}
     * @param value     {@link java.lang.Object}
     *
     * @return {@link IrDo}
     */
    IrDo update(String fieldExpr, Object value);

    /**
     * @param field    {@link java.lang.String} The field name
     * @param consumer {@link java.util.function.BiConsumer} The qr item consumed
     */
    void match(String field, BiConsumer<IrItem, JsonObject> consumer);

    /**
     * Serialized current instance to Json
     *
     * @return {@link JsonObject}
     */
    JsonObject toJson();
}
