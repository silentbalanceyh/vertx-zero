package io.horizon.specification.under;

import io.horizon.eon.em.EmLib;
import io.horizon.specification.unit.HEditor;
import io.horizon.specification.unit.HLibrary;

import java.util.Set;
import java.util.concurrent.ConcurrentMap;

/**
 * 「研发中心」Research 和 Development
 * <hr/>
 * 低代码研发中心，系统会在创建超级账号的低代码空间时直接将此处内容初始化，研发中心中会包含如下内容：
 * 研发中心所有的内容都是系统级的，不会对接到租户的任何内容，而租户在项目中使用时会选择对应的内容进行
 * 关联
 * <pre><code>
 *     1. 库相关：
 *        - internal：库信息，主要存储了系统库相关信息
 *        - external：库信息，外部扩展库（包括用户自定义库）
 *        - infix：内部插件信息
 *        - plugin：插件信息，插件只能在系统中处理
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface HRAD {
    /**
     * 所有研发中心定义的库信息，按类型分组
     * <pre><code>
     *     - INTERNAL
     *     - EXTERNAL
     *     - INFIX
     *     - PLUGIN
     * </code></pre>
     *
     * @return 系统库集合
     */
    ConcurrentMap<EmLib.LibraryType, Set<HLibrary>> libraries();

    /**
     * 研发中心定义的编辑器信息（工具，每种工具只能有一个引用）
     *
     * @return 编辑器集合
     */
    ConcurrentMap<EmLib.EditorType, HEditor> editors();
}
