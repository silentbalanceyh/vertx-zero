package io.vertx.up.uca.marshal;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Transformer<I, T> {
    T transform(I input);
}
