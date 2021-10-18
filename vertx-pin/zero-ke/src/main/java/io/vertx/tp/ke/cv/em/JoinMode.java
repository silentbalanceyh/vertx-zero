package io.vertx.tp.ke.cv.em;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum JoinMode {
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
