package io.horizon.util;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Consumer;

/**
 * @author lang : 2023/4/27
 */
final class TPeriod {
    private static final List<DateTimeFormatter> DATES = new ArrayList<>() {
        {
            this.add(Iso.DATE);
            this.add(Iso.BASIC_DATE);
            this.add(Iso.ORDINAL_DATE);
            this.add(Iso.OFFSET_DATE);
            this.add(Iso.LOCAL_DATE);
            this.add(Iso.WEEK_DATE);
        }
    };
    private static final List<DateTimeFormatter> DATETIMES = new ArrayList<>() {
        {
            this.add(Iso.DATE_TIME);
            this.add(Iso.INSTANT);
            this.add(Iso.RFC1123_DATE_TIME);
            this.add(Iso.COMMON);
            this.add(Iso.READBALE);
            this.add(Iso.OFFSET_DATE_TIME);
            this.add(Iso.LOCAL_DATE_TIME);
            this.add(Iso.ZONED_DATE_TIME);
        }
    };
    private static final List<DateTimeFormatter> TIMES = new ArrayList<>() {
        {
            this.add(Iso.TIME);
            this.add(Iso.LOCAL_TIME);
            this.add(Iso.OFFSET_TIME);
            this.add(Iso.TIME_FIX);
        }
    };

    private TPeriod() {
    }

    /**
     * Convert to datetime
     *
     * @param literal the literal that will be
     *
     * @return null or valid DateTime
     */
    static LocalDateTime toDateTime(final String literal) {
        return DATETIMES.stream()
            .map(formatter -> parseEach(literal, formatter, LocalDateTime::parse))
            .filter(Objects::nonNull)
            .findAny()
            .orElse(null);
    }

    private static <T> T parseEach(final String literal, final DateTimeFormatter formatter,
                                   final BiFunction<String, DateTimeFormatter, T> executor) {
        if (TIs.isNil(literal)) {
            return null;
        } else {
            try {
                return executor.apply(literal, formatter);
            } catch (final Throwable ex) {
                return null;
            }
        }
    }

    /**
     * Convert to date
     *
     * @param date input java.util.Date
     *
     * @return parsed LocalDateTime
     */
    static LocalDateTime toDateTime(final Date date) {
        return toDateTime(date.toInstant());
    }

    static LocalDateTime toDateTime(final Instant instant) {
        return LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
    }

    /**
     * Convert to date
     *
     * @param literal literal that will be parsed
     *
     * @return parsed LocalDate
     */
    static LocalDate toDate(final String literal) {
        /*
         * Directly Parsing
         */
        final LocalDate date = DATES.stream()
            .map(formatter -> parseEach(literal, formatter, LocalDate::parse))
            .filter(Objects::nonNull)
            .findAny()
            .orElse(null);
        if (Objects.isNull(date)) {
            final LocalDateTime datetime = toDateTime(literal);
            if (Objects.nonNull(datetime)) {
                /*
                 * LocalDateTime -> LocalDate
                 */
                return datetime.toLocalDate();
            } else {
                return null;
            }
        } else {
            /*
             * Valid Parsing
             */
            return date;
        }
    }

    /**
     * Convert to date
     *
     * @param date input Date
     *
     * @return LocalDate parsed
     */
    static LocalDate toDate(final Date date) {
        final LocalDateTime datetime = toDateTime(date);
        return datetime.toLocalDate();
    }

    static LocalDate toDate(final Instant instant) {
        final LocalDateTime datetime = toDateTime(instant);
        return datetime.toLocalDate();
    }

    /**
     * Convert to time
     *
     * @param literal input literal
     *
     * @return LocalTime parsed
     */
    static LocalTime toTime(final String literal) {
        return TIMES.stream()
            .map(formatter -> parseEach(literal, formatter, LocalTime::parse))
            .filter(Objects::nonNull)
            .findAny()
            .orElse(null);
    }

