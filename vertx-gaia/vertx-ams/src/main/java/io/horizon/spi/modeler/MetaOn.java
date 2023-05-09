package io.horizon.spi.modeler;

import io.modello.specification.meta.HMetaAtom;
import io.modello.specification.meta.HMetaField;

/**
 * 归口统一的 Meta 元模型层创建专用工厂，替换实现类中的
 * <pre><code>
 *     1. MetaAtom.of()
 *     2. MetaField.of()
 * </code></pre>
 * 直接走接口层统一调用。
 *
 * @author lang : 2023-05-09
 */
public interface MetaOn {
    /**
     * 创建一个新的 HMetaAtom，替换原始的 HMetaAtom.of
     *
     * @return HMetaAtom
     */
    HMetaAtom atom();

    /**
     * 创建一个新的 HMetaField，替换原始的 HMetaField.of
     *
     * @param name  字段名称
     * @param alias 字段别名
     * @param type  字段类型
     *
     * @return HMetaField
     */
    HMetaField field(String name, String alias, Class<?> type);
}
