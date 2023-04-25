package io.horizon.cloud.boot;

import io.aeon.atom.iras.HAeon;
import io.horizon.cloud.action.HEvent;
import io.vertx.core.Future;

/**
 * 「指令」准入（底层抽象，负责检查）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HOn extends HEvent<HAeon, Boolean> {
    // 合法性 / 合规性检查
    @Override
    default Future<Boolean> configure(final HAeon input) {
        return HEvent.super.configure(input);
    }
}
