package io.macrocosm.specification.boot;

import io.horizon.specification.typed.TCommand;
import io.macrocosm.specification.config.HConfig;

/**
 * 「指令」准入（底层抽象，负责检查）
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HOn<T extends HConfig> extends TCommand<T, Boolean> {
    /**
     * 此方法可以直接菜单 HOn 引用中提取启动配置，由于 HOn 是单件模式，所以
     * 启动配置只有一份不会出现多份，基于此种设计，启动配置就可以直接存储在
     * HOn组件中，而HOn组件又会被传入到 {@link io.horizon.specification.boot.HLauncher}
     * 中，那么真实启动器提取配置就会变得很简洁，而启动配置的初始化在
     * 调用 HOn 的 configure 方法中可执行完成。
     *
     * @return {@link T}
     */
    default T store() {
        return null;
    }

    /**
     * 启动参数绑定
     *
     * @param args 待绑定参数
     *
     * @return 当前引用
     */
    default HOn<T> args(final String[] args) {
        return this;
    }
}
