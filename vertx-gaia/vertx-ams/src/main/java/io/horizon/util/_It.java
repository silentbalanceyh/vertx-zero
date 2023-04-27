package io.horizon.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * @author lang : 2023/4/27
 */
class _It extends _Is {
    /**
     * 按天遍历日期区间
     *
     * @param from  开始日期
     * @param to    结束日期
     * @param dayFn 每一天的执行函数
     */
    public static void itDay(final String from, final String to, final Consumer<Date> dayFn) {
        final LocalDateTime begin = HPeriod.toDateTime(Objects.requireNonNull(HPeriod.parseFull(from)));
        final LocalDateTime end = HPeriod.toDateTime(Objects.requireNonNull(HPeriod.parseFull(to)));
        HPeriod.itDay(begin.toLocalDate(), end.toLocalDate(), dayFn);
    }

    /**
     * 按天遍历日期区间
     *
     * @param from  开始日期
     * @param to    结束日期
     * @param dayFn 每一天的执行函数
     */
    public static void itDay(final LocalDateTime from, final LocalDateTime to, final Consumer<Date> dayFn) {
        HPeriod.itDay(from.toLocalDate(), to.toLocalDate(), dayFn);
    }


    /**
     * 按天遍历日期区间
     *
     * @param from  开始日期
     * @param to    结束日期
     * @param dayFn 每一天的执行函数
     */
    public static void itDay(final LocalDate from, final LocalDate to, final Consumer<Date> dayFn) {
        HPeriod.itDay(from, to, dayFn);
    }

    /**
     * 按周遍历日期区间
     *
     * @param from  开始日期
     * @param to    结束日期
     * @param dayFn 每一天的执行函数
     */
    public static void itWeek(final LocalDateTime from, final LocalDateTime to, final Consumer<Date> dayFn) {
        HPeriod.itWeek(from.toLocalDate(), to.toLocalDate(), dayFn);
    }

    /**
     * 按周遍历日期区间
     *
     * @param from  开始日期
     * @param to    结束日期
     * @param dayFn 每一天的执行函数
     */
    public static void itWeek(final LocalDate from, final LocalDate to, final Consumer<Date> dayFn) {
        HPeriod.itWeek(from, to, dayFn);
    }

    /**
     * 按周遍历日期区间
     *
     * @param from  开始日期
     * @param to    结束日期
     * @param dayFn 每一天的执行函数
     */
    public static void itWeek(final String from, final String to, final Consumer<Date> dayFn) {
        final LocalDate begin = HPeriod.toDate(HPeriod.parseFull(from));
        final LocalDate end = HPeriod.toDate(HPeriod.parseFull(to));
        HPeriod.itWeek(begin, end, dayFn);
    }
}