    /**
     * Convert to date
     *
     * @param date input Date
     *
     * @return LocalTime parsed
     */
    static LocalTime toTime(final Date date) {
        /*
         * Default time should be 1899 of year
         * In java, when there is only time part, the Date should be:
         * Sun Dec 31 18:00:00 CST 1899
         *
         * In this situation, we should do some adjustment for variable date.
         * The dateTime here value is such as
         * 1899-12-31T18:05:43
         *
         * The time part is '18:05:43'
         *
         * There are 5 min 43 seconds adjust info ( Wrong )
         * Why ?
         *
         * I think the datetime is over the range in Java here
         */
        final Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        final Date normalized;
        if (1899 == cal.get(Calendar.YEAR)) {
            /*
             * One year could plus to 1901
             * All the valid date should be after "1899-12-30" instead of
             * 1) For '1900-01-01', this issue also will happen
             * 2) For '1901', it's more safer to extract time part ( To LocalTime )
             */

            cal.add(Calendar.YEAR, 2);
            normalized = cal.getTime();
            /*
             * Re-calculate local time here to be sure the result is as following
             * 1901-12-31T18:00
             *
             * The time part is '18:00:00'
             */
        } else {
            normalized = date;
        }
        final LocalDateTime datetime = toDateTime(normalized);
        return datetime.toLocalTime();
    }

    static LocalTime toTime(final Instant instant) {
        final LocalDateTime datetime = toDateTime(instant);
        return datetime.toLocalTime();
    }

    /**
     * Check whether it's valid
     *
     * @param literal input literal
     *
     * @return checked result whether it's valid date
     */
    static boolean isValid(final String literal) {
        final Date parsed = parse(literal);
        return null != parsed;
    }

    private static DateTimeFormatter analyzeFormatter(final String pattern, final String literal) {
        final DateTimeFormatter formatter;
        if (19 == pattern.length()) {
            // 2018-07-29T16:26:49格式的特殊处理
            formatter = DateTimeFormatter.ofPattern(pattern, Locale.US);
        } else if (23 == pattern.length()) {
            formatter = DateTimeFormatter.ofPattern(pattern, Locale.US);
        } else if (literal.contains("\\+") || literal.contains("\\-")) {
            formatter = DateTimeFormatter.ofPattern(Storage.ADJUST_TIME, Locale.US);
        } else {
            formatter = DateTimeFormatter.ofPattern(pattern, Locale.US);
        }
        return formatter;
    }

    static Date parse(final String literal) {
        if (Objects.isNull(literal)) {
            return null;
        } else {
            String target = literal;
            if (target.contains("T")) {
                target = target.replace('T', ' ');
            }
            final int length = target.length();
            final String pattern = Storage.PATTERNS_MAP.get(length);
            if (null != pattern) {
                final DateTimeFormatter formatter = analyzeFormatter(pattern, literal);
                final Date converted;
                if (10 == pattern.length()) {
                    final LocalDate date = parseEach(target, formatter, LocalDate::parse); // LocalDate.parse(target, formatter);
                    if (Objects.isNull(date)) {
                        converted = null;
                    } else {
                        final ZoneId zoneId = getAdjust(literal);
                        converted = parse(date, zoneId);
                    }
                } else if (15 > pattern.length()) {
                    final LocalTime time = parseEach(target, formatter, LocalTime::parse);
                    if (Objects.isNull(time)) {
                        converted = null;
                    } else {
                        final ZoneId zoneId = getAdjust(literal);
                        converted = parse(time, zoneId);
                    }
                } else {
                    final LocalDateTime datetime = parseEach(target, formatter, LocalDateTime::parse);
                    // final LocalDateTime datetime = LocalDateTime.parse(target, formatter);
                    if (Objects.isNull(datetime)) {
                        converted = null;
                    } else {
                        final ZoneId zoneId = getAdjust(literal);
                        converted = parse(datetime, zoneId);
                    }
                }
                return converted;
            } else {
                return parseFull(literal);
            }
        }
    }

