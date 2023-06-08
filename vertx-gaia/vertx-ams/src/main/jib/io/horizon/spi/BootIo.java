package io.horizon.spi;

import io.horizon.specification.boot.HLauncher;
import io.macrocosm.atom.boot.KEnergy;
import io.macrocosm.specification.config.HEnergy;

/**
 * 「主配置入口规范」
 * - 不考虑 Zero Framework 本身启动规范，它自身会直接执行 {@link io.horizon.specification.boot.HLauncher}
 * Aeon平台拥有Aeon的基础配置文件规范，SMAVE拥有自身规范，都遵循此核心目录配置，本接口在于设计
 * 核心的启动接口规范，用于提取启动配置文件。
 * 启动统一配置 {@link io.horizon.eon.VSpec.Boot} 中进行定义
 *
 * @author lang : 2023-05-30
 */
public interface BootIo {
    /**
     * 提取主启动器，启动当前容器环境
     *
     * @param <T> : 启动器类型
     *
     * @return {@link HLauncher}
     */
    <T> HLauncher<T> launcher();

    /**
     * 同时从启动配置中提取能量配置
     *
     * @param bootCls : 启动类
     * @param args    : 启动参数
     *
     * @return {@link KEnergy}
     */
    HEnergy energy(Class<?> bootCls, String[] args);
}
