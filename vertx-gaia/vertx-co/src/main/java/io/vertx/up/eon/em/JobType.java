package io.vertx.up.eon.em;

public enum JobType {
    FIXED,      // Run at timestamp
    PLAN,       // Scheduled
    ONCE,       // Run once
    CONTAINER,  // Task container

    /*
     * For business extension, here provide four values:
     * 1. Month
     * 2. Week
     * 3. Quarter
     * 4. Year
     */
    MONTH,
    WEEK,
    QUARTER,
    YEAR,
}
