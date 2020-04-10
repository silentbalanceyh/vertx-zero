package io.vertx.up.eon.em;

public enum JobType {
    FIXED,      // Run at timestamp
    PLAN,       // Scheduled
    ONCE,       // Run once
    CONTAINER,  // Task container
}
