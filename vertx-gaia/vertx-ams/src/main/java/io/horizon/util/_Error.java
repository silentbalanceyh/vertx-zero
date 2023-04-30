package io.horizon.util;

import io.horizon.exception.WebException;

/**
 * @author lang : 2023/4/30
 */
class _Error extends _Env {
    /**
     * 生成一个 WebException
     *
     * @param clazz WebException 的调用类
     * @param error 其他异常
     *
     * @return WebException
     */
    public static WebException failWeb(final Class<?> clazz, final Throwable error) {
        return HError.failWeb(clazz, error, false);
    }

    /**
     * 生成一个 WebException
     *
     * @param clazz   WebException 的调用类
     * @param error   其他异常
     * @param isCause 是否使用 error 的 cause 作为 WebException 的 cause
     *
     * @return WebException
     */
    public static WebException failWeb(final Class<?> clazz, final Throwable error, final boolean isCause) {
        return HError.failWeb(clazz, error, isCause);
    }

    /**
     * 生成一个 WebException
     *
     * @param clazz WebException 的调用类
     * @param args  WebException 的参数
     *
     * @return WebException
     */
    public static WebException failWeb(final Class<? extends WebException> clazz, final Object... args) {
        return HError.failWeb(clazz, args);
    }
}
