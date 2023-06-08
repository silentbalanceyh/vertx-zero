package io.macrocosm.specification.program;

import io.horizon.specification.typed.TComponent;

/**
 * 「容器运行时」Cri
 * <hr/>
 * 直接对接实际容器专用的基础容器运行时接口，底层实现可使用 Docker 或 Podman 完成容器本身的对接。
 * 后期扩展还会追加：
 * <pre><code>
 *     1. 镜像管理，镜像仓库引用
 *     2. 上层容器链
 *     3. 资源容器链
 *     4. 服务清单
 * </code></pre>
 *
 * @author lang : 2023-05-21
 */
public interface HCRI extends TComponent {
    /**
     * 容器运行的 Pod 关联信息
     *
     * @return {@link HPod}
     */
    HPod pod();
}
