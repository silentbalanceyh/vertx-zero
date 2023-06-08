package io.horizon.eon.em;

/**
 * @author lang : 2023-06-03
 */
public final class EmDict {
    private EmDict() {
    }

    public enum Type {
        CATEGORY, // The category data ( tree ) definition,
        TABULAR,  // The tabular data ( list ) definition,
        ASSIST,   // The adminicle data ( dynamic ) came from definition
        DAO,      // The standard Dao Dict for capture
        NONE,     // None for source define
    }
}
