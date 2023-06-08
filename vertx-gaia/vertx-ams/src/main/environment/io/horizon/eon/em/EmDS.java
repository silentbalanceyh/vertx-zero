package io.horizon.eon.em;

/**
 * @author lang : 2023-05-31
 */
public final class EmDS {
    private EmDS() {
    }

    public enum Format {
        ENTITY, JSON
    }

    public enum Category {
        /* MySQL */
        MYSQL5,     // MySQL 5.x
        MYSQL8,     // MySQL 8.x
        MYSQL,      // Old Version
        /* Oracle */
        ORACLE,     // Old Version
        ORACLE11,   // 11
        ORACLE12,   // 12
    }

    public enum Stored {
        // Default Value ( Major Database configured by 'provider' )
        PRIMARY,
        // History Value ( configured by 'orbit' )
        HISTORY,
        // Workflow Value ( configured by 'workflow' )
        WORKFLOW,
        // Dynamic Value ( Build by 'DataPool' here )
        DYNAMIC,
        // Extension Value ( configured by '<key>' that provided )
        EXTENSION,
    }
}
