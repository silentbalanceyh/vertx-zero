package io.horizon.specification.executor;

import io.horizon.specification.storage.HPath;
import io.horizon.specification.typed.TExecutor;

import java.util.Set;

/**
 * 「执行器」备份
 *
 * @author lang : 2023-05-21
 */
public interface HBackup extends TExecutor {
    /**
     * 抽象路径，备份的具体目标位置
     *
     * @return {@link HPath}
     */
    default Set<HPath> targets() {
        return Set.of();
    }

    /**
     * 快速接口，抽象路径（唯一值，只包含一个的情况）
     *
     * @return {@link HPath}
     */
    default HPath target() {
        final Set<HPath> targets = this.targets();
        return targets.isEmpty() ? null : targets.iterator().next();
    }
}
