package io.vertx.tp.atom.modeling.config;

import io.vertx.core.json.JsonArray;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class AoBag implements Serializable {
    /** Model identifier for data bag */
    private transient String identifier;
    /** The data in current package */
    private transient JsonArray data = new JsonArray();
    /** The data size of current package **/
    private transient Integer size = 0;

    public String getIdentifier() {
        return this.identifier;
    }

    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    public JsonArray getData() {
        return this.data;
    }

    public void setData(final JsonArray data) {
        this.data = data;
    }

    public Integer getSize() {
        return this.size;
    }

    public void setSize(final Integer size) {
        this.size = size;
    }
}
