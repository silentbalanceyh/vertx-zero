package io.vertx.up.eon.em.secure;

/**
 * ACL的作用周期
 * - DELAY：延迟执行，处理影响型请求专用
 * - EAGER：及时执行，处理当前请求专用
 * - ERROR：配置错误导致ACL的作用周期失效
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum ActPhase {
    DELAY, // Delay for ACL control
    EAGER, // Eager for ACL control, this control should impact current request
    ERROR, // Error phase
}
