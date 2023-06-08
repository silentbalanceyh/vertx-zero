package io.horizon.util;

import io.horizon.annotations.Memory;
import io.horizon.runtime.Macrocosm;
import io.horizon.uca.cache.Cc;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SuppressWarnings("all")
interface CACHE {
    /**
     * 全局单件模式专用
     */
    @Memory(Object.class)
    Cc<String, Object> CC_SINGLETON = Cc.open();
    /**
     * 全局类缓存专用
     */
    @Memory(Class.class)
    Cc<String, Class<?>> CC_CLASSES = Cc.open();
}

/**
 * @author lang : 2023/4/28
 */
interface INFO {
    interface Io {
        String INF_PATH = "「I/O」Absolute path is hitted: {0}.";
    }

    interface IoStream {
        String INF_PATH = "「I/O」The system class Stream try to data from {0}, got stream: {1}.";
        String INF_CUR = "「I/O」Current path is scanned by the system, up.god.file existing ? {0}.";
        String __FILE_ROOT = "「DevOps」root = {0}, file = {1}";
        String __FILE_INPUT_STREAM = "「DevOps」\t\t{0} 1. new FileInputStream(File)";
        String __RESOURCE_AS_STREAM = "「DevOps」\t\t{0} 2. clazz.getResourceAsStream(String)";
        String __CLASS_LOADER = "「DevOps」\t\t{0} 3. Thread.currentThread().getContextClassLoader()";
        String __CLASS_LOADER_STREAM = "「DevOps」\t\t{0} 4. Stream.class.getResourceAsStream(String)";
        String __CLASS_LOADER_SYSTEM = "「DevOps」\t\t{0} 5. ClassLoader.getSystemResourceAsStream(String)";
        String __JAR_RESOURCE = "「DevOps」\t\t{0} 6. Read from jar file";
    }
}

class LogUtil {
    private final Logger logger;

    private LogUtil(final Class<?> target) {
        this.logger = LoggerFactory.getLogger(target);
    }

    static LogUtil from(final Class<?> target) {
        return new LogUtil(target);
    }

    void io(final String pattern, final Object... args) {
        /* 底层防止循环调用，此处不走 DiagnosisOption */
        final String value = System.getenv(Macrocosm.DEV_IO);
        if (HUt.isBoolean(value)) {
            final boolean ioDebug = Boolean.parseBoolean(value);
            if (ioDebug) {
                final String message = HFormat.fromMessage(pattern, args);
                this.logger.info(message);
            }
        }
    }

    void info(final String pattern, final Object... args) {
        final String message = HFormat.fromMessage(pattern, args);
        this.logger.info(message);
    }

    void warn(final String pattern, final Object... args) {
        final String message = HFormat.fromMessage(pattern, args);
        this.logger.warn(message);
    }

    void error(final String pattern, final Object... args) {
        final String message = HFormat.fromMessage(pattern, args);
        this.logger.error(message);
    }
}
