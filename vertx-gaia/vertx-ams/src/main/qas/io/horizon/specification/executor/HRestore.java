package io.horizon.specification.executor;

import io.horizon.specification.storage.HPath;
import io.horizon.specification.typed.TExecutor;

import java.util.Set;

/**
 * 「执行器」还原
 *
 * @author lang : 2023-05-21
 */
public interface HRestore extends TExecutor {
    /**
     * 抽象路径，还原的具体源位置
     *
     * @return {@link HPath}
     */
    default Set<HPath> sources() {
        return Set.of();
    }

    /**
     * 快速接口，抽象路径（唯一值，只包含一个的情况）
     *
     * @return {@link HPath}
     */
    default HPath source() {
        final Set<HPath> targets = this.sources();
        return targets.isEmpty() ? null : targets.iterator().next();
    }
}
