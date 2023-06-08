package io.horizon.eon.em;

/**
 * @author lang : 2023-05-31
 */
public final class EmType {
    private EmType() {
    }

    public enum Json {
        STRING,
        INTEGER,
        DECIMAL,
        BOOLEAN,
        DATE,
        JOBJECT,
        JARRAY,
        CLASS
    }

    public enum Yaml {
        OBJECT,
        ARRAY
    }

    /**
     * @author lang : 2023-05-21
     */
    public enum Store {
        BLOCK,      // 块存储
        RDBMS,      // 数据库存储
        FS,         // 文件存储
    }
}
