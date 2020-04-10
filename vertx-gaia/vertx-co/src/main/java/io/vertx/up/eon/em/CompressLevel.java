package io.vertx.up.eon.em;

public enum CompressLevel {

    /**
     * Compression level for no compression.
     */
    NO_COMPRESSION(0),

    /**
     * Compression level for fastest compression.
     */
    BEST_SPEED(1),

    /**
     * Compression level for best compression.
     */
    BEST_COMPRESSION(9),

    /**
     * Default compression level.
     */
    DEFAULT_COMPRESSION(-1);

    private final int level;

    CompressLevel(final int level) {
        this.level = level;
    }

    public int getLevel() {
        return this.level;
    }
}
