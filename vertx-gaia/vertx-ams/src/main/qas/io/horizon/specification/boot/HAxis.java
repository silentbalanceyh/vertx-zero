package io.horizon.specification.boot;

/**
 * 「路由注册器」
 * 在 Zero 中专用于路由挂载模块，挂载路由发布 RESTful 到环境中
 * <pre><code>
 *     Zero中
 *      - 1. Router
 *      - 2. Route
 *      - 3. Event
 *     通用型分流处理
 *      - 1. RESTful
 *      - 2. WebSocket ( Quic / HTTP3 )
 *      - 3. Scheduler ( Task )
 *      - 4. Monitor ( 监控 ）
 * </code></pre>
 *
 * @author lang
 */
public interface HAxis<GATEWAY> {
    void mount(GATEWAY router);
}
