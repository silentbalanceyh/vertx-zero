package io.horizon.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;

/**
 * @author lang : 2023/4/27
 */
class _Parse extends _Net {
    protected _Parse() {
    }

    /**
     * 将字符串literal转换成 Date类型（和JDK老版本对接）
     *
     * @param literal 字符串
     *
     * @return Date
     */
    public static Date parse(final String literal) {
        return TPeriod.parse(literal);
    }

    /**
     * 将LocalTime转换成 Date类型（和JDK老版本对接）
     *
     * @param time LocalTime
     *
     * @return Date
     */
    public static Date parse(final LocalTime time) {
        return TPeriod.parse(time);
    }

    /**
     * 将LocalDateTime转换成 Date类型（和JDK老版本对接）
     *
     * @param datetime LocalDateTime
     *
     * @return Date
     */
    public static Date parse(final LocalDateTime datetime) {
        return TPeriod.parse(datetime);
    }

    /**
     * 将LocalDate转换成 Date类型（和JDK老版本对接）
     *
     * @param date LocalDate
     *
     * @return Date
     */
    public static Date parse(final LocalDate date) {
        return TPeriod.parse(date);
    }

    /**
     * 将字符串literal转换成 Date类型（和JDK老版本对接）
     *
     * @param literal 字符串
     *
     * @return Date
     */
    public static Date parseFull(final String literal) {
        return TPeriod.parseFull(literal);
    }
}
