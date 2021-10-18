package io.vertx.tp.modular.query;

import io.vertx.tp.atom.modeling.element.DataMatrix;
import io.vertx.tp.modular.jooq.internal.Jq;
import io.vertx.tp.plugin.jooq.condition.Clause;
import io.vertx.up.atom.query.tree.*;
import io.vertx.up.eon.Values;
import org.jooq.Condition;
import org.jooq.Field;

import java.util.*;
import java.util.concurrent.ConcurrentMap;


/*
 * 查询分析树解析器
 */
@SuppressWarnings("all")
class QVisitor {

    /*
     * 直接解析，不带表前缀
     */
    static Condition analyze(final QTree tree, final DataMatrix matrix) {
        final QNode node = tree.getData();
        final Set<DataMatrix> set = new HashSet<>();
        set.add(matrix);
        return analyze(node, set, null);
    }

    static Condition analyze(final QTree tree, final Set<DataMatrix> matrix,
                             final ConcurrentMap<String, String> fieldMap) {
        final QNode node = tree.getData();
        return analyze(node, matrix, fieldMap);
    }

    private static Condition analyze(final QNode node, final Set<DataMatrix> matrix,
                                     final ConcurrentMap<String, String> fieldMap) {
        /*
         * 看这个 node 是 哪种
         * 1. Branch：非子节点
         * 2. Value：子节点
         **/
        if (node instanceof QValue) {
            final QLeaf value = (QLeaf) node;
            return analyze(value, matrix, fieldMap);
        } else {
            final List<Condition> conditions = new ArrayList<>();
            final QBranch branch = (QBranch) node;
            branch.nodes().stream().map(each -> analyze(each, matrix, fieldMap))
                .filter(Objects::nonNull)
                .forEach(conditions::add);
            /* 拼条件 */
            return analyze(conditions, node.op());
        }
    }

    private static Condition analyze(final QLeaf leaf, final Set<DataMatrix> matrix,
                                     final ConcurrentMap<String, String> fieldMap) {
        final String field = leaf.field();
        final Field column = Jq.toField(field, matrix, fieldMap);
        if (Objects.isNull(column)) {
            /*
             * Fix: java.lang.NullPointerException
	                at cn.vertxup.tp.modular.query.QVisitor.analyze(QVisitor.java:63)
             */
            return null;
        } else {
            final Clause clause = Clause.get(column.getType());
            return clause.where(column, column.getName(),
                /* 特殊 op 处理 */
                leaf.op().value(), leaf.value());
        }
    }

    private static Condition analyze(final List<Condition> conditions, final QOp op) {
        /*
         * 防止数组越界
         */
        if (conditions.isEmpty()) {
            /*
             * Where ignore null;
             */
            return null;
        } else if (1 == conditions.size()) {
            return conditions.get(Values.IDX);
        } else {
            Condition condition = conditions.get(Values.IDX);
            for (int idx = 1; idx < conditions.size(); idx++) {
                final Condition target = conditions.get(idx);
                if (null != target) {
                    if (QOp.AND == op) {
                        condition = condition.and(target);
                    } else {
                        condition = condition.or(target);
                    }
                }
            }
            return condition;
        }
    }
}
