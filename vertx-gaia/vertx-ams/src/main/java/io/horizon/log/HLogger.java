package io.horizon.log;

/**
 * @author lang : 2023/4/28
 */
public interface HLogger {

    // -------------- 异常专用日志方法

    void fatal(Throwable ex);

    // -------------- 通用日志方法
    void warn(String key, Object... args);

    void error(String key, Object... args);

    void info(String key, Object... args);

    void info(boolean condition, String key, Object... args);

    void debug(String key, Object... args);
}
