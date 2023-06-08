package io.horizon.specification.boot;

import io.horizon.exception.web._501NotImplementException;
import io.macrocosm.specification.boot.HOff;
import io.macrocosm.specification.boot.HOn;
import io.macrocosm.specification.boot.HRun;
import io.macrocosm.specification.config.HConfig;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

/**
 * 「启动器」专用接口
 * 用于主启动容器，主启动容器的类型为泛型，可以帮助系统启动完成，主容器生命周期很简单
 * <pre><code>
 *     - start：启动
 *     - stop：停止
 *     - refresh：刷新
 *     - restart：重启
 * </code></pre>
 * 示例代码通常如下：
 * <pre><code>
 *     HLauncher launcher = ...;
 *     launcher.start(server -> {
 *         // 真正的启动周期，之前可以针对 server 做相关调整以及配置
 *     });
 * </code></pre>
 *
 * 所有在启动之前和停止之后的动作都在实现类内部处理，而保证实现类的自由度，如：
 * <pre><code>
 *     1. 配置文件加载
 *     2. 准备类加载器
 *     3. 调用 Google Guice 启动IoC
 *     4. 准备文件和配置缓存
 *     5. 启动数据库连接池
 *     4. Metadata仓库基础配置
 * </code></pre>
 * 即：{@link HLauncher} 的每个行为的回调函数才是整个容器Ready之后刚刚创建的过程，内部实现全部放到实现类中。
 * <pre><code>
 *     1. 主容器种类
 *        - SM模式：OSGI Launcher -> Bundle ( Jetty )
 *        - Zero模式：Zero Launcher -> Vertx
 *        - Aeon模式：Aeon Launcher -> HAeon
 *     2. 辅助组件
 *        生命周期辅助组件为启动新规范，为了让启动器在每个生命周期多一层，且启动组件会
 *        跟随启动器的启动而启动，跟随启动器的停止而停止，所以此处的启动器需要提供一个
 *        完整的生命周期组件处理和启动相关的事情
 *        - on：{@link HOn} 启动组件
 *        - off：{@link HOff} 停止组件
 *        - run: {@link HRun} 运行组件
 * </code></pre>
 *
 * @author lang
 */
public interface HLauncher<WebServer> {
    /**
     * 启动服务器，由于此处在实现层会处理内置的配置模型，之所以使用 void 的返回值主要是
     * 考虑到启动过程中可能存在异步调用所以 void 可以保证通过 callback 的写法处理所有
     * 异步行为而不影响主逻辑，由于此类是入口类，所以此处根据最新的启动器规范进行修订，以保证
     *
     * @param on     服务器配置消费（容器启动之前）
     * @param server 服务器消费器（容器启动之后）
     */
    <WebConfig extends HConfig> void start(HOn<WebConfig> on, Consumer<WebServer> server);

    /**
     * 停止服务器，由于此处在实现层会处理内置的配置模型，所以使用回调模式 Consumer 来完
     * 成真正意义的启动，AOP这一层直接放到启动器停止后
     *
     * @param off    服务器配置消费（容器停止之前）
     * @param server 服务器消费器（容器停止之后）
     */
    <WebConfig extends HConfig> void stop(HOff<WebConfig> off, Consumer<WebServer> server);

    /**
     * 重启服务器
     *
     * @param run    服务器重启准备
     * @param server 服务器引用
     */
    default <WebConfig extends HConfig> void restart(final HRun<WebConfig> run, final Consumer<WebServer> server) {
        throw new _501NotImplementException(this.getClass());
    }

    /**
     * 刷新服务器（热部署模式）
     *
     * @param run    服务器刷新准备
     * @param server 服务器引用
     */
    default <WebConfig extends HConfig> void refresh(final HRun<WebConfig> run, final Consumer<WebServer> server) {
        throw new _501NotImplementException(this.getClass());
    }

    /**
     * 节点模式的服务器专用
     *
     * @return 服务器存储
     */
    default ConcurrentMap<String, WebServer> store() {
        return new ConcurrentHashMap<>();
    }
}
