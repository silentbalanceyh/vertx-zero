package io.horizon.spi.boot;

import io.macrocosm.specification.config.HSetting;

/**
 * 「装配器」
 * 读取配置专用，装配器可以用来直接生成 {@link io.macrocosm.specification.config.HSetting}
 *
 * @author lang : 2023-05-30
 */
public interface HEquip {
    /**
     * 执行装配器初始化
     *
     * @return {@link HSetting}
     */
    HSetting initialize();
}
