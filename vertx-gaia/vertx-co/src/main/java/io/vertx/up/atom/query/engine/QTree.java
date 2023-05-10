package io.vertx.up.atom.query.engine;

import io.horizon.eon.VString;
import io.vertx.core.json.JsonObject;
import io.vertx.up.atom.query.Criteria;
import io.vertx.up.util.Ut;

import java.util.Objects;

public class QTree {

    private final QNode current;

    private QTree(final JsonObject content) {
        this.current = this.initTier(content, 0);
    }

    public static QTree create(final Criteria criteria) {
        return new QTree(criteria.toJson());
    }

    public static QTree create(final JsonObject content) {
        return new QTree(content);
    }

    private QNode initTier(final String field, final Object value, final Integer level) {
        final QNode node;
        if (Ut.isJObject(value)) {
            /* Create new branch */
            node = this.initTier(((JsonObject) value), level);
        } else {
            /* Create new leaf */
            node = this.initValue(field, value).level(level);
        }
        return node;
    }

    public QNode getData() {
        return this.current;
    }

    public boolean hasValue() {
        if (this.current instanceof QTier) {
            return 0 < ((QTier) this.current).nodes().size();
        } else {
            return Objects.nonNull(this.current);
        }
    }

    private QValue initValue(final String field, final Object value) {
        if (field.contains(",")) {
            final String[] fieldOp = field.split(",");
            final String target = fieldOp[0].trim();
            final String opStr = fieldOp[1].trim();
            final QOp op = QOp.toOp(opStr);
            return QValue.create(target, op, value);
        } else {
            return QValue.create(field, QOp.EQ, value);
        }
    }

    private QNode initTier(final JsonObject content, final Integer level) {
        /* 1. Initialize node */
        final QNode root = this.init(content, level);

        /* 2. Then initialize children nodes */
        content.fieldNames().stream().filter(Ut::isNotNil).forEach(field -> {

            /* 3. Each */
            final QNode tier = this.initTier(field, content.getValue(field), level + 1);
            if (!root.isLeaf()) {
                ((QBranch) root).add(tier);
            }
        });
        return root;
    }

    private QNode init(final JsonObject content, final Integer level) {
        final QNode root;
        if (content.containsKey(VString.EMPTY)) {
            final Boolean isAnd = content.getBoolean(VString.EMPTY);
            root = QTier.create(isAnd ? QOp.AND : QOp.OR).level(level);
        } else {
            root = QTier.create(QOp.AND).level(level);
        }
        return root;
    }

    @Override
    public String toString() {
        return "\n" + this.current.toString();
    }
}
