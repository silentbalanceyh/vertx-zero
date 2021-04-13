package io.vertx.up.atom.query.tree;

import io.vertx.up.atom.query.Qr;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## Query Operator
 *
 * The operator in where clause.
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum QOp {

    // Branch
    AND(Qr.Connector.AND.name()),
    OR(Qr.Connector.OR.name()),

    // Leaf
    EQ(Qr.Op.EQ),              // ==
    NEQ(Qr.Op.NEQ),            // <>
    LT(Qr.Op.LT),              // <
    LE(Qr.Op.LE),              // <=
    GT(Qr.Op.GT),              // >
    GE(Qr.Op.GE),              // >=
    NULL(Qr.Op.NULL),          // n
    NOT_NULL(Qr.Op.NOT_NULL),  // !n
    TRUE(Qr.Op.TRUE),          // t
    FALSE(Qr.Op.FALSE),        // f
    IN(Qr.Op.IN),              // i
    NOT_IN(Qr.Op.NOT_IN),      // !i
    START(Qr.Op.START),        // s
    END(Qr.Op.END),            // e
    CONTAIN(Qr.Op.CONTAIN);    // c

    private static final ConcurrentMap<String, QOp> MAP = new ConcurrentHashMap<String, QOp>() {
        {
            this.put(Qr.Op.EQ, QOp.EQ);
            this.put(Qr.Op.NEQ, QOp.NEQ);
            this.put(Qr.Op.LT, QOp.LT);
            this.put(Qr.Op.LE, QOp.LE);
            this.put(Qr.Op.GT, QOp.GT);
            this.put(Qr.Op.GE, QOp.GE);
            this.put(Qr.Op.NULL, QOp.NULL);
            this.put(Qr.Op.NOT_NULL, QOp.NOT_NULL);
            this.put(Qr.Op.TRUE, QOp.TRUE);
            this.put(Qr.Op.FALSE, QOp.FALSE);
            this.put(Qr.Op.IN, QOp.IN);
            this.put(Qr.Op.NOT_IN, QOp.NOT_IN);
            this.put(Qr.Op.START, QOp.START);
            this.put(Qr.Op.END, QOp.END);
            this.put(Qr.Op.CONTAIN, QOp.CONTAIN);
        }
    };
    private final transient String value;

    QOp(final String value) {
        this.value = value;
    }

    public static QOp toOp(final String opStr) {
        return MAP.getOrDefault(opStr, QOp.EQ);
    }

    public String value() {
        return this.value;
    }
}
