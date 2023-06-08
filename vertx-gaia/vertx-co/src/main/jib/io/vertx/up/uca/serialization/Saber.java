package io.vertx.up.uca.serialization;

/**
 * For field serialization
 */
public interface Saber {
    /**
     * String to object
     *
     * @param type
     * @param literal
     *
     * @return
     */
    Object from(Class<?> type, String literal);

    /**
     * T to object
     *
     * @param input
     * @param <T>
     *
     * @return
     */
    <T> Object from(T input);
}
