package io.horizon.util;

import io.horizon.exception.WebException;
import io.horizon.exception.internal.ErrorMissingException;
import io.horizon.exception.internal.SPINullException;
import io.horizon.exception.web._500InternalCauseException;
import io.horizon.exception.web._500InternalServerException;
import io.horizon.fn.HFn;
import io.horizon.spi.HorizonIo;
import io.vertx.core.json.JsonObject;

import java.util.Objects;

/**
 * @author lang : 2023/4/30
 */
class HError {
    static String fromReadable(final int code, final Object... args) {
        final HorizonIo io = HSPI.service(HorizonIo.class);
        if (Objects.isNull(io)) {
            // 此处SPI组件不能为空，必须存在
            throw new SPINullException(HError.class);
        }
        final JsonObject message = io.ofFailure();
        final String tpl = message.getString(String.valueOf(Math.abs(code)), null);
        if (TIs.isNil(tpl)) {
            return null;
        } else {
            return HFormat.fromMessage(tpl, args);
        }
    }

    static String fromError(final String tpl,
                            final Class<?> clazz, final int code,
                            final Object... args) {
        return HFn.failOr(() -> {
            final String key = ("E" + Math.abs(code)).intern();
            final HorizonIo io = HSPI.service(HorizonIo.class);
            if (Objects.isNull(io)) {
                // 此处SPI组件不能为空，必须存在
                throw new SPINullException(HError.class);
            }
            final JsonObject data = io.ofError();
            if (null != data && data.containsKey(key)) {
                // 1. Read pattern
                final String pattern = data.getString(key);
                // 2. Build message
                final String error = HFormat.fromMessage(pattern, args);
                // 3. Format
                return HFormat.fromMessage(tpl, String.valueOf(code), clazz.getSimpleName(), error);
            } else {
                throw new ErrorMissingException(clazz, code);
            }
        }, clazz);
    }

    // 异常专用信息
    static WebException failWeb(final Class<?> clazz, final Throwable error,
                                final boolean isCause) {
        return HFn.runOr(error instanceof WebException,
            // Throwable 异常本身是 WebException，直接转出
            () -> {
                assert error instanceof WebException;
                return (WebException) error;
            },
            // Throwable 异常不是 WebException，封装成 500 默认异常转出
            () -> {
                final Class<?> target = Objects.isNull(clazz) ? HError.class : clazz;
                // 传入 Throwable 是否为空
                if (Objects.isNull(error)) {
                    return new _500InternalServerException(target, "Throwable is null");
                }
                if (isCause) {
                    // 调用 getCause() 模式
                    final Throwable cause = error.getCause();
                    if (Objects.isNull(cause)) {
                        return new _500InternalCauseException(target, error);
                    }

                    // 递归调用
                    return failWeb(clazz, cause, true);
                } else {
                    // 直接模式
                    assert !(error instanceof WebException);
                    return new _500InternalServerException(target, error.getMessage());
                }
            }
        );
    }

    static WebException failWeb(final Class<? extends WebException> clazz, final Object... args) {
        // Fix：此处必须追加 <WebException> 泛型，否则会抛出转型异常
        return HFn.<WebException>runOr(Objects.isNull(clazz),
            // 特殊情况，编程过程中忘了传入 clazz
            () -> new _500InternalServerException(clazz, "WebException class is null"),
            // 正常情况，传入 clazz
            () -> HInstance.instance(clazz, args)
        );
    }
}
