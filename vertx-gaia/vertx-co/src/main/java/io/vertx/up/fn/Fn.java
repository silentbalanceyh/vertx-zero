package io.vertx.up.fn;

import io.horizon.eon.VMessage;
import io.horizon.exception.AbstractException;
import io.horizon.exception.BootingException;
import io.horizon.exception.ProgramException;
import io.horizon.exception.WebException;
import io.horizon.fn.ErrorSupplier;
import io.horizon.fn.ProgramActuator;
import io.horizon.uca.log.Annal;
import io.vertx.core.Future;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Unique interface to call function in zero framework.
 * 基本格式：
 * --- [] 代表集合，
 * --- () 代表异步
 * 基础标记位处理：
 * 1. 参数类型标记位：
 * --- J: JsonObject
 * --- A: JsonArray
 * --- T: 泛型T
 * --- B: Boolean
 * --- M: Map哈希表
 * --- L: 列表 List
 * --- G: 分组专用
 * 2. 函数标记位（按参数可重载）
 * --- Supplier:    Or
 * --- Function:    Of
 * --- Predicate:   x
 * --- Consumer:    At
 * --- Actuator:    Act
 * 3. 函数异常标记位矩阵：
 * ------------ |  Throwable  |  Exception  |  ZeroException  |  ZeroRunException
 * - Supplier - |
 * - Function - |
 * - Consumer - |
 * - Predicate  |
 */
@SuppressWarnings("all")
public final class Fn extends _Out {
    private Fn() {
    }

    // 安全执行确认：Supplier<T> -> Consumer<T>
    public static <T> void monad(final Supplier<T> supplier, final Consumer<T> consumer) {
        Monad.safeT(supplier, consumer);
    }

    public static <T> T monad(final ErrorSupplier<T> supplier, final T defaultValue) {
        return Monad.monad(supplier, defaultValue);
    }

    public static <T> Future<T> monadAsync(final ErrorSupplier<Future<T>> supplier, final T defaultValue) {
        return Monad.monadAsync(supplier, defaultValue);
    }

    // ---------------------------------------------------- 响应函数 ----------------------------------------------------
    // ------ ZeroException out
    public static void outZero(final boolean condition, final Annal logger, final Class<? extends ProgramException> zeroClass, final Object... args) throws ProgramException {
        if (condition) {
            Warning.outZero(logger, zeroClass, args);
        }
    }

    // ------ ZeroException to RunZeroException out
    public static void outUp(final ProgramActuator actuator, final Annal logger) {
        Wait.wrapper(logger, actuator);
    }

    // ------ RunZeroException out
    /* Old Style */
    public static void outUp(final boolean condition, final Annal logger, final Class<? extends AbstractException> upClass, final Object... args) {
        if (condition) {
            Warning.outUp(logger, upClass, args);
        }
    }

    /* New Style */
    public static void outUp(final boolean condition, final Class<? extends BootingException> upClass, final Object... args) {
        if (condition) {
            Warning.outUp(upClass, args);
        }
    }

    /**
     * New structure for exception out ( RuntimeException )
     * UpException | WebException supported
     * --
     * outUp + outWeb
     *
     * @param condition The condition for throwing
     * @param clazz     The exception clazz here ( because all the exception class first argument type is Class<?>, it means
     *                  you can initialize logger inner this method instead of input
     * @param args      dynamic objects for exception
     */
    public static void out(final boolean condition, final Class<?> clazz, final Object... args) {
        if (condition) {
            Warning.out(clazz, args);
        }
    }

    // ------ WebException out
    /* Old Style */
    public static void outWeb(final boolean condition, final Annal logger, final Class<? extends WebException> webClass, final Object... args) {
        if (condition) {
            Warning.outWeb(logger, webClass, args);
        }
    }

    /* New Style */
    public static void outWeb(final boolean condition, final Class<? extends WebException> webClass, final Object... args) {
        if (condition) {
            Warning.outWeb(webClass, args);
        }
    }

    /*
     * Program Used:
     * outOr / outQr
     * 1. This point should be first line of one method:
     *    if(condition){
     *        throw out exception of `_412NullValueException.class`
     *    }
     * 2. This point should be QR result checking after Future<T>
     *    Future.compose(record -> {
     *        if(Objects.isNull(record)){
     *             throw out exception of `_412NullValueException.class`
     *        }
     *    })
     *
     * The default message:
     * 1. Or -> [ Program ] Null Input
     * 2. Qr -> [ Program ] Null Record in database
     * */
    public static <T> void outOr(final T condition, final Class<?> clazz, final String message) {
        Warning.outOr(condition, clazz, message);
    }

    public static <T> void outOr(final T condition, final Class<?> clazz) {
        outOr(condition, clazz, VMessage.Fn.PROGRAM_NULL);
    }

    public static <T> void outQr(final T condition, final Class<?> clazz) {
        outOr(condition, clazz, VMessage.Fn.PROGRAM_QR);
    }

    // ------ Specification for JsonFormat
    public static <T> T runOut(final Supplier<T> supplier, final Class<? extends AbstractException> runCls, final Object... args) {
        return Warning.execRun(supplier, runCls, args);
    }

}
