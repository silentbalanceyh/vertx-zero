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

    /**
     * 扩展异常代码
     *
     * @return 异常代码
     */
    public abstract int getCode();
}
