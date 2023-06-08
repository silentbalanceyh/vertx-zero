package io.vertx.up.atom.element;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

/**
 * ## 「Pojo」Uniform Data Object Container
 *
 * ### 1. Intro
 *
 * Uniform for batch / single calculation
 * 1) single - {@link io.vertx.core.json.JsonObject}
 * 2) batch - {@link io.vertx.core.json.JsonArray}
 *
 * Difference situation will use this one, this object contains Ambiguity here
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class JAmb {
    /**
     * The data reference of {@link io.vertx.core.json.JsonObject} or {@link io.vertx.core.json.JsonArray}
     */
    private Object data;
    /**
     * The flat to identify current object to check whether it's {@link io.vertx.core.json.JsonObject}
     */
    private Boolean single;

    /**
     * @return Object data reference
     */
    public Object data() {
        return this.data;
    }

    /**
     * @param <T> The actual data type of {@link java.lang.Object} instance member `data`.
     *
     * @return Generic data reference that will be convert to T
     */
    @SuppressWarnings("unchecked")
    public <T> T dataT() {
        return (T) this.data;
    }

    /**
     * 「Fluent」Set the data of {@link io.vertx.core.json.JsonObject}.
     *
     * @param data {@link io.vertx.core.json.JsonObject}
     *
     * @return Reference of this
     */
    @Fluent
    public JAmb data(final JsonObject data) {
        this.data = data;
        this.single = Boolean.TRUE;
        return this;
    }

    /**
     * 「Fluent」Set the data of {@link io.vertx.core.json.JsonArray}.
     *
     * @param data {@link io.vertx.core.json.JsonArray}
     *
     * @return Reference of this
     */
    @Fluent
    public JAmb data(final JsonArray data) {
        this.data = data;
        this.single = Boolean.FALSE;
        return this;
    }

    /**
     * 「Fluent」Add new element in {@link io.vertx.core.json.JsonArray}.
     *
     * @param item {@link io.vertx.core.json.JsonObject}
     *
     * @return Reference of this
     */
    @Fluent
    public JAmb add(final JsonObject item) {
        if (!this.single) {
            ((JsonArray) this.data).add(item);
        }
        return this;
    }

    /**
     * Here are three values for `this.single`.
     *
     * - TRUE: The data is {@link io.vertx.core.json.JsonObject} type.
     * - FALSE: The data is {@link io.vertx.core.json.JsonArray} type.
     * - null: Invalid of current, the data part is null or invalid format.
     *
     * @return {@link java.lang.Boolean} The flag of current object.
     */
    public Boolean isSingle() {
        return this.single;
    }

    /**
     * @return {@link java.lang.Boolean} The flag of current object.
     */
    public Boolean isValid() {
        return Objects.nonNull(this.data);
    }
}
