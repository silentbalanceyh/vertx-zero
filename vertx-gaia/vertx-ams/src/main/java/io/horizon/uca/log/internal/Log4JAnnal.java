package io.horizon.uca.log.internal;

import io.horizon.exception.AbstractException;
import io.horizon.exception.ProgramException;
import io.horizon.runtime.Macrocosm;
import io.horizon.uca.log.Annal;
import io.horizon.util.HUt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;


public class Log4JAnnal extends AbstractAnnal implements Annal {

    private transient final Logger logger;

    public Log4JAnnal(final Class<?> clazz) {
        if (Objects.isNull(clazz)) {
            this.logger = LoggerFactory.getLogger(Log4JAnnal.class);
        } else {
            this.logger = LoggerFactory.getLogger(clazz);
        }
    }

    public Log4JAnnal(final Logger logger) {
        this.logger = logger;
    }

    @Override
    public void fatal(final Throwable ex) {
        Objects.requireNonNull(ex);
        if (ex instanceof ProgramException || ex instanceof AbstractException) {
            this.logger.warn(ex.getMessage());
        } else {
            this.logger.error(ex.getMessage());
            final Boolean isDebug = HUt.envWith(Macrocosm.DEV_JVM_STACK, Boolean.FALSE, Boolean.class);
            if (isDebug) {
                /*
                 * 堆栈信息打印条件
                 * 1. 开启了 DEV_JVM_STACK 环境变量
                 * 2. 传入异常不可为空
                 */
                ex.printStackTrace();
            }
        }
    }

    @Override
    public void warn(final String key, final Object... args) {
        this.log(this.logger::isWarnEnabled, this.logger::warn, key, args);
    }

    @Override
    public void error(final String key, final Object... args) {
        this.log(this.logger::isErrorEnabled, this.logger::error, key, args);
    }


    @Override
    public void info(final String key, final Object... args) {
        this.log(this.logger::isInfoEnabled, this.logger::info, key, args);
    }

    @Override
    public void info(final boolean condition, final String key, final Object... args) {
        this.log(() -> condition && this.logger.isInfoEnabled(), this.logger::info, key, args);
    }

    @Override
    public void debug(final String key, final Object... args) {
        this.log(this.logger::isDebugEnabled, this.logger::debug, key, args);
    }
}
