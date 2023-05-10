package io.vertx.up.atom.query.engine;

public interface QLeaf extends QNode {
    /*
     * Read current node field information
     * 1. If current node is leaf, field will be attribute/column name
     * 2. If current node is not leaf, field will be expr such as `$0` or `$1`
     */
    String field();

    /*
     * Get the value of current node
     */
    Object value();
}
