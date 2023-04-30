package io.horizon.exception;

/**
 * 顶层异常
 */
public abstract class ProgramException extends Exception {

    private final String message;

    protected ProgramException(final String message) {
        super(message);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
    
    public abstract int getCode();
}
