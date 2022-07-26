package io.vertx.aeon.specification.boot;

import io.vertx.aeon.atom.configuration.HAeon;
import io.vertx.aeon.specification.element.HEvent;

/**
 * 「指令」准入（底层抽象，负责检查）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HOn extends HEvent<HAeon, Boolean> {
    // 合法性 / 合规性检查
}
