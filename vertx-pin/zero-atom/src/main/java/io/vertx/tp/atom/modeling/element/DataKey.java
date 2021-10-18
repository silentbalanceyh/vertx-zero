package io.vertx.tp.atom.modeling.element;

import io.vertx.tp.atom.cv.em.IdMode;
import io.vertx.up.fn.Fn;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 一个模型中的唯一标识，根据主键属性计算出来的内容
 */
public class DataKey implements Serializable {
    /* 主键专用管理 */
    private static final ConcurrentMap<String, DataKey> KEYS = new ConcurrentHashMap<>();
    private final transient String unique;
    private transient IdMode mode = IdMode.DIRECT;    // 模式

    /* 主键字段：table name -> 属性矩阵，可支持多张表 */
    private ConcurrentMap<String, DataMatrix> matrix = new ConcurrentHashMap<>();

    private DataKey(final String unique) {
        this.unique = unique;
    }

    public static DataKey create(final String unique) {
        return Fn.pool(KEYS, unique, () -> new DataKey(unique));
    }

    public String getUnique() {
        return this.unique;
    }

    public ConcurrentMap<String, DataMatrix> getMatrix() {
        return this.matrix;
    }

    public void setMatrix(final ConcurrentMap<String, DataMatrix> matrix) {
        this.matrix = matrix;
    }

    public IdMode getMode() {
        return this.mode;
    }

    public void setMode(final IdMode mode) {
        this.mode = mode;
    }

    DataKey cloneKey() {
        final DataKey key = new DataKey(this.unique);
        key.setMode(this.mode);
        return key;
    }
}
