package io.horizon.specification.typed;

import io.horizon.annotations.reference.One2One;
import io.horizon.specification.storage.HStore;
import io.modello.specification.element.HNs;

/**
 * 「工程部署」Descriptor
 * <hr/>
 * 工程部署接口，用于描述所需的工程部署文件，工程部署会包含
 * <pre><code>
 *     1. 项目关联的所有工程部署文件：开发和调试
 *     2. 管理端应用的所有工程部署文件：运行
 *     3. 部署计划的所有工程部署文件：部署和运维
 * </code></pre>
 *
 * @author lang : 2023-05-21
 */
public interface TDescriptor extends HNs {
    /**
     * 部署描述符的统一标识符
     *
     * @return {@link String}
     */
    String identifier();

    /**
     * 部署描述符消费的存储基础，关联到 {@link HStore} 中的
     * 基本顶层存储信息，部署描述符和存储之间只会存在一对一的关系，不运行出现一对多的情况
     *
     * @return {@link String}
     */
    @One2One
    String store();
}
