package io.horizon.specification.boot;

/**
 * 「模块启动器」模块启动专用接口
 * 辅助启动器，使用 OSGI 模式启动模块，内置启动容器类型为泛型，同 {@link HLauncher} 一样可直接完成启动之前的动作
 * 此启动器更契合在启动容器过程中的 Bundle 生命周期处理，主要包括：
 * <pre><code>
 *     - configure：注册模块之前的配置预处理，配置完成后 Resolved
 *     - install：安装模块, 完成之后 Installed
 *     - start：启动模块（激活）
 *     - stop：停止模块
 *     - unregister：注销模块
 *     - cleanup: 清除数据、验证
 * </code></pre>
 *
 * @author lang : 2023-05-25
 */
public interface HInstall<BND> {
    void configure(BND bundle);

    void install(BND bundle);

    void start(BND bundle);

    void stop(BND bundle);

    void unregister(BND bundle);

    void cleanup(BND bundle);
}
