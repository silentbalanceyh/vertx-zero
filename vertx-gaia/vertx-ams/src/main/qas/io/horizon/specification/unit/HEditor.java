package io.horizon.specification.unit;

import io.horizon.eon.em.EmLib;
import io.horizon.specification.typed.TContract;
import io.horizon.specification.typed.TProducer;
import io.vertx.core.buffer.Buffer;

/**
 * 「编辑器」Editor
 * 核心工具专用，分成不同的类型，类型参考 {@link EmLib.EditorType}
 *
 * <pre><code>
 *     1. {@link TContract}
 *        - identifier()
 *        - version()
 *     2. {@link TProducer}
 *        - name()
 *        - dependencies()
 *        - datum()
 *     3. {@link HEditor}
 *        - type()
 *        - definition()
 * </code></pre>
 *
 * @author lang : 2023-05-20
 */
public interface HEditor extends TProducer {

    /**
     * 当前编辑器的类型
     *
     * @return {@link EmLib.EditorType}
     */
    EmLib.EditorType type();

    /**
     * 当前编辑器的工程文件内容
     * <pre><code>
     *     1. 可以是 JSON 格式
     *     2. 可以是 XML 格式
     *     3. 也可以直接二进制格式
     * </code></pre>
     *
     * @return {@link String}
     */
    Buffer definition();
}
