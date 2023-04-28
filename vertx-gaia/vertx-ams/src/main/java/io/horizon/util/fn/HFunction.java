package io.horizon.util.fn;

import io.horizon.exception.AbstractException;
import io.horizon.exception.ProgramException;
import io.horizon.fn.ErrorFunction;
import io.horizon.fn.ExceptionFunction;
import io.horizon.fn.ProgramFunction;
import io.horizon.log.HLogger;

import java.util.Objects;
import java.util.function.Function;

/**
 * @author lang : 2023/4/28
 */
class HFunction {
    private HFunction() {
    }

    static <T, R> R jvmAt(final R defaultValue, final ErrorFunction<T, R> function, final HLogger logger) {
        try {
            final R ret = function.apply(null);
            return Objects.isNull(ret) ? defaultValue : ret;
        } catch (final AbstractException ex) {
            if (Objects.nonNull(logger)) {
                logger.fatal(ex);
            }
            throw ex;
        } catch (final Throwable ex) {
            // 环境变量开启时打印异常堆栈
            if (Objects.nonNull(logger)) {
                logger.fatal(ex);
            }
            return defaultValue;
        }
    }

    static <T, R> R bugAt(final R defaultValue, final ProgramFunction<T, R> function,
                          final HLogger logger) throws ProgramException {
        try {
            final R ret = function.apply(null);
            return Objects.isNull(ret) ? defaultValue : ret;
        } catch (final ProgramException ex) {
            if (Objects.nonNull(logger)) {
                logger.fatal(ex);
            }
            // 日志记录器追加
            return defaultValue;
        } catch (final Throwable ex) {
            // 环境变量开启时打印异常堆栈
            if (Objects.nonNull(logger)) {
                logger.fatal(ex);
            }
            return defaultValue;
        }
    }

    static <T, R> R failAt(final R defaultValue, final ExceptionFunction<T, R> function, final HLogger logger) {
        try {
            final R ret = function.apply(null);
            return Objects.isNull(ret) ? defaultValue : ret;
        } catch (final Exception ex) {
            if (Objects.nonNull(logger)) {
                logger.fatal(ex);
            }
            // DEBUG:
            if (ex instanceof AbstractException) {
                throw (AbstractException) ex;
            }
            // 日志记录器追加
            return defaultValue;
        } catch (final Throwable ex) {
            // 环境变量开启时打印异常堆栈
            if (Objects.nonNull(logger)) {
                logger.fatal(ex);
            }
            // ex.printStackTrace();
            return defaultValue;
        }
    }

    static <T, R> R runAt(final R defaultValue, final Function<T, R> function) {
        final R ret = function.apply(null);
        return Objects.isNull(ret) ? defaultValue : ret;
    }
}
