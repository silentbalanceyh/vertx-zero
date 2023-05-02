package io.horizon.spi.typed;

/**
 * 比较扩展专用方法
 * 1. 可直接调用比较扩展
 * 2. 调用 Vs 中的方法会默认触发比较扩展
 * 返回值为true表示两个传入数据是相同的（等价）
 *
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
