package io.zero.fn.error;

/**
 * @author lang : 2023/4/24
 */
public interface EFunction<I, O, E extends Throwable> {
    O apply(I i) throws E;
}
