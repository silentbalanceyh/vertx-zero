package io.modello.atom.app;

import io.horizon.exception.boot.CombineOwnerException;
import io.horizon.util.HUt;
import io.macrocosm.specification.secure.HoI;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 「租户维度」
 * 多租户专用的租户对象标识（该对象规范从 {@see XHeader} 中提取
 * <pre><code>
 *     - sigma: 统一标识
 *     - language：语言信息
 *     - appKey / appId / appName （内置包含 {@link io.macrocosm.specification.app.HApp}
 *     拥有者ID，特殊维度（云环境专用，处理核心维度）
 * </code></pre>
 * oi = Owner ID（持有者标识）
 * <pre><code>
 *     新版对接协议
 *     实现：              {@link io.macrocosm.specification.app.HRegistry}
 *         默认：          {@link io.horizon.uca.boot.KRegistry}
 *         zero-ambient:  {@see RegistryExtension}
 *         aeon-ambient:  {@see RegistryAeon}
 *                               |
 *                               |
 *                               |
 *                        ( initialized )
 *                            HAmbient  ---------> KAmbientContext -------> KOI
 *                               |
 *                               |
 *                             HArk ( running ) -----------> key ---------> KOI
 *                               |
 *                               |
 *         connect:        sigma / appId / appKey / name / code / tenantId
 *
 *     充当了上下文环境的应用/租户 请求信息，和 HArk 不同的点在于，当前静态中，可执行全局化调用，
 *     且请求产生时执行初始化，依赖 XHeader 中数据
 * </code></pre>
 *
 * > 为了区别于 {@link io.macrocosm.specification.secure.HTenant} 和 {@link io.horizon.specification.secure.HOwner}
 * 此处的拼写改成 Tenement，具有多层租户含义，对应接口为 {@link HoI}
 *
 * @author lang : 2023-06-06
 */
public class KTenement implements HoI {

    private final ConcurrentMap<String, HoI> children = new ConcurrentHashMap<>();

    private final String id;

    public KTenement() {
        this(null);
    }

    public KTenement(final String id) {
        this.id = HUt.keyOwner(id);
    }

    @Override
    public String owner() {
        return this.id;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || this.getClass() != o.getClass()) {
            return false;
        }
        final KTenement owner = (KTenement) o;
        return Objects.equals(this.id, owner.id);
    }

    @Override
    public HoI child(final String id) {
        return this.children.getOrDefault(id, null);
    }

    @Override
    public void child(final String id, final HoI hoi) {
        Optional.ofNullable(hoi).ifPresent(h -> this.children.put(id, h));
    }

    @Override
    public HoI apply(final HoI target) {
        if (Objects.nonNull(target)) {
            if (!target.equals(this)) {
                throw new CombineOwnerException(this.getClass(), this.id, target.owner());
            }
        }
        return this;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }
}
