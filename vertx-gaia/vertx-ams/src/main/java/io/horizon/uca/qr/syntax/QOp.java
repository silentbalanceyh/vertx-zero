package io.horizon.uca.qr.syntax;

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
    AND(Ir.Connector.AND.name()),
    OR(Ir.Connector.OR.name()),

    // Leaf
    EQ(Ir.Op.EQ),              // ==
    NEQ(Ir.Op.NEQ),            // <>
    LT(Ir.Op.LT),              // <
    LE(Ir.Op.LE),              // <=
    GT(Ir.Op.GT),              // >
    GE(Ir.Op.GE),              // >=
    NULL(Ir.Op.NULL),          // n
    NOT_NULL(Ir.Op.NOT_NULL),  // !n
    TRUE(Ir.Op.TRUE),          // t
    FALSE(Ir.Op.FALSE),        // f
    IN(Ir.Op.IN),              // i
    NOT_IN(Ir.Op.NOT_IN),      // !i
    START(Ir.Op.START),        // s
    END(Ir.Op.END),            // e
    CONTAIN(Ir.Op.CONTAIN);    // c

    private static final ConcurrentMap<String, QOp> MAP = new ConcurrentHashMap<String, QOp>() {
        {
            this.put(Ir.Op.EQ, QOp.EQ);
            this.put(Ir.Op.NEQ, QOp.NEQ);
            this.put(Ir.Op.LT, QOp.LT);
            this.put(Ir.Op.LE, QOp.LE);
            this.put(Ir.Op.GT, QOp.GT);
            this.put(Ir.Op.GE, QOp.GE);
            this.put(Ir.Op.NULL, QOp.NULL);
            this.put(Ir.Op.NOT_NULL, QOp.NOT_NULL);
            this.put(Ir.Op.TRUE, QOp.TRUE);
            this.put(Ir.Op.FALSE, QOp.FALSE);
            this.put(Ir.Op.IN, QOp.IN);
            this.put(Ir.Op.NOT_IN, QOp.NOT_IN);
            this.put(Ir.Op.START, QOp.START);
            this.put(Ir.Op.END, QOp.END);
            this.put(Ir.Op.CONTAIN, QOp.CONTAIN);
        }
    };
    private final String value;

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
