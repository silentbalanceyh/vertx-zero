package io.modello.specification.element;

import io.modello.eon.em.KeyType;

import java.util.Set;

/**
 * 键集合，用于描述键信息，键主要包含如下几种格式：
 * <pre><code>
 * 1. 主键：PrimaryKey
 * 2. 外键：ForeignKey
 * 3. 唯一键：UniqueKey
 * </code></pre>
 *
 * @author lang : 2023-05-08
 */
public interface HKey {
    /**
     * @return 键类型
     */
    KeyType type();

    /**
     * 返回单字段键相关信息，通常此键由一个字段构成
     *
     * @return 单字段键的键名
     */
    String key();

    /**
     * 设置单字段键相关信息
     *
     * @param key 单字段键
     *
     * @return 单字段键
     */
    HKey key(String key);

    /**
     * 设置多字段键相关信息
     *
     * @param keys 多字段键
     *
     * @return 多字段键
     */
    HKey key(Set<String> keys);

    /**
     * 返回多字段键相关信息，通常此键由多个字段构成
     *
     * @return 多字段键
     */
    Set<String> keys();
}
