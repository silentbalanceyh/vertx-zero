package io.horizon.function;

/**
 * Actuator function interface, this interface could throw out
 * java.lang.Exception but return void without any parameters.
 */
@FunctionalInterface
public interface ExceptionActuator extends EActuator<Exception> {
}
