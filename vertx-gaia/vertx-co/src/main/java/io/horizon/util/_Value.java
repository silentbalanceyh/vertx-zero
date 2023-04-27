package io.horizon.util;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author lang : 2023/4/27
 */
class _Value extends _To {
    /**
     * （计算过时区）获取当前时间，转换成 Date 值
     *
     * @return Date
     */
    public static Date valueNow() {
        return HPeriod.parse(LocalDateTime.now());
    }
}
