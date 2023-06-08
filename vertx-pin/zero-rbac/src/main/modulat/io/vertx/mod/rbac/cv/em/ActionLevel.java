package io.vertx.mod.rbac.cv.em;

/*
 * Action Level for ACL
 */
public enum ActionLevel {
    // ---- Read Action
    /* 0 - READONLY:  The min level of all action */
    READONLY,
    /* 1 - READ:  Single record read */
    READ,
    /* 2 - READ_INTEGRATION:  Read when integration */
    READ_INTEGRATION,
    /* 3 - READ_EXPORT: Export some data from system */
    READ_EXPORT,

    // ----- Create Action
    /* 4 - ADD:  Create new record, batch create records */
    ADD,
    /* 5 - ADD_IMPORT: Import data into system */
    ADD_IMPORT,
    /* 6 - ADD_INTEGRATION */
    ADD_INTEGRATION,
    /* 7 - ADD_META */
    ADD_META,

    // ----- Update Action
    /* 8 - EDIT_APPROVE: Approved, Confirm */
    EDIT_APPROVE,
    /* 9 - EDIT:  Update with all update level */
    EDIT,
    /* 10 - EDIT_INTEGRATION: */
    EDIT_INTEGRATION,
    /* 11 - EDIT_META: Modify configuration, metadata data include columns */
    EDIT_META,

    // ----- Delete Action
    /* 12 - DELETE:  Delete, BatchDelete */
    DELETE,
    /* 13 - DELETE_PURGE: Purge */
    DELETE_PURGE,
    /* 14 - DELETE_META: */
    DELETE_META,
    /* 15 - FULL */
    FULL
}
