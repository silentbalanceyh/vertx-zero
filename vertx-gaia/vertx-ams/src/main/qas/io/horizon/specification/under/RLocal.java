package io.horizon.specification.under;

import io.horizon.annotations.reference.One2One;
import io.horizon.specification.unit.HTrash;

/**
 * 「本地库」Local Repository
 * 本地代码库直接从库中直接继承，而限制条件如：
 * <pre><code>
 *     1. 本地库不可以发布，即不可以执行类似 HCabe 应用区域的部分
 *     2. 本地库会包含本地回收站引用，可以将删除内容扔到回收站程序中
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface RLocal extends HRepo {
    /**
     * 返回回收站引用
     *
     * @return {@link HTrash}
     */
    @One2One
    HTrash trash();
}
