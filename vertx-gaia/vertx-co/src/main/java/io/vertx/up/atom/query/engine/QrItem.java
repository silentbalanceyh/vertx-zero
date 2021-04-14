package io.vertx.up.atom.query.engine;

import io.vertx.up.atom.Kv;
import io.vertx.up.eon.Strings;
import io.vertx.up.exception.web._400OpUnsupportException;
import io.vertx.up.exception.web._500QueryMetaNullException;
import io.vertx.up.fn.Fn;
import io.vertx.up.util.Ut;

import java.io.Serializable;

/**
 * ## Field
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class QrItem implements Serializable {
    /**
     * The field name
     */
    private transient final String field;
    /**
     * The field operator
     */
    private transient final String op;
    /**
     * The key of condition
     */
    private transient final String qrKey;
    /**
     * The value of current kv.
     */
    private transient Object value;

    private transient Kv<String, Kv<String, Object>> item;

    public QrItem(final String fieldExpr) {
        /* First checking for `fieldExpr` literal */
        Fn.out(Ut.isNil(fieldExpr), _500QueryMetaNullException.class, this.getClass());
        this.qrKey = fieldExpr;
        if (fieldExpr.contains(Strings.COMMA)) {
            this.field = fieldExpr.split(Strings.COMMA)[0];
            this.op = fieldExpr.split(Strings.COMMA)[1];
            /* op un support of query engine */
            Fn.out(!Qr.Op.VALUES.contains(this.op), _400OpUnsupportException.class, this.getClass(), this.op);
        } else {
            this.field = fieldExpr;
            this.op = Qr.Op.EQ;
        }
    }

    public String field() {
        return this.field;
    }

    public String op() {
        return this.op;
    }

    public String qrKey() {
        return this.qrKey;
    }

    /**
     * Bind the value in current Qr item.
     *
     * @param value {@link java.lang.Object} value input here.
     *
     * @return {@link QrItem}
     */
    public QrItem value(final Object value) {
        this.value = value;
        final Kv<String, Object> condition = Kv.create(this.op, value);
        this.item = Kv.create(this.field, condition);
        return this;
    }

    public Object value() {
        return this.value;
    }

    public Kv<String, Kv<String, Object>> item() {
        return this.item;
    }
}
