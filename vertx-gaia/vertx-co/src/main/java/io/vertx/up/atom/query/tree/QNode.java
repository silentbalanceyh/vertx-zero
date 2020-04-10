package io.vertx.up.atom.query.tree;

/*
 * The top level query node
 */
public interface QNode {

    /*
     * Operator
     * 1. If current is leaf, exclude `AND` and `OR`
     * 2. If current is branch, only contains `AND` or `OR`
     */
    QOp op();

    /*
     * Current tree level data here
     */
    QNode level(Integer level);

    /*
     * Whether current node is leaf node
     */
    boolean isLeaf();
}
