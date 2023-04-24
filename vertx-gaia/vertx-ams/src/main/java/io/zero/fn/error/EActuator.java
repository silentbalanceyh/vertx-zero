package io.zero.fn.error;

/**
 * @author lang : 2023/4/24
 */
public interface EActuator<E extends Throwable> {
    void execute() throws E;
}
