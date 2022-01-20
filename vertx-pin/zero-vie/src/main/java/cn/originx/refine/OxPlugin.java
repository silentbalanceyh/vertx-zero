package cn.originx.refine;

import cn.originx.uca.log.Ko;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.modeling.data.DataGroup;
import io.vertx.tp.optic.DS;
import io.vertx.tp.optic.Pocket;
import io.vertx.tp.optic.plugin.AspectPlugin;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * ## 插件工具
 *
 * ### 1. 基本介绍
 *
 * Ox平台专用插件处理工具。
 *
 * ### 2. 插件表
 *
 * - 数据连接池插件：{@link DataPool}类型。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class OxPlugin {

    /*
     * 私有构造函数（工具类转换）
     */
    private OxPlugin() {
    }

    /**
     * 根据应用标识符读取数据连接池（{@link DataPool}类型）。
     *
     * @param sigma {@link String} 应用统一标识符
     *
     * @return {@link DataPool}连接池对象
     */
    private static DataPool pluginDs(final String sigma) {
        if (Ut.isNil(sigma)) {
            return null;
        } else {
            final DS ds = Pocket.lookup(DS.class);
            if (Objects.isNull(ds)) {
                return null;
            } else {
                return ds.switchDs(sigma);
            }
        }
    }

    /**
     * 「Function」插件类安全执行器，执行内部`executor`，若有异常则直接调用内部日志记录。
     *
     * @param clazz    {@link Class} 调用该方法的对象类
     * @param input    `executor`的输入
     * @param executor {@link Function} 外部传入执行器
     * @param <T>      `executor`执行器处理类型
     *
     * @return `executor`执行结果
     */
    static <T> Future<T> runSafe(final Class<?> clazz, final T input, final Function<T, Future<T>> executor) {
        try {
            return executor.apply(input);
        } catch (final Throwable ex) {
            return runSafe(clazz, input, ex);
        }
    }

    /**
     * 「Supplier」插件类安全执行器，执行内部`executor`，若有异常则直接调用内部日志记录。
     *
     * @param clazz    {@link Class} 调用该方法的对象类
     * @param input    `executor`的输入
     * @param supplier {@link Supplier} 外部传入数据构造器
     * @param <T>      `executor`执行器处理类型
     *
     * @return `executor`执行结果
     */
    static <T> Future<T> runSafe(final Class<?> clazz, final T input, final Supplier<T> supplier) {
        try {
            return Ux.future(supplier.get())
                /* 内置处理 */
                .compose(indexed -> Ux.future(input));
        } catch (final Throwable ex) {
            return runSafe(clazz, input, ex);
        }
    }

    /**
     * 「内部调用」异常执行器。
     *
     * @param clazz {@link Class} 调用该方法的对象类
     * @param input `executor`的输入
     * @param ex    {@link Throwable} 异常信息
     * @param <T>   返回的真实数据类型
     *
     * @return {@link Future}
     */
    private static <T> Future<T> runSafe(final Class<?> clazz, final T input, final Throwable ex) {
        // ex.printStackTrace();
        final Annal logger = Annal.get(clazz);
        OxLog.warn(logger, "plugin", ex.getMessage());
        /* 集成专用信息，这里不打印 ex 的 Stack 信息 */
        Ko.integration(clazz, null, ex);
        return Ux.future(input);
    }

    /**
     * 数据源执行器，{@link DataPool}数据源运行主流程。
     *
     * @param sigma    {@link String} 应用统一标识符
     * @param supplier {@link Supplier} 外部数据读取器
     * @param executor {@link Function} 函数执行器
     * @param <T>      最终执行后返回的数据类型
     *
     * @return {@link Future}
     */
    static <T> Future<T> runDs(final String sigma, final Supplier<T> supplier,
                               final Function<DataPool, Future<T>> executor) {
        final DataPool ds = pluginDs(sigma);
        if (Objects.isNull(ds)) {
            return Ux.future(supplier.get());
        } else {
            return executor.apply(ds);
        }
    }

    /**
     * 分组运行器，将数据分组后执行分组过后的运行。
     *
     * - 每一组有相同的模型定义{@link DataAtom}。
     * - 每一组有相同的数据输入{@link JsonArray}
     *
     * @param groupSet {@link Set}<{@link DataGroup}> 分组集合
     * @param consumer {@link BiFunction} 双输入函数
     *
     * @return {@link Future}<{@link JsonArray}>
     */
    static Future<JsonArray> runGroup(final Set<DataGroup> groupSet,
                                      final BiFunction<JsonArray, DataAtom, Future<JsonArray>> consumer) {
        final List<Future<JsonArray>> futures = new ArrayList<>();
        groupSet.forEach(group -> futures.add(consumer.apply(group.data(), group.atom())));
        return Ux.thenCombineArray(futures);
    }

    /**
     * 切面执行器，执行`before -> executor -> after`流程处理数据记录。
     *
     * @param input    {@link JsonObject} 输入数据记录
     * @param config   {@link JsonObject} 配置数据
     * @param supplier {@link Supplier} 插件提取器，提取{@link AspectPlugin}插件
     * @param atom     {@link DataAtom} 模型定义
     * @param executor {@link Function} 函数执行器
     *
     * @return {@link Future}<{@link JsonObject}>返回执行的最终结果
     */
    static Future<JsonObject> runAop(final JsonObject input, final JsonObject config,
                                     final Supplier<AspectPlugin> supplier, final DataAtom atom,
                                     final Function<JsonObject, Future<JsonObject>> executor) {
        /*
         * 输入处理 input
         */
        if (Ut.isNil(input)) {
            return Ux.future(new JsonObject());
        } else {
            final AspectPlugin plugin = supplier.get();
            /*
             * 插件是否存在
             */
            if (Objects.isNull(plugin)) {
                /*
                 * 默认的主逻辑
                 */
                return executor.apply(input);
            } else {
                /*
                 * 插件不为空
                 * - beforeAsync
                 * - 主逻辑
                 * - afterAsync
                 */
                plugin.bind(atom);
                OxLog.infoHub(OxPlugin.class, "插件选择：{0}", plugin.getClass());
                return plugin.beforeAsync(input, config)
                    /*
                     * 主逻辑
                     */
                    .compose(executor)
                    /*
                     * 后置逻辑
                     */
                    .compose(done -> plugin.afterAsync(done, config));
            }
        }
    }
}
