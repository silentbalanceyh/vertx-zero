package io.vertx.up.eon.em;

public enum MappingMode {
    BEFORE,  // Before execution
    AFTER,   // After execution
    AROUND,  // Around execution
    NONE,    // Disable dual mapping execution
}
