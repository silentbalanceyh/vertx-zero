package io.horizon.util;

import io.horizon.fn.Actuator;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

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
        final LocalDateTime begin = TPeriod.toDateTime(Objects.requireNonNull(TPeriod.parseFull(from)));
        final LocalDateTime end = TPeriod.toDateTime(Objects.requireNonNull(TPeriod.parseFull(to)));
        TPeriod.itDay(begin.toLocalDate(), end.toLocalDate(), dayFn);
    }

    /**
     * 按天遍历日期区间
     *
     * @param from  开始日期
     * @param to    结束日期
     * @param dayFn 每一天的执行函数
     */
    public static void itDay(final LocalDateTime from, final LocalDateTime to, final Consumer<Date> dayFn) {
        TPeriod.itDay(from.toLocalDate(), to.toLocalDate(), dayFn);
    }


    /**
     * 按天遍历日期区间
     *
     * @param from  开始日期
     * @param to    结束日期
     * @param dayFn 每一天的执行函数
     */
    public static void itDay(final LocalDate from, final LocalDate to, final Consumer<Date> dayFn) {
        TPeriod.itDay(from, to, dayFn);
    }

    /**
     * 按周遍历日期区间
     *
     * @param from  开始日期
     * @param to    结束日期
     * @param dayFn 每一天的执行函数
     */
    public static void itWeek(final LocalDateTime from, final LocalDateTime to, final Consumer<Date> dayFn) {
        TPeriod.itWeek(from.toLocalDate(), to.toLocalDate(), dayFn);
    }

    /**
     * 按周遍历日期区间
     *
     * @param from  开始日期
     * @param to    结束日期
     * @param dayFn 每一天的执行函数
     */
    public static void itWeek(final LocalDate from, final LocalDate to, final Consumer<Date> dayFn) {
        TPeriod.itWeek(from, to, dayFn);
    }

    /**
     * 按周遍历日期区间
     *
     * @param from  开始日期
     * @param to    结束日期
     * @param dayFn 每一天的执行函数
     */
    public static void itWeek(final String from, final String to, final Consumer<Date> dayFn) {
        final LocalDate begin = TPeriod.toDate(TPeriod.parseFull(from));
        final LocalDate end = TPeriod.toDate(TPeriod.parseFull(to));
        TPeriod.itWeek(begin, end, dayFn);
    }

    /**
     * 带非空 null 检查的 Set 遍历，直接返回 Stream
     *
     * @param set 集合
     * @param <V> V
     *
     * @return Stream
     */
    public static <V> Stream<V> itSet(final Set<V> set) {
        return HIter.itSet(set);
    }

    /**
     * 带非空 null 检查的 List 遍历，直接返回 Stream
     *
     * @param list 集合
     * @param <V>  V
     *
     * @return Stream
     */
    public static <V> Stream<V> itList(final List<V> list) {
        return HIter.itList(list);
    }


    /**
     * （带非空 null 检查）遍历 JsonArray，提取元素为 JsonObject 的类型，返回 Stream
     *
     * @param array JsonArray
     *
     * @return Stream
     */
    public static Stream<JsonObject> itJArray(final JsonArray array) {
        return HIter.itJArray(array);
    }

    /**
     * （带非空 null 检查）遍历 JsonArray，提取元素为 JsonObject 的类型，且满足条件的，返回 Stream
     *
     * @param array     JsonArray
     * @param predicate 过滤器
     *
     * @return Stream
     */
    public static Stream<JsonObject> itJArray(final JsonArray array, final Predicate<JsonObject> predicate) {
        return HIter.itJArray(array, predicate);
    }

    /**
     * （带非空 null 检查）遍历 JsonArray，提取元素为 String 的类型，返回 Stream
     *
     * @param array JsonArray
     *
     * @return Stream
     */
    public static Stream<String> itJString(final JsonArray array) {
        return HIter.itJString(array);
    }

    /**
     * （带非空 null 检查）遍历 JsonArray，提取元素为 String 的类型，且满足条件的，返回 Stream
     *
     * @param array     JsonArray
     * @param predicate 过滤器
     *
     * @return Stream
     */
    public static Stream<String> itJString(final JsonArray array, final Predicate<String> predicate) {
        return HIter.itJString(array, predicate);
    }

    /**
     * 统一遍历输入数据中的 JsonArray 或 JsonObject，最终调用函数 executor 转换 T 类型
     *
     * @param data     输入数据
     * @param executor 转换函数
     * @param <T>      T
     *
     * @return T
     */
    public static <T> T itJson(final T data, final Function<JsonObject, T> executor) {
        return HIter.itJson(data, executor);
    }

    /**
     * 按次数重复执行
     *
     * @param times    次数
     * @param actuator 执行函数
     */
    public static void itRepeat(final Integer times, final Actuator actuator) {
        HIter.itRepeat(times, actuator);
    }
}
