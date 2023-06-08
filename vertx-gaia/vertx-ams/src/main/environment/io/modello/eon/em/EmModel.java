package io.modello.eon.em;

/**
 * @author lang : 2023-05-31
 */
public class EmModel {
    public enum Type {
        DIRECT,       // 直接模型（和数据表1对1处理，默认为DIRECT）
        VIEW,         // 视图模型（后期可以和数据库中的视图绑定）
        JOINED,       // 连接模型，和视图模型类似，但不绑定数据库中视图，直接做连接（自然连接）
        READONLY,     // 只读模型
    }

    /**
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    public enum Join {
        /**
         * CRUD, `crud` configured, the system will look up target configuration by `CRUD` standard way.
         */
        CRUD,
        /**
         * DAO, `classDao` configured, directly to seek target configuration
         */
        DAO,
        /**
         * DEFINE, `classDefine` configured, Reserved in future.
         */
        DEFINE,
    }

    /*
     * New Structure for different interface ( Default value )
     * BY_ID / BY_KEY / BY_TENANT / BY_SIGMA
     */
    public enum By {
        BY_ID,              // APP_ID
        BY_KEY,             // APP_KEY
        BY_TENANT,          // TENANT_ID
        BY_SIGMA            // SIGMA
    }
}