    private static ZoneId getAdjust(final String literal) {
        if (literal.endsWith("Z")) {
            return ZoneId.from(ZoneOffset.UTC);
        } else {
            return ZoneId.systemDefault();
        }
    }

    /**
     * 「Not Recommend」directly for deep parsing
     *
     * @param literal Date/DateTime/Time literal value here.
     *
     * @return null or valid `java.util.Date` object
     */
    static Date parseFull(final String literal) {
        if (Objects.isNull(literal)) {
            return null;
        } else {
            // Datetime parsing
            final LocalDateTime datetime = toDateTime(literal);
            final ZoneId zoneId = getAdjust(literal);
            if (Objects.isNull(datetime)) {
                // Date parsing
                final LocalDate date = toDate(literal);
                if (Objects.isNull(date)) {
                    // Time parsing
                    final LocalTime time = toTime(literal);
                    return null == time ? null : parse(time);
                } else {
                    /*
                     * Not null datetime
                     */
                    return Date.from(date.atStartOfDay().atZone(zoneId).toInstant());
                }
            } else {
                /*
                 * Not null datetime
                 */
                return Date.from(datetime.atZone(zoneId).toInstant());
            }
        }
    }

    static void itDay(final LocalDate from, final LocalDate end,
                      final Consumer<Date> consumer) {
        LocalDate beginDay = from;
        do {
            consumer.accept(parse(beginDay));
            beginDay = beginDay.plusDays(1);
        } while (end.isAfter(beginDay) || end.isEqual(beginDay));
    }

    static void itWeek(final LocalDate from, final LocalDate end,
                       final Consumer<Date> consumer) {
        LocalDate beginDay = from;
        do {
            consumer.accept(parse(beginDay));
            beginDay = beginDay.plusWeeks(1);
        } while (end.isAfter(beginDay) || end.isEqual(beginDay));
    }

    static boolean isDuration(final LocalDateTime current, final LocalDateTime start, final LocalDateTime end) {
        final LocalDate currentDay = current.toLocalDate();
        final LocalDate startDay = start.toLocalDate();
        final LocalDate endDay = end.toLocalDate();
        return (currentDay.isEqual(startDay) || currentDay.isAfter(startDay))
            && (currentDay.isEqual(endDay) || currentDay.isBefore(endDay));
    }

