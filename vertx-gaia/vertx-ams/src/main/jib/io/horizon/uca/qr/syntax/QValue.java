package io.horizon.uca.qr.syntax;

import io.horizon.util.HUt;

public class QValue implements QLeaf {

    private final QOp op;
    private final String field;

    private final Object value;
    private Integer level = 0;

    private QValue(final String field, final QOp op, final Object value) {
        this.field = field;
        this.op = null == op ? QOp.EQ : op;
        this.value = value;
    }

    static QValue create(final String field,
                         final QOp op,
                         final Object value) {
        return new QValue(field, op, value);
    }

    @Override
    public boolean isLeaf() {
        return true;
    }

    @Override
    public String field() {
        return this.field;
    }

    @Override
    public QOp op() {
        return this.op;
    }

    @Override
    public QNode level(final Integer level) {
        this.level = level;
        return this;
    }

    @Override
    public Object value() {
        return this.value;
    }

    @Override
    public String toString() {
        final StringBuilder source = new StringBuilder();
        HUt.itRepeat(this.level, () -> source.append("\t"));
        source.append("Leaf").append(",");
        source.append("( ").append(this.field).append(" , ")
            .append(this.op).append(" , ")
            .append(this.value).append(" ) ");
        return source.toString();
    }
}
