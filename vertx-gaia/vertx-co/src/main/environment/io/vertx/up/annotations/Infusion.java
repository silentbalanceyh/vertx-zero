package io.vertx.up.annotations;

import java.lang.annotation.*;

/**
 * 旧版本不成熟设计中的Infix架构，此处需重新处理一部分，原版系统流程
 * <pre><code>
 *     1. 启动时 Agent/Worker 全部走的单件模式，非延迟加载
 *     2. 然后在扫描时通过 Infusion 调用单件构造
 *     上述流程在新版本中会有问题，新版本中会因为延迟加载和非单件模式导致 @Infusion 注入失效
 * </code></pre>
 * 新版本中的流程，Agent/Worker 都改成了延迟加载，所以此处的构造需要将 Infusion 的构造扔到构造 Agent/Worker
 * 中打个构造补丁，来实现注入
 * <pre><code>
 *     所有构造 Agent/Worker 地方都需重写，调用 XInfix.getXXX() 方法来提取唯一的 Client 对象
 * </code></pre>
 */
@Target({ElementType.TYPE, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Infusion {
}
