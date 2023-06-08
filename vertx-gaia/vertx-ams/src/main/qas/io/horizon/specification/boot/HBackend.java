package io.horizon.specification.boot;

/**
 * 「管理端」挂载模式
 * 管理端挂载模式主要用于系统执行管理部分的核心挂载
 * <pre><code>
 *     1. 第三方 console 加载
 *     2. 集成通道加载
 *     3. 注册中心
 *     4. 配置中心
 *     5. 日志监控加载
 * </code></pre>
 * 队列启动时专用
 *
 * @author lang : 2023-05-25
 */
public interface HBackend<ENTRY> {

    void mount(ENTRY router);
}
