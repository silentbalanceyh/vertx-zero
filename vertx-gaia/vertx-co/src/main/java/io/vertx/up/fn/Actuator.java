package io.vertx.up.fn;

/*
 * Be careful about this interface because it's only support
 * Sync mode, when the function existing async here, may be the reference data
 * could not be synced.
 */
@FunctionalInterface
public interface Actuator {

    void execute();
}
