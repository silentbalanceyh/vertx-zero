package io.macrocosm.atom.boot;

import io.macrocosm.specification.config.HConfig;
import io.macrocosm.specification.config.HSetting;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 核心配置存储，专用于存储启动配置，替换原始组件部分
 * <pre><code>
 *     1. 核心框架初始化部分
 *     2. Extension 扩展框架的 Pin 部分
 *     3. Scan 的类结果部分
 * </code></pre>
 * 高阶部分的核心接口
 * <pre><code>
 *     1. {@link io.macrocosm.specification.config.HConfig}，组件配置接口
 *     2. {@link KEnergy} 启动配置接口
 *     3. {@link io.horizon.uca.boot.KLauncher} 启动器接口
 *     4. {@link io.horizon.spi.BootIo} 启动选择器 / 组件加载器
 * </code></pre>
 * 此部分底层还可以走一个特殊的 {@link io.horizon.specification.storage.HStore}，然后从 HStore
 * 中提取配置数据部分，这样可以实现配置数据的存储，而不是直接存储在内存中。完整的结构如：
 * <pre><code>
 *     1. container：容器配置
 *     2. launcher：启动器配置
 *     3. extension：Zero Extension扩展模块配置
 *     4. infix：Infix架构下的插件配置
 *     5. registry：注册表，注册表中保存了应该有的配置路径
 * </code></pre>
 *
 * @author lang : 2023-05-30
 */
public class KSetting implements HSetting {
    /** 扩展配置部分 **/
    private final ConcurrentMap<String, HConfig> extension =
        new ConcurrentHashMap<>();
    /** 插件配置 **/
    private final ConcurrentMap<String, HConfig> infix =
        new ConcurrentHashMap<>();
    /** 容器主配置 */
    private HConfig container;
    /** 启动器配置 **/
    private HConfig launcher;

    private KSetting() {

    }

    public static HSetting of() {
        return new KSetting();
    }

    @Override
    public HConfig container() {
        return this.container;
    }

    @Override
    public HSetting container(final HConfig container) {
        this.container = container;
        return this;
    }

    @Override
    public HConfig launcher() {
        return this.launcher;
    }

    @Override
    public HSetting launcher(final HConfig launcher) {
        this.launcher = launcher;
        return this;
    }

    @Override
    public HSetting extension(final String name, final HConfig config) {
        this.extension.put(name, config);
        return this;
    }

    @Override
    public HConfig extension(final String name) {
        return this.extension.get(name);
    }

    @Override
    public HSetting infix(final String name, final HConfig config) {
        this.infix.put(name, config);
        return this;
    }

    @Override
    public HConfig infix(final String name) {
        return this.infix.get(name);
    }

    @Override
    public boolean hasInfix(final String name) {
        return this.infix.containsKey(name);
    }
}