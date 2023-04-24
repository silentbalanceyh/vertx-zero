package io.zero.fn.error;

/**
 * @author lang : 2023/4/24
 */
public interface EBiConsumer<L, R, E extends Throwable> {

    void accept(L input, R second) throws E;
}
