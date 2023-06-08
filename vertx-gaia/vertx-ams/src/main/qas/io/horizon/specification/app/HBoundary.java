package io.horizon.specification.app;

/**
 * 「边界」
 * <hr/>
 * 专用边界方法，限定系统的核心范围，实现最终的边界控制，边界控制后期可能直接对接安全
 * 模式下的控制
 * <pre><code>
 *     1. License 限制
 *     2. 凭证检查
 *     3. 安全检查
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface HBoundary<T> {
    /**
     * 返回对应类型的边界
     *
     * @return {@link T}
     */
    T realm();
}
