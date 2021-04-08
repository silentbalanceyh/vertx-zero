package io.vertx.up.commune.element;

import io.vertx.codegen.annotations.Fluent;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 * Uniform for batch / single calculation
 * 1) single - JsonObject
 * 2) batch - JsonArray
 *
 * Difference situation will use this one, this object contains Ambiguity here
 */
public class JAmb {
    private transient Object data;
    private transient Boolean single;

    public Object data() {
        return this.data;
    }

    @SuppressWarnings("unchecked")
    public <T> T dataT() {
        return (T) this.data;
    }

    @Fluent
    public JAmb data(final JsonObject data) {
        this.data = data;
        this.single = Boolean.TRUE;
        return this;
    }

    @Fluent
    public JAmb data(final JsonArray data) {
        this.data = data;
        this.single = Boolean.FALSE;
        return this;
    }

    /*
     * null means invalid
     */
    public Boolean isSingle() {
        return this.single;
    }
}
