package io.modello.specification.atom;

/**
 * 「链接」
 * <hr/>
 * 模型到实体的链接
 * <pre><code>
 *     1. {@link HModel}
 *     2. {@link HEntity}
 * </code></pre>
 *
 * @author lang : 2023-05-22
 */
public interface HJoin {
    /**
     * 对应 {@link HModel} 的 identifier
     *
     * @return {@link String}
     */
    String model();

    /**
     * 对应 {@link HModel} 的 key
     *
     * @return {@link String}
     */
    String modelKey();

    /**
     * 对应 {@link HEntity} 的 identifier
     *
     * @return {@link String}
     */
    String entity();

    /**
     * 对应 {@link HEntity} 的 key
     *
     * @return {@link String}
     */
    String entityKey();

    /**
     * 优先级，决定左连接还是右链接
     *
     * @return {@link Integer}
     */
    Integer priority();

    /**
     * 名空间
     *
     * @return {@link String}
     */
    String namespace();
}
