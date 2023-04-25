package cn.originx.refine;

import io.horizon.eon.em.ChangeFlag;
import io.vertx.core.json.JsonArray;
import io.vertx.up.log.Annal;
import io.vertx.up.log.Log;

import java.util.concurrent.ConcurrentMap;

/**
 * ## 「内部」日志器
 *
 * ### 1. 基本介绍
 *
 * Ox平台专用内部日志器（部分方法内部调用）
 *
 * ### 2. 日志器种类
 *
 * - Hub日志器
 * - Shell日志器
 * - Compare比较日志器
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class OxLog {
    /*
     * 私有构造函数（工具类转换）
     */
    private OxLog() {
    }

    /**
     * INFO日志处理
     *
     * @param logger  {@link Annal} Zero专用日志器
     * @param flag    {@link String} 业务标记
     * @param pattern {@link String} 日志信息模式
     * @param args    {@link Object...} 可变参数
     */
    static void info(final Annal logger,
                     final String flag, final String pattern, final Object... args) {
        logger.info(Log.green("Προέλευση Χ") + " ( " + flag + " ) " + pattern, args);
    }

    /**
     * DEBUG日志处理
     *
     * @param logger  {@link Annal} Zero专用日志器
     * @param flag    {@link String} 业务标记
     * @param pattern {@link String} 日志信息模式
     * @param args    {@link Object...} 可变参数
     */
    static void debug(final Annal logger,
                      final String flag, final String pattern, final Object... args) {
        logger.debug(Log.green("Προέλευση Χ") + " ( " + flag + " ) " + pattern, args);
    }

    /**
     * WARN日志处理
     *
     * @param logger  {@link Annal} Zero专用日志器
     * @param flag    {@link String} 业务标记
     * @param pattern {@link String} 日志信息模式
     * @param args    {@link Object...} 可变参数
     */
    static void warn(final Annal logger,
                     final String flag, final String pattern, final Object... args) {
        logger.warn(Log.green("Προέλευση Χ") + " ( " + flag + " ) " + pattern, args);
    }

    /**
     * Info级别，Hub专用日志器（内部和外部）
     *
     * @param logger  {@link Annal} Zero专用日志器
     * @param pattern {@link String} 日志信息模式
     * @param args    {@link Object...} 可变参数
     */
    static void infoHub(final Annal logger, final String pattern, final Object... args) {
        OxLog.info(logger, "Hub", pattern, args);
    }

    /**
     * Info级别，Hub专用日志器（内部和外部）
     *
     * @param clazz   {@link Class} 调用日志器的类
     * @param pattern {@link String} 日志信息模式
     * @param args    {@link Object...} 可变参数
     */
    static void infoHub(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        infoHub(logger, pattern, args);
    }

    /**
     * Warn级别，Hub专用日志器（内部和外部）
     *
     * @param logger  {@link Annal} Zero专用日志器
     * @param pattern {@link String} 日志信息模式
     * @param args    {@link Object...} 可变参数
     */
    static void warnHub(final Annal logger, final String pattern, final Object... args) {
        OxLog.warn(logger, "Hub", pattern, args);
    }

    /**
     * Info级别，Shell专用日志器（内部和外部）
     *
     * @param logger  {@link Annal} Zero专用日志器
     * @param pattern {@link String} 日志信息模式
     * @param args    {@link Object...} 可变参数
     */
    static void warnShell(final Annal logger, final String pattern, final Object... args) {
        OxLog.warn(logger, "Shell", pattern, args);
    }

    /**
     * Warn级别，Hub专用日志器（内部和外部）
     *
     * @param clazz   {@link Class} 调用日志器的类
     * @param pattern {@link String} 日志信息模式
     * @param args    {@link Object...} 可变参数
     */
    static void warnHub(final Class<?> clazz, final String pattern, final Object... args) {
        final Annal logger = Annal.get(clazz);
        warnHub(logger, pattern, args);
    }

    /**
     * Info级别，比较结果日志
     *
     * @param clazz {@link Class} 调用日志器的类
     * @param map   比对结果表
     */
    static void infoCompared(final Class<?> clazz, final ConcurrentMap<ChangeFlag, JsonArray> map) {
        final Annal logger = Annal.get(clazz);
        OxLog.info(logger, "CRT", "Report Start ----- ");
        map.forEach((type, data) -> OxLog.info(logger, "CRT", "Type = {0}, Size = {2}, Data = {1}",
            type, data.encode(), String.valueOf(data.size())));
        OxLog.info(logger, "CRT", "Report End ----- ");
    }
}
