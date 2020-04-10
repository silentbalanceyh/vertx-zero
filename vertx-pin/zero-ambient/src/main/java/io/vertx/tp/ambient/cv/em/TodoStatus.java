package io.vertx.tp.ambient.cv.em;

public enum TodoStatus {
    PENDING,    // Wait for execute
    FINISHED,   // Finished by user
    REJECTED,   // Reject by user
    CANCELED,   // Cancelled by user
}
