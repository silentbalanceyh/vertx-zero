package io.horizon.fn;

import io.horizon.exception.AbstractException;
import io.horizon.exception.ProgramException;
import io.horizon.specification.uca.HLogger;

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
            // 自定义异常
            ex.printStackTrace();
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
            // 自定义异常
            ex.printStackTrace();
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
        } catch (final AbstractException ex) {
            if (Objects.nonNull(logger)) {
                logger.fatal(ex);
            }
            // 自定义异常
            ex.printStackTrace();
            throw ex;
        } catch (final Exception ex) {
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

    static <T, R> R runAt(final R defaultValue, final Function<T, R> function) {
        final R ret = function.apply(null);
        return Objects.isNull(ret) ? defaultValue : ret;
    }
}