    static boolean isSame(final Date left, final Date right) {
        // Compare year
        int leftVal = toItem(left, Calendar.YEAR);
        int rightVal = toItem(right, Calendar.YEAR);
        if (leftVal == rightVal) {
            // Compare month
            leftVal = toItem(left, Calendar.MONTH);
            rightVal = toItem(right, Calendar.MONTH);
            if (leftVal == rightVal) {
                // Compare day
                leftVal = toItem(left, Calendar.DAY_OF_MONTH);
                rightVal = toItem(right, Calendar.DAY_OF_MONTH);
                return leftVal == rightVal;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    static int toMonth(final String literal) {
        final Date date = parse(literal);
        return toItem(date, Calendar.MONTH);
    }

    static int toMonth(final Date date) {
        return toItem(date, Calendar.MONTH);
    }

    static int toYear(final Date date) {
        return toItem(date, Calendar.YEAR);
    }

    static int toYear(final String literal) {
        final Date date = parse(literal);
        return toItem(date, Calendar.YEAR);
    }

    private static int toItem(final Date date, final int flag) {
        final Calendar issue = Calendar.getInstance();
        issue.setTime(date);
        return issue.get(flag);
    }

    static Date parse(final LocalTime time) {
        return parse(time, ZoneId.systemDefault());
    }

    private static Date parse(final LocalTime time, final ZoneId zoneId) {
        final LocalDate date = LocalDate.now();
        final LocalDateTime datetime = LocalDateTime.of(date, time);
        return Date.from(datetime.atZone(zoneId).toInstant());
    }

    static Date parse(final LocalDateTime datetime) {
        return parse(datetime, ZoneId.systemDefault());
    }

    private static Date parse(final LocalDateTime datetime, final ZoneId zoneId) {
        return Date.from(datetime.atZone(zoneId).toInstant());
    }

    static Date parse(final LocalDate datetime) {
        return parse(datetime, ZoneId.systemDefault());
    }

    private static Date parse(final LocalDate datetime, final ZoneId zoneId) {
        return Date.from(datetime.atStartOfDay(zoneId).toInstant());
    }

    static LocalDateTime toDuration(final long millSeconds) {
        final Instant instant = Instant.ofEpochMilli(millSeconds);
        final OffsetDateTime offsetTime = instant.atOffset(ZoneOffset.UTC);
        return offsetTime.toLocalDateTime();
    }

    static String fromPattern(final TemporalAccessor date, final String pattern) {
        final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern);
        return formatter.format(date);
    }

    /**
     * /**
     * Symbol  Meaning                     Presentation      Examples
     * ------  -------                     ------------      -------
     * G       era                         text              AD; Anno Domini; A
     * u       year                        year              2004; 04
     * y       year-of-era                 year              2004; 04
     * D       day-of-year                 number            189
     * M/L     month-of-year               number/text       7; 07; Jul; July; J
     * d       day-of-month                number            10
     * <p>
     * Q/q     quarter-of-year             number/text       3; 03; Q3; 3rd quarter
     * Y       week-based-year             year              1996; 96
     * w       week-of-week-based-year     number            27
     * W       week-of-month               number            4
     * E       day-of-week                 text              Tue; Tuesday; T
     * e/c     localized day-of-week       number/text       2; 02; Tue; Tuesday; T
     * F       week-of-month               number            3
     * <p>
     * a       am-pm-of-day                text              PM
     * h       clock-hour-of-am-pm (1-12)  number            12
     * K       hour-of-am-pm (0-11)        number            0
     * k       clock-hour-of-am-pm (1-24)  number            0
     * <p>
     * H       hour-of-day (0-23)          number            0
     * m       minute-of-hour              number            30
     * s       second-of-minute            number            55
     * S       fraction-of-second          fraction          978
     * A       milli-of-day                number            1234
     * n       nano-of-second              number            987654321
     * N       nano-of-day                 number            1234000000
     * <p>
     * V       time-zone ID                zone-id           America/Los_Angeles; Z; -08:30
     * z       time-zone name              zone-name         Pacific Standard Time; PST
     * O       localized zone-offset       offset-O          GMT+8; GMT+08:00; UTC-08:00;
     * X       zone-offset 'Z' for zero    offset-X          Z; -08; -0830; -08:30; -083015; -08:30:15;
     * x       zone-offset                 offset-x          +0000; -08; -0830; -08:30; -083015; -08:30:15;
     * Z       zone-offset                 offset-Z          +0000; -0800; -08:00;
     * <p>
     * p       pad next                    pad modifier      1
     * <p>
     * '       escape for text             delimiter
     * ''      single quote                literal           '
     * [       optional section start
     * ]       optional section end
     * #       reserved for future use
     * {       reserved for future use
     * }       reserved for future use
     */
    private interface Iso {

        /**
         * '2011-12-03'
         */
        DateTimeFormatter LOCAL_DATE = DateTimeFormatter.ISO_LOCAL_DATE
            .withLocale(Locale.getDefault());
        /**
         * 2011-12-03+01:00
         */
        DateTimeFormatter OFFSET_DATE = DateTimeFormatter.ISO_OFFSET_DATE
            .withLocale(Locale.getDefault());
        /**
         * '2011-12-03'
         * '2011-12-03+01:00'
         */
        DateTimeFormatter DATE = DateTimeFormatter.ISO_DATE
            .withLocale(Locale.getDefault());
        /**
         * '10:15'
         * '10:15:30'
         */
        DateTimeFormatter LOCAL_TIME = DateTimeFormatter.ISO_LOCAL_TIME
            .withLocale(Locale.getDefault());
        /**
         * '10:15+01:00'
         * '10:15:30+01:00'.
         */
        DateTimeFormatter OFFSET_TIME = DateTimeFormatter.ISO_OFFSET_TIME
            .withLocale(Locale.getDefault());
        /**
         * '10:15'
         * '10:15:30'
         * '10:15:30+01:00'.
         */
        DateTimeFormatter TIME = DateTimeFormatter.ISO_TIME
            .withLocale(Locale.getDefault());
        /**
         * '2011-12-03T10:15:30'.
         */
        DateTimeFormatter LOCAL_DATE_TIME = DateTimeFormatter.ISO_LOCAL_DATE_TIME
            .withLocale(Locale.getDefault());
        /**
         * '2011-12-03T10:15:30+01:00'.
         */
        DateTimeFormatter OFFSET_DATE_TIME = DateTimeFormatter.ISO_OFFSET_DATE_TIME
            .withLocale(Locale.getDefault());
        /**
         * '2011-12-03T10:15:30+01:00[Europe/Paris]'
         */
        DateTimeFormatter ZONED_DATE_TIME = DateTimeFormatter.ISO_ZONED_DATE_TIME
            .withLocale(Locale.getDefault());
        /**
         * '2011-12-03T10:15:30',
         * '2011-12-03T10:15:30+01:00'
         * '2011-12-03T10:15:30+01:00[Europe/Paris]'.
         */
        DateTimeFormatter DATE_TIME = DateTimeFormatter.ISO_DATE_TIME
            .withLocale(Locale.getDefault());
        /**
         * '2012-337'
         */
        DateTimeFormatter ORDINAL_DATE = DateTimeFormatter.ISO_ORDINAL_DATE
            .withLocale(Locale.getDefault());
        /**
         * '2012-W48-6'.
         */
        DateTimeFormatter WEEK_DATE = DateTimeFormatter.ISO_WEEK_DATE
            .withLocale(Locale.getDefault());
        /**
         * 2011-12-03T10:15:30Z
         */
        DateTimeFormatter INSTANT = DateTimeFormatter.ISO_INSTANT
            .withLocale(Locale.getDefault());
        /**
         * 20111203
         */
        DateTimeFormatter BASIC_DATE = DateTimeFormatter.BASIC_ISO_DATE
            .withLocale(Locale.getDefault());
        /**
         * RFC-1123 date-time formatter, such as 'Tue, 3 Jun 2008 11:05:30 GMT'
         */
        DateTimeFormatter RFC1123_DATE_TIME = DateTimeFormatter.RFC_1123_DATE_TIME
            .withLocale(Locale.getDefault());
        /**
         * 2017-02-03T20:58:00.000Z
         * yyyy-MM-dd'T'HH:mm:ss.SSS'Z' 常用时间格式解析
         */
        DateTimeFormatter COMMON = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .withLocale(Locale.getDefault());
        /**
         * 2017-02-03 20:10:11
         */
        DateTimeFormatter READBALE = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withLocale(Locale.getDefault());
        /*
         * 1:00:00
         */
        DateTimeFormatter TIME_FIX = DateTimeFormatter.ofPattern("H:mm:ss");

    }

    private interface Storage {
        /**
         * Date Time patterns
         */
        ConcurrentMap<Integer, String> PATTERNS_MAP = new ConcurrentHashMap<Integer, String>() {
            {
                this.put(19, "yyyy-MM-dd HH:mm:ss");
                this.put(24, "yyyy-MM-dd HH:mm:ss.SSS'Z'");
                this.put(25, "yyyy-MM-dd HH:mm:ss.SSS+'z'");
                this.put(23, "yyyy-MM-dd HH:mm:ss.SSS");
                this.put(28, "EEE MMM dd HH:mm:ss 'CST' yyyy");
                this.put(12, "HH:mm:ss.SSS");
                this.put(10, "yyyy-MM-dd");
                this.put(8, "HH:mm:ss");
            }
        };

        String ADJUST_TIME = "yyyy-MM-dd HH:mm:ss.SSS'z'";
    }
}
