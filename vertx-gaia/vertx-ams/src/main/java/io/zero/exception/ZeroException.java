package io.zero.exception;

/**
 * 顶层异常
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
