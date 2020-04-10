package io.vertx.up.atom.query.tree;

import io.vertx.up.atom.query.Inquiry;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/*
 * Operator
 */
public enum QOp {
    /* Connect to Query Engine */

    // Branch
    AND(Inquiry.Connector.AND.name()),
    OR(Inquiry.Connector.OR.name()),

    // Leaf
    EQ(Inquiry.Op.EQ),              // ==
    NEQ(Inquiry.Op.NEQ),            // <>
    LT(Inquiry.Op.LT),              // <
    LE(Inquiry.Op.LE),              // <=
    GT(Inquiry.Op.GT),              // >
    GE(Inquiry.Op.GE),              // >=
    NULL(Inquiry.Op.NULL),          // n
    NOT_NULL(Inquiry.Op.NOT_NULL),  // !n
    TRUE(Inquiry.Op.TRUE),          // t
    FALSE(Inquiry.Op.FALSE),        // f
    IN(Inquiry.Op.IN),              // i
    NOT_IN(Inquiry.Op.NOT_IN),      // !i
    START(Inquiry.Op.START),        // s
    END(Inquiry.Op.END),            // e
    CONTAIN(Inquiry.Op.CONTAIN);    // c

    private static final ConcurrentMap<String, QOp> MAP = new ConcurrentHashMap<String, QOp>() {
        {
            this.put(Inquiry.Op.EQ, QOp.EQ);
            this.put(Inquiry.Op.NEQ, QOp.NEQ);
            this.put(Inquiry.Op.LT, QOp.LT);
            this.put(Inquiry.Op.LE, QOp.LE);
            this.put(Inquiry.Op.GT, QOp.GT);
            this.put(Inquiry.Op.GE, QOp.GE);
            this.put(Inquiry.Op.NULL, QOp.NULL);
            this.put(Inquiry.Op.NOT_NULL, QOp.NOT_NULL);
            this.put(Inquiry.Op.TRUE, QOp.TRUE);
            this.put(Inquiry.Op.FALSE, QOp.FALSE);
            this.put(Inquiry.Op.IN, QOp.IN);
            this.put(Inquiry.Op.NOT_IN, QOp.NOT_IN);
            this.put(Inquiry.Op.START, QOp.START);
            this.put(Inquiry.Op.END, QOp.END);
            this.put(Inquiry.Op.CONTAIN, QOp.CONTAIN);
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
