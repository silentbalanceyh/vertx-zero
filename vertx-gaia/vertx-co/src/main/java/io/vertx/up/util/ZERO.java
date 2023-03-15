package io.vertx.up.util;

import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * Support values
 * event-bus,
 * up.god.file,
 * json,
 * http,
 * env,
 * sys,
 * directory
 */
enum StoreType {
    JSON("json"),
    FILE("file"),
    HTTP("http"),
    ENV("env"),
    SYS("sys"),
    DIRECTORY("directory"),
    EVENT_BUS("event-but");

    private final transient String literal;

    StoreType(final String literal) {
        this.literal = literal;
    }

    public String key() {
        return this.literal;
    }
}

enum StoreFormat {
    YAML("yaml"),
    PROP("properties");

    private final transient String literal;

    StoreFormat(final String literal) {
        this.literal = literal;
    }

    public String key() {
        return this.literal;
    }
}

enum StoreConfig {
    PATH("path");

    private final transient String literal;

    StoreConfig(final String literal) {
        this.literal = literal;
    }

    public String key() {
        return this.literal;
    }
}

interface Storage {
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

interface Info {
    String INF_PATH = "「I/O」The system class Stream try to data from {0}, got stream: {1}.";
    String INF_CUR = "「I/O」Current path is scanned by the system, up.god.file existing ? {0}.";
    String INF_APATH = "「I/O」Absolute path is hitted: {0}.";

    String __FILE_ROOT = "「DevOps」root = {0}, file = {1}";
    String __FILE_INPUT_STREAM = "「DevOps」\t\t{0} 1. new FileInputStream(File)";
    String __RESOURCE_AS_STREAM = "「DevOps」\t\t{0} 2. clazz.getResourceAsStream(String)";
    String __CLASS_LOADER = "「DevOps」\t\t{0} 3. Thread.currentThread().getContextClassLoader()";
    String __CLASS_LOADER_STREAM = "「DevOps」\t\t{0} 4. Stream.class.getResourceAsStream(String)";
    String __CLASS_LOADER_SYSTEM = "「DevOps」\t\t{0} 5. ClassLoader.getSystemResourceAsStream(String)";
    String __JAR_RESOURCE = "「DevOps」\t\t{0} 6. Read from jar file";

    String MATH_NOT_MATCH = "( Numeric ) The system could not match current type {0} to do sum";

    String IO_CMD_RM = "I/O Command: `rm -rf {0}`";
    String IO_CMD_MKDIR = "I/O Command: `mkdir -P {0}`";
    String IO_CMD_MOVE = "I/O Command: `mv {0} {1}`";
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

interface Iso {
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
