package io.vertx.up.atom.query.engine;

import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface QrDo {

    /**
     * Add new condition to LINEAR component.
     *
     * The parameters are following format:
     *
     * 1. fieldExpr is format of `field,op`, it contains two parts.
     * 2. fieldExpr is format of `field`, the default op is =.
     *
     * @param fieldExpr {@link java.lang.String}
     * @param value     {@link java.lang.Object}
     *
     * @return {@link QrDo}
     */
    QrDo add(String fieldExpr, Object value);

    /**
     * Serialized current instance to Json
     *
     * @return {@link JsonObject}
     */
    JsonObject toJson();

    /**
     * Check current QTree to see whether it's valid.
     *
     * @return {@link java.lang.Boolean};
     */
    boolean valid();
}
