package io.horizon.util;

import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * High Metadata Service 高阶元数据工具类
 * 1）动参版本
 * 2）对象版本
 * 上述两个版本直接在 HH 类中铺开写逻辑
 * 包域中只包含基础逻辑，没有其他内容
 *
 * @author lang : 2023/4/27
 */
public class HUt extends _Value {
    /*
     * 构造函数只能被子类调用，不可以在其他地方调用，所以此处是工具类的重新设计，原理
     * 1. Java 中的静态方法可以从父类继承，但是不可以从接口初获得
     * 2. 虽然继承之后子类定义的方法只是隐藏了父类的，但不会出现Overwrite
     * 3. 但从代码去重上是一个非常好的策略
     *
     * 父类：protected 的默认构造函数 + 静态类
     * 子类：private 的构造函数 + final 不可继承类
     * 这种类最终会导致二者合并形成统一方法区间
     */
    protected HUt() {
    }

    // ---------------- 格式化函数

    /**
     * 使用 MessageFormat 进行格式化，参数
     * {0} {1} {2}
     * 使用 Slf4j 进行格式化，参数
     * {} {} {}
     *
     * @param pattern 格式化模板
     * @param args    参数
     *
     * @return 格式化后的字符串
     */
    public static String fromMessage(final String pattern, final Object... args) {
        return HFormat.fromMessage(pattern, args);
    }

    /**
     * 加粗版本的格式化，用于日志部分的专用处理
     *
     * @param pattern 格式化模板
     * @param args    参数
     *
     * @return 格式化后的字符串
     */
    public static String fromMessageB(final String pattern, final Object... args) {
        return HFormat.fromMessageB(pattern, args);
    }

    /**
     * 异常信息的格式化，用于各种异常信息模板化输出专用处理，该格式化函数是根据
     * 异常构造参数调用，所以通常在抽象异常的构造函数中使用而不能在其他位置直接
     * 使用，其他位置时候格式化之后的结果可能有问题
     *
     * @param pattern 格式化模板
     * @param clazz   触发异常调用类
     * @param code    异常代码
     * @param args    参数
     *
     * @return 格式化后的字符串
     */
    public static String fromError(final String pattern, final Class<?> clazz,
                                   final int code, final Object... args) {
        return HError.fromError(pattern, clazz, code, args);
    }

    /**
     * 异常信息的格式化，用于各种异常信息模板化输出专用处理，该格式化函数是根据
     * 异常 code 从文件中提取，
     * vertx zero 中直接从 vertx-readable.yml 中提取
     *
     * @param code 异常代码
     *
     * @return 格式化后的字符串
     */
    public static String fromReadable(final int code, final Object... args) {
        return HError.fromReadable(code, args);
    }

    // ---------------- 池化函数
    public static <V> V poolThread(final Map<String, V> pool, final Supplier<V> poolFn, final String key) {
        return HPool.poolThread(pool, poolFn, key);
    }

    public static <V> V poolThread(final Map<String, V> pool, final Supplier<V> poolFn) {
        return HPool.poolThread(pool, poolFn, null);
    }

    public static <K, V> V pool(final Map<K, V> pool, final K key, final Supplier<V> poolFn) {
        return HPool.pool(pool, key, poolFn);
    }


    // ---------------- 转换函数：to - 转成 / from - 转入

    // ---------------- Jvm强化函数
    public static void requireNonNull(final Object... args) {
        Arrays.stream(args).forEach(Objects::requireNonNull);
    }
}
