package io.vertx.up.commune.compare;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface VsExtension {

    /**
     * @param valueOld {@link Object} input old
     * @param valueNew {@link Object} input new
     * @param type     {@link Class} input type
     *
     * @return {@link Boolean} true when Same.
     */
    boolean is(Object valueOld, Object valueNew, Class<?> type);
}
