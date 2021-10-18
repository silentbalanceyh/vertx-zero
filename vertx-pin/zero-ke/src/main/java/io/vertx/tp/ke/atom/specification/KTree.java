package io.vertx.tp.ke.atom.specification;

import io.vertx.up.eon.KName;

import java.io.Serializable;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class KTree implements Serializable {
    private transient String in;
    private transient String out = KName.KEY;
    private transient String field = "parentId";

    public String getIn() {
        return this.in;
    }

    public void setIn(final String in) {
        this.in = in;
    }

    public String getOut() {
        return this.out;
    }

    public void setOut(final String out) {
        this.out = out;
    }

    public String getField() {
        return this.field;
    }

    public void setField(final String field) {
        this.field = field;
    }
}
