package io.horizon.util;

/**
 * @author lang : 2023/4/28
 */
class _Element extends _Color {
    /**
     * 针对数组执行拷贝，支持泛型数组的拷贝动作
     *
     * @param array   数组
     * @param element 元素
     * @param <T>     泛型
     *
     * @return 拷贝后的数组
     */
    public static <T> T[] elementAdd(final T[] array, final T element) {
        return HArray.elementAdd(array, element);
    }
}
