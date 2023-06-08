package io.macrocosm.specification.program;

import io.horizon.specification.unit.HLibrary;

/**
 * 「Bundle」Bundle
 * <hr/>
 * 针对OSGI的打包专用 Bundle 部分，可作为插件底层，而其他所有的内容都是从 Bundle 中直接引用而来。
 *
 * @author lang : 2023-05-21
 */
public interface HBundle {
    /**
     * 库信息
     *
     * @return {@link HLibrary}
     */
    HLibrary library();

    /**
     * 资源目录信息
     *
     * @return {@link String}
     */
    String resource();
}
