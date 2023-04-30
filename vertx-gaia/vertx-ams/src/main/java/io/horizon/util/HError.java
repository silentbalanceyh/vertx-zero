package io.horizon.util;

import io.horizon.exception.internal.ErrorMissingException;
import io.horizon.exception.internal.SPINullException;
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
}
