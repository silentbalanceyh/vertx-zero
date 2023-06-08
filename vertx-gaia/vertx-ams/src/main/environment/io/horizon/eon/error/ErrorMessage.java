package io.horizon.eon.error;

/**
 * @author lang : 2023/4/27
 */
public interface ErrorMessage {
    /**
     * InternalException 专用错误信息模板，通常用于最底层的基础工具类异常，主要包含
     * 1. 内部工具执行处理异常
     * 2. 函数流程执行处理异常
     * 3. 通用系统级异常
     */
    String EXCEPTION_INTERNAL = "[ ERR{} ] ( {} ) Internal Error : {}";

    /**
     * DaemonException 专用错误信息模板，一般用于守护进程类异常，主要包含
     * 1. 后台执行程序异常
     * 2. 后台组件处理程序异常
     */
    String EXCEPTION_DAEMON = "[ ERR{} ] ( {} ) Daemon Error : {}";

    /**
     * BootException 专用错误信息模板，一般用于容器启动类异常
     * 1. 容器启动异常
     * 2. 模块启动异常
     * 3. 插件激活异常
     */
    String EXCEPTION_BOOTING = "[ ERR{} ] ( {} ) Booting Error: {}";

    /**
     * 带状态代码的 Web 级专用异常，此种异常通常定义如：_NNNXXX 的格式，此处
     * _NNN 为三位数字的状态代码，XXX 为异常类型，如：_400XXX
     */
    String EXCEPTION_WEB = "[ ERR{} ] ( {} ) Web Error: {}";
}
