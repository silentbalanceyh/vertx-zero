package io.zero.spec.function;

import io.zero.fn.error.EActuator;

/**
 * @author lang : 2023/4/24
 */
@FunctionalInterface
public interface ErrorActuator extends EActuator<Throwable> {
}
