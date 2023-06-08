package io.horizon.uca.qr.syntax;

import io.horizon.eon.VString;
import io.horizon.exception.web._400QOpUnknownException;
import io.horizon.exception.web._500QQueryMetaNullException;
import io.horizon.fn.HFn;
import io.horizon.util.HUt;
import io.vertx.core.json.JsonArray;

import java.io.Serializable;
import java.util.Objects;

/**
 * ## Field
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public class IrItem implements Serializable {
    /**
     * The field name
     */
    private final String field;
    /**
     * The field operator
     */
    private final String op;
    /**
     * The key of condition
     */
    private final String qrKey;
    /**
     * The value of current kv.
     */
    private Object value;

    public IrItem(final String fieldExpr) {
        /* First checking for `fieldExpr` literal */
        HFn.out(HUt.isNil(fieldExpr), _500QQueryMetaNullException.class, this.getClass());
        this.qrKey = fieldExpr;
        if (fieldExpr.contains(VString.COMMA)) {
            this.field = fieldExpr.split(VString.COMMA)[0];
            this.op = fieldExpr.split(VString.COMMA)[1];
            /* op un support of query engine */
            HFn.out(!Ir.Op.VALUES.contains(this.op), _400QOpUnknownException.class, this.getClass(), this.op);
        } else {
            this.field = fieldExpr;
            this.op = Ir.Op.EQ;
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
     * @return {@link IrItem}
     */
    public IrItem value(final Object value) {
        this.value = value;
        return this;
    }

    public Object value() {
        return this.value;
    }

    public boolean valueEq(final IrItem target) {
        final Object value = this.value;
        final Object valueTarget = target.value;
        if (Objects.isNull(value) || Objects.isNull(valueTarget)) {
            // All null
            return value == valueTarget;
        } else {
            return value.equals(valueTarget);
        }
    }

    public IrItem add(final JsonArray value, final boolean isAnd) {
        if (this.value instanceof JsonArray) {
            this.value = IrDo.combine(value, this.value, isAnd);
        }
        return this;
    }
}
