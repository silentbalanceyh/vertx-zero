package io.vertx.up.exception;

/**
 * Top exception for this project
 */
public abstract class ZeroException extends Exception {

    private final String message;

    protected ZeroException(final String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }

    public abstract int getCode();
}
