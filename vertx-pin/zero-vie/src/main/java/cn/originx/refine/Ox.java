package cn.originx.refine;

import cn.originx.cv.OxCv;
import cn.originx.cv.em.TypeLog;
import cn.originx.uca.code.Numeration;
import cn.vertxup.ambient.domain.tables.pojos.XActivity;
import cn.vertxup.ambient.service.application.AppStub;
import cn.vertxup.ambient.service.application.InitStub;
import cn.vertxup.workflow.domain.tables.pojos.WTodo;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.tp.atom.modeling.data.DataGroup;
import io.vertx.tp.optic.plugin.AfterPlugin;
import io.vertx.tp.optic.plugin.AspectPlugin;
import io.vertx.tp.plugin.database.DataPool;
import io.vertx.tp.plugin.elasticsearch.ElasticSearchClient;
import io.vertx.up.commune.Envelop;
import io.vertx.up.commune.Record;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.config.Integration;
import io.vertx.up.eon.em.ChangeFlag;
import io.vertx.up.eon.em.Environment;
import io.vertx.up.experiment.mixture.HDao;
import io.vertx.up.log.Annal;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * ## 统一工具类
 *
 * 可在环境中直接使用`Ox.xxx`的方式调用工具类。
 *
 * ### 1. 内置工具类列表
 *
 * |类名|说明|
 * |:---|:---|
 * |{@link OxAmbient}|环境工具|
 * |{@link OxCompareUi}|界面比对工具|
 * |{@link OxConfig}|环境配置工具|
 * |{@link OxConsole}|命令行工具|
 * |{@link OxTo}|数据工具|
 * |{@link OxField}|字段工具|
 * |{@link OxJson}|Json处理工具|
 * |{@link OxLog}|日志器|
 * |{@link OxMocker}|数据模拟工具|
 * |{@link OxPlugin}|插件工具|
 * |{@link OxView}|视图工具|
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
@SuppressWarnings("all")
public final class Ox {
    private static ConcurrentMap<Class<?>, String> NUM_MAP = new ConcurrentHashMap<Class<?>, String>() {
        {
            this.put(XActivity.class, "NUM.ACTIVITY");
            this.put(WTodo.class, "NUM.TODO");
        }
    };

    /*
     * 私有构造函数（工具类转换）
     */
    private Ox() {
    }

    public static void numerationStd() {
        Numeration.preprocess(NUM_MAP);
    }

    public static Future<Record> viGet(final DataAtom atom, final String identifier,
                                       final String field, final Object value) {
        return OxLinker.viGet(atom, identifier, field, value);
    }

    public static Future<Record[]> viGet(final DataAtom atom, final String identifier,
                                         final String field, final JsonArray values) {
        return OxLinker.viGet(atom, identifier, field, values);
    }

    public static Future<ConcurrentMap<String, Record>> viGetMap(final DataAtom atom, final String identifier,
                                                                 final String field, final JsonArray values) {
        return OxLinker.viGetMap(atom, identifier, field, values, field);
    }

    public static Future<ConcurrentMap<String, Record>> viGetMap(final DataAtom atom, final String identifier,
                                                                 final String field, final JsonArray values,
                                                                 final String fieldGroup) {
        return OxLinker.viGetMap(atom, identifier, field, values, fieldGroup);
    }

    /**
     * 「After」读取<value>plugin.ticket</value>值提取ITSM开单专用插件。
     *
     * @param options {@link JsonObject} ServiceConfig配置原始数据
     *
     * @return {@link AfterPlugin}
     */
    public static AfterPlugin pluginTicket(final JsonObject options) {
        return Ut.plugin(options, OxCv.PLUGIN_TICKET, AfterPlugin.class);
    }

    /**
     * 「Around」读取<value>plugin.activity</value>值构造历史记录生成专用插件。
     *
     * @param options {@link JsonObject} ServiceConfig配置原始数据
     *
     * @return {@link AspectPlugin}
     */
    public static AspectPlugin pluginActivity(final JsonObject options) {
        return Ut.plugin(options, OxCv.PLUGIN_ACTIVITY, AspectPlugin.class);
    }

    /**
     * 「Around」读取<value>plugin.todo</value>值构造待办确认单生成专用插件。
     *
     * @param options {@link JsonObject} ServiceConfig配置原始数据
     *
     * @return {@link AspectPlugin}
     */
    public static AspectPlugin pluginTodo(final JsonObject options) {
        return Ut.plugin(options, OxCv.PLUGIN_TODO, AspectPlugin.class);
    }

    /**
     * 「Around」读取<value>plugin.component</value>值构造标准的AOP插件，执行Before和After两个核心流程
     *
     * - <value>plugin.component.before</value>：前置插件表（数组）
     * - <value>plugin.component.after</value>：后置插件表（数组）
     *
     * @param options {@link JsonObject} ServiceConfig配置原始数据
     *
     * @return {@link AspectPlugin}
     */
    public static AspectPlugin pluginComponent(final JsonObject options) {
        return Ut.plugin(options, OxCv.PLUGIN_COMPONENT, AspectPlugin.class);
    }

    /**
     * 构造标识规则选择器，读取插件<value>plugin.identifier</value>值提取标识规则选择器，使用默认配置项`options`。
     *
     * @param atom {@link DataAtom} 模型定义
     *
     * @return {@link Identity} 构造好的标识规则选择器
     */
    public static Identity pluginIdentifier(final DataAtom atom) {
        return OxConfig.toIdentity(atom, OxConfig.toOptions());
    }

    /**
     * 构造初始化专用接口，用于初始化某个`X_APP`的应用配置。
     *
     * - 初始化接口{@link InitStub}，执行应用初始化。
     * - 应用访问接口{@link AppStub}。
     *
     * @return {@link InitStub}
     */
    public static InitStub pluginInitializer() {
        return OxAmbient.pluginInitializer();
    }

    /**
     * 读取前置插件链：`plugin.component.before`。
     *
     * @param options {@link JsonObject} 服务配置数据，对应ServiceConfig字段
     *
     * @return {@link List}<{@link Class}>
     */
    public static List<Class<?>> pluginBefore(final JsonObject options) {
        return OxAmbient.pluginClass(options, OxCv.PLUGIN_COMPONENT_BEFORE);
    }

    /**
     * 读取后置插件链：`plugin.component.after`。
     *
     * @param options {@link JsonObject} 服务配置数据，对应ServiceConfig字段
     *
     * @return {@link List}<{@link Class}>
     */
    public static List<Class<?>> pluginAfter(final JsonObject options) {
        return OxAmbient.pluginClass(options, OxCv.PLUGIN_COMPONENT_AFTER);
    }

    /**
     * 读取后置异步插件：`plugin.component.job`。
     *
     * @param options {@link JsonObject} 服务配置数据，对应ServiceConfig字段
     *
     * @return {@link List}<{@link Class}>
     */
    public static List<Class<?>> pluginJob(final JsonObject options) {
        return OxAmbient.pluginClass(options, OxCv.PLUGIN_COMPONENT_JOB);
    }

    /**
     * 从原始配置数据中读取`plugin.config`节点，根据传入的`clazz`读取和当前插件相关的配置信息。
     *
     * - 从原始配置中读取基础配置，上层传入的`options`数据。
     * - 提取`plugin.config`中和`clazz`绑定的配置数据。
     * - 合并二者所有配置数据构造最终配置数据。
     *
     * @param clazz   {@link Class} 执行初始化的类信息，反射调用
     * @param options {@link JsonObject} 服务配置数据，对应ServiceConfig字段
     *
     * @return {@link JsonObject}
     */
    public static JsonObject pluginOptions(final Class<?> clazz, final JsonObject options) {
        return OxAmbient.pluginOptions(clazz, options);
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
     * @return 返回执行的最终结果{@link io.vertx.core.Future}<{@link JsonObject}>
     */
    public static Future<JsonObject> runAop(final JsonObject input, final JsonObject config,
                                            final Supplier<AspectPlugin> supplier, final DataAtom atom,
                                            final Function<JsonObject, Future<JsonObject>> executor) {
        return OxPlugin.runAop(input, config, supplier, atom, executor);
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
    public static <T> Future<T> runDs(final String sigma, final Supplier<T> supplier,
                                      final Function<DataPool, Future<T>> executor) {
        return OxPlugin.runDs(sigma, supplier, executor);
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
    public static Future<JsonArray> runGroup(final Set<DataGroup> groupSet,
                                             final BiFunction<JsonArray, DataAtom, Future<JsonArray>> consumer) {
        return OxPlugin.runGroup(groupSet, consumer);
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
    public static <T> Future<T> runSafe(final Class<?> clazz, final T input, final Function<T, Future<T>> executor) {
        return OxPlugin.runSafe(clazz, input, executor);
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
    public static <T> Future<T> runSafe(final Class<?> clazz, final T input, final Supplier<T> supplier) {
        return OxPlugin.runSafe(clazz, input, supplier);
    }

    /**
     * 命令执行器，批量调用操作系统中的命令提示符运行操作命令。
     *
     * @param commands {@link List}<{@link String}> 待执行的命令清单
     */
    public static void runCmd(final List<String> commands) {
        OxConsole.runCmd(commands);
    }

    /**
     * 「Async」ElasticSearch异步执行器，重建索引。
     *
     * @param atom {@link DataAtom} 模型定义
     *
     * @return {@link Future}<{@link ElasticSearchClient}>
     */
    public static Future<ElasticSearchClient> runEs(final DataAtom atom) {
        return OxConsole.runEs(atom);
    }

    /**
     * {@link JsonObject}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link JsonObject} 返回最终数据记录
     */
    public static JsonObject mockJ(final Integration integration, final String file, final Supplier<JsonObject> supplier) {
        return OxMocker.mockJ(integration, file, supplier);
    }

    /**
     * {@link JsonArray}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link JsonArray} 返回最终数据记录
     */
    public static JsonArray mockA(final Integration integration, final String file, final Supplier<JsonArray> supplier) {
        return OxMocker.mockA(integration, file, supplier);
    }

    /**
     * {@link String}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link String} 返回最终数据记录
     */
    public static String mockS(final Integration integration, final String file, final Supplier<String> supplier) {
        return OxMocker.mockS(integration, file, supplier);
    }

    /**
     * 「Async」{@link JsonObject}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link JsonObject} 返回最终数据记录
     */
    public static Future<JsonObject> mockAsyncJ(final Integration integration, final String file, final Supplier<Future<JsonObject>> supplier) {
        return OxMocker.mockAsyncJ(integration, file, supplier);
    }

    /**
     * 「Async」{@link JsonArray}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link JsonArray} 返回最终数据记录
     */
    public static Future<JsonArray> mockAsyncA(final Integration integration, final String file, final Supplier<Future<JsonArray>> supplier) {
        return OxMocker.mockAsyncA(integration, file, supplier);
    }

    /**
     * 「Async」{@link String}模拟数据
     *
     * @param integration {@link Integration} 集成配置对象
     * @param file        {@link String} 模拟数据读取文件
     * @param supplier    {@link Supplier} 真实执行器
     *
     * @return {@link String} 返回最终数据记录
     */
    public static Future<String> mockAsyncS(final Integration integration, final String file, final Supplier<Future<String>> supplier) {
        return OxMocker.mockAsyncS(integration, file, supplier);
    }

    /**
     * 基本平台级忽略字段
     *
     * @param atom {@link DataAtom}
     *
     * @return {@link Set}
     */
    public static Set<String> ignorePure(final DataAtom atom) {
        return OxField.ignorePure(atom);
    }

    /**
     * syncIn = false 的忽略字段
     *
     * @param atom {@link DataAtom}
     *
     * @return {@link Set}
     */
    public static Set<String> ignoreIn(final DataAtom atom) {
        return OxField.ignoreIn(atom);
    }

    /**
     * 从第三方拉取数据的忽略字段
     *
     * @param atom {@link DataAtom}
     *
     * @return {@link Set}
     */
    public static Set<String> ignorePull(final DataAtom atom) {
        return OxField.ignorePull(atom);
    }

    /**
     * 开放API部分的忽略字段
     *
     * @param atom {@link DataAtom}
     *
     * @return {@link Set}
     */
    public static Set<String> ignoreApi(final DataAtom atom) {
        return OxField.ignoreApi(atom);
    }

    /**
     * 推送过程中的忽略字段
     *
     * @param atom {@link DataAtom}
     *
     * @return {@link Set}
     */
    public static Set<String> ignorePush(final DataAtom atom) {
        return OxField.ignorePush(atom);
    }

    /**
     * 将输入数据构造成集合{@link Set}<{@link DataGroup}>封装对象。
     *
     * {@link DataGroup}中包含了三个核心数据：
     *
     * - 数据部分：{@link JsonArray}数据信息。
     * - identifier：{@link String}模型标识符。
     * - 元数据部分：{@link DataAtom} 模型定义对象。
     *
     * @param atom  {@link DataAtom} 模型定义
     * @param input {@link JsonArray} 数据信息
     *
     * @return {@link Set}<{@link DataGroup}>
     */
    public static Set<DataGroup> toGroup(final DataAtom atom, final JsonArray input) {
        final Set<DataGroup> set = new HashSet<>();
        set.add(DataGroup.create(atom).add(input));
        return set;
    }

    /**
     * 根据传入路径执行路径解析工作，解析三种不同环境的路径。
     *
     * - Development：开发环境
     * - Production：生产环境
     * - Mockito：模拟测试环境（integration中的debug = true）
     *
     * @param path        {@link String} 构造的环境读取数据专用路径
     * @param environment {@link Environment} 传入环境数据
     *
     * @return {@link String} 返回不同环境处理过的绝对路径信息
     */
    public static String toRoot(final String path, final Environment environment) {
        return OxAmbient.resolve(path, environment);
    }

    /**
     * 根据传入路径执行解析工作，解析`Development`开发环境的路径。
     *
     * @param path {@link String} 构造的环境读取数据专用路径
     *
     * @return {@link String} 返回不同环境处理过的绝对路径信息
     */
    public static String toRootDev(final String path) {
        return OxAmbient.resolve(path, Environment.Development);
    }

    /**
     * 「Async」元数据执行器
     *
     * @param input 输入的最终响应数据，内部调用`metadata(JsonArray)`方法执行转换。
     *
     * @return {@link Future}<{@link JsonArray}> 异步执行结果
     */
    public static Future<JsonArray> metadataAsync(final JsonArray input) {
        return Ux.future(OxField.metadata(input));
    }

    /**
     * 元数据执行器
     *
     * 支持功能：
     *
     * - 针对字段`metadata`执行Json转换。
     * - 按`visible = true/false`执行过滤，如果不存在则默认为`true`，筛选元素。
     * - 针对字段`columns`执行Json转换。
     *
     * @param input 输入的最终响应数据
     *
     * @return {@link JsonArray} 同步执行结果
     */
    public static JsonArray metadata(final JsonArray input) {
        return OxField.metadata(input);
    }

    /**
     * 「Node」图节点数组格式化专用方法。
     *
     * 格式化判断：
     *
     * - 如果出现相同的`globalId`则直接忽略，先执行节点合并（按`globalId`执行压缩）。
     * - 每一个节点内部调用`toNode`重载方法（{@link JsonObject}类型处理）。
     *
     * @param nodes {@link JsonArray} 待格式化的图节点数组
     *
     * @return {@link JsonArray} 完成格式化的图节点数组
     */
    public static JsonArray toNode(final JsonArray nodes) {
        return OxTo.toNode(nodes);
    }

    /**
     * 「Node」图节点格式化专用方法。
     *
     * 格式化细节：
     *
     * - 将`globalId`赋值给`key`属性。
     * - 拷贝`name`属性。
     * - 拷贝`code`属性。
     * - 将原始数据{@link JsonObject}拷贝到`data`属性中。
     *
     * @param node {@link JsonObject} 待格式化的图节点对象
     *
     * @return {@link JsonObject} 完成格式化的图节点
     */
    public static JsonObject toNode(final JsonObject node) {
        return OxTo.toNode(node);
    }

    /**
     * 「Edge」图边数组格式化专用方法。
     *
     * > 内部调用`toEdge`的重载方法（{@link JsonObject}类型）。
     *
     * @param edges {@link JsonArray} 待格式化的边数据数组
     *
     * @return {@link JsonArray} 已格式化的边数据数组
     */
    public static JsonArray toEdge(final JsonArray edges) {
        return OxTo.toEdge(edges);
    }

    /**
     * 「Edge」图边格式化专用方法
     *
     * 格式化细节：
     *
     * - 拷贝`sourceGlobalId`到`source`属性中。
     * - 拷贝`targetGlobalId`到`target`属性中。
     * - 拷贝`type`边类型到`type`属性中。
     * - 将原始数据{@link JsonObject}拷贝到`data`属性中。
     *
     * @param edge {@link JsonObject} 待格式化的边对象
     *
     * @return {@link JsonObject} 已格式化的边对象
     */
    public static JsonObject toEdge(final JsonObject edge) {
        return OxTo.toEdge(edge);
    }

    /**
     * 提取上下游关系合并到一起。
     *
     * - down：下游属性。
     * - up：上游属性。
     *
     * @param source 输入的源类型数据
     * @param <T>    输入的源中元素的Java类型
     *
     * @return 拉平过后的关系信息
     */
    public static <T> JsonArray toLinker(final T source) {
        return OxTo.toLinker(source);
    }


    /**
     * 构造比对报表
     *
     * @param atom  {@link DataAtom}
     * @param forms {@link JsonArray} 当前模型关联的表单集`UI_FORM`
     * @param lists {@link JsonArray} 当前模型关联的列表集`UI_LIST`
     *
     * @return {@link JsonObject} 返回比对报表
     */
    public static JsonArray compareUi(final DataAtom atom, final JsonArray forms, final JsonArray lists) {
        return OxCompareUi.compareUi(atom, forms, lists);
    }

    /**
     * 统一视图工具执行器
     *
     * `GET /api/ox/columns/:identifier/all`全列读取请求
     *
     * 请求数据格式
     *
     * - identifier：模型标识符
     * - dynamic：是否动态视图（存储在`S_VIEW`中）
     * - view：视图类型，默认值`DEFAULT`
     *
     * 除开上述参数后，还包含`Http Header`的所有信息。
     *
     * @param envelop
     * @param identifier
     *
     * @return {@link Future}<{@link JsonArray}>
     */
    public static Future<JsonArray> viewFull(final Envelop envelop, final String identifier) {
        return OxView.viewFull(envelop, identifier);
    }

    /**
     * 我的视图列工具执行器
     *
     * `GET /api/ox/columns/:identifier/my`我的视图列读取请求
     *
     * 请求数据格式
     *
     * - uri：当前请求路径
     * - method：当前HTTP方法
     * - 合并了所有`Http Header`的内容
     *
     * 返回数据格式：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *     "user": "用户主键",
     *     "habitus": "构造的权限池信息",
     *     "view": "视图类型"
     * }
     * // </code></pre>
     * ```
     *
     * @param envelop    {@link Envelop} Zero标准请求模型
     * @param identifier {@link String} 模型标识符
     *
     * @return {@link Future}<{@link JsonArray}>
     */
    public static Future<JsonObject> viewMy(final Envelop envelop, final String identifier) {
        return OxView.viewMy(envelop, identifier);
    }

    /**
     * 根据<strong>应用标识</strong>和<strong>模型标识</strong>构造数据库访问器`Dao`对象。
     *
     * @param key        {@link String} 应用标识，可以是`appId`、也可以是`sigma`
     * @param identifier {@link String} 模型统一标识符
     *
     * @return {@link HDao} 数据库访问对象
     */
    public static HDao toDao(final String key, final String identifier) {
        return OxTo.toDao(key, identifier);
    }

    public static HDao toDao(final DataAtom atom) {
        return OxTo.toDao(atom.sigma(), atom.identifier());
    }

    /**
     * 根据<strong>应用标识</strong>和<strong>模型标识</strong>构造模型定义对象。
     *
     * @param key        {@link String} 应用标识，可以是`appId`、也可以是`sigma`
     * @param identifier {@link String} 模型统一标识符
     *
     * @return {@link DataAtom} 模型定义
     */
    public static DataAtom toAtom(final String key, final String identifier) {
        return OxTo.toAtom(key, identifier);
    }

    /**
     * 「开发专用」调试和开发专用方法（监控数据信息）。
     *
     * @param clazz {@link Class} 调用该函数的类
     * @param <T>   函数Monad输入和输出相关数据类型
     *
     * @return {@link Function} 返回异步函数
     */
    public static <T> Function<T[], Future<T[]>> toArray(final Class<?> clazz) {
        return result -> {
            Log.infoUca(clazz, "结果数组数量：{0}", result.length);
            return Ux.future(result);
        };
    }


    /**
     * 为记录主键赋值，内置调用`UUID.randomUUID().toString()`。
     *
     * @param data {@link JsonObject} 数据信息
     * @param atom {@link DataAtom} 模型定义
     *
     * @return {@link JsonObject} 处理过的数据信息
     */
    public static JsonObject elementUuid(final JsonObject data, final DataAtom atom) {
        return OxJson.elementUuid(data, atom);
    }

    /**
     * 「批量」为记录主键赋值，内置调用`UUID.randomUUID().toString()`。
     *
     * @param data {@link JsonArray} 数据信息
     * @param atom {@link DataAtom} 模型定义
     *
     * @return {@link JsonArray} 处理过的数据信息
     */
    public static JsonArray elementUuid(final JsonArray data, final DataAtom atom) {
        Ut.itJArray(data).forEach(record -> elementUuid(record, atom));
        return data;
    }

    public static String stellarConnect() {
        return OxConfig.stellarConnect();
    }

    /**
     * 数组压缩，将每个元素中的`null`使用`""`替换。
     *
     * @param input {@link JsonArray} 待处理数组数据
     * @param atom  {@link DataAtom} 模型定义
     *
     * @return {@link JsonArray} 处理后的数据
     */
    public static JsonArray elementCompress(final JsonArray input, final DataAtom atom) {
        return OxJson.elementCompress(input, atom);
    }

    /**
     * ## 环境静态类
     *
     * ### 1. 基本介绍
     *
     * 读取配置文件`runtime/configuration.json`构造CMDB基础环境。
     *
     * @author <a href="http://www.origin-x.cn">Lang</a>
     */
    public interface Env {

        /**
         * <value>item.enabled</value>，ITSM 专用流程开关。
         *
         * @return {@link Boolean} ITSM是否启用
         */
        static boolean isItsmEnabled() {
            return OxConfig.isItsmEnabled();
        }


        /**
         * <value>cmdb.commutator</value>，反射专用生命周期处理器配置（下层调用上层，使用反射，不能直接使用类型）。
         *
         * @param commutator `cn.originx.uca.workflow.Commutator`类型默认值
         *
         * @return {@link Class} 返回最终的 clazz 值
         */
        static Class<?> commutator(final Class<?> commutator) {
            return OxConfig.toCommutator(commutator);
        }

        /**
         * <value>options</value>，返回服务配置选项专用数据，构造`options`选项。
         *
         * @return {@link JsonObject}
         */
        static JsonObject options() {
            return OxConfig.toOptions();
        }

        /**
         * 读取核心配置，使用双维度从核心配置中提取配置信息。
         *
         * 原始配置如下：
         *
         * ```json
         * // <pre><code class="json">
         * {
         *     "components": {
         *         "ADD.true": {},
         *         "ADD.false": {},
         *         "UPDATE.true": {},
         *         "UPDATE.false": {},
         *         "DELETE.true": {},
         *         "DELETE.false": {}
         *     }
         * }
         * // </code></pre>
         * ```
         *
         * @param type  {@link ChangeFlag} 操作类型
         * @param batch {@link Boolean} 是否批量
         *
         * @return {@link JsonObject} 配置项数据
         */
        static JsonObject components(final ChangeFlag type, final Boolean batch) {
            return OxConfig.HEX.data(type, batch);
        }

        /**
         * 根据日志类型读取日志信息。
         *
         * @param type {@link TypeLog} 日志类型
         *
         * @return 返回该日志类型中的打印日志内容
         */
        static String message(final TypeLog type) {
            return OxConfig.toMessage(type);
        }
    }

    /**
     * ## 日志器
     *
     * ### 1. 基本介绍
     *
     * Ox平台专用内部日志器，外部日志器。
     *
     * ### 2. 日志器种类
     *
     * - Atom模型日志器
     * - UCA日志器
     * - Shell日志器
     * - 插件Plugin日志器
     * - Hub总线日志器
     * - Web流程日志器
     * - 状态日志器
     */
    public interface Log {

        /**
         * Info级别，模型日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoAtom(final Annal logger, final String pattern, final Object... args) {
            OxLog.info(logger, "Atom", pattern, args);
        }

        static void infoAtom(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            OxLog.info(logger, "Atom", pattern, args);
        }

        /**
         * Debug级别，模型日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void debugAtom(final Annal logger, final String pattern, final Object... args) {
            OxLog.debug(logger, "Atom", pattern, args);
        }

        /**
         * Debug级别，UCA日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void debugUca(final Annal logger, final String pattern, final Object... args) {
            OxLog.debug(logger, "UCA", pattern, args);
        }

        /**
         * Debug级别，UCA日志器
         *
         * @param clazz   {@link Class} 调用日志器的类
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void debugUca(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            debugUca(logger, pattern, args);
        }

        /**
         * Info级别，UCA日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoUca(final Annal logger, final String pattern, final Object... args) {
            OxLog.info(logger, "UCA", pattern, args);
        }

        /**
         * Info级别，UCA日志器
         *
         * @param clazz   {@link Class} 调用日志器的类
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoUca(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            infoUca(logger, pattern, args);
        }

        /**
         * Warn级别，UCA日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void warnUca(final Annal logger, final String pattern, final Object... args) {
            OxLog.warn(logger, "UCA", pattern, args);
        }

        /**
         * Warn级别，UCA日志器
         *
         * @param clazz   {@link Class} 调用日志器的类
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void warnUca(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            warnUca(logger, pattern, args);
        }

        /**
         * Info级别，Shell日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoShell(final Annal logger, final String pattern, final Object... args) {
            OxLog.info(logger, "Shell", pattern, args);
        }

        /**
         * Info级别，Shell日志器
         *
         * @param clazz   {@link Class} 调用日志器的类
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoShell(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            OxLog.info(logger, "Shell", pattern, args);
        }

        /**
         * Warn级别，Shell日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void warnShell(final Annal logger, final String pattern, final Object... args) {
            OxLog.warnShell(logger, pattern, args);
        }

        /**
         * Info级别，插件日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoPlugin(final Annal logger, final String pattern, final Object... args) {
            OxLog.info(logger, "Plugin", pattern, args);
        }

        /**
         * Info级别，插件日志器
         *
         * @param clazz   {@link Class} 调用日志器的类
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoPlugin(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            infoPlugin(logger, pattern, args);
        }

        /**
         * Warn级别，插件日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void warnPlugin(final Annal logger, final String pattern, final Object... args) {
            OxLog.warn(logger, "Plugin", pattern, args);
        }

        /**
         * Warn级别，插件日志器
         *
         * @param clazz   {@link Class} 调用日志器的类
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void warnPlugin(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            warnPlugin(logger, pattern, args);
        }

        /**
         * Info级别，Hub日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoHub(final Annal logger, final String pattern, final Object... args) {
            OxLog.infoHub(logger, pattern, args);
        }

        /**
         * Info级别，Hub日志器
         *
         * @param clazz   {@link Class} 调用日志器的类
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoHub(final Class<?> clazz, final String pattern, final Object... args) {
            OxLog.infoHub(clazz, pattern, args);
        }

        /**
         * Warn级别，Hub日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void warnHub(final Annal logger, final String pattern, final Object... args) {
            OxLog.warnHub(logger, pattern, args);
        }

        /**
         * Warn级别，Hub日志器
         *
         * @param clazz   {@link Class} 调用日志器的类
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void warnHub(final Class<?> clazz, final String pattern, final Object... args) {
            OxLog.warnHub(clazz, pattern, args);
        }

        /**
         * Info级别，Web流程日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoWeb(final Annal logger, final String pattern, final Object... args) {
            OxLog.info(logger, "Web", pattern, args);
        }

        /**
         * Info级别，Web流程日志器
         *
         * @param clazz   {@link Class} 调用日志器的类
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoWeb(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            infoWeb(logger, pattern, args);
        }

        /**
         * Info级别，状态日志器
         *
         * @param logger  {@link Annal} Zero专用日志器
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoStatus(final Annal logger, final String pattern, final Object... args) {
            OxLog.info(logger, "Status", pattern, args);
        }

        /**
         * Info级别，状态日志器
         *
         * @param clazz   {@link Class} 调用日志器的类
         * @param pattern {@link String} 日志信息模式
         * @param args    {@link Object...} 可变参数
         */
        static void infoStatus(final Class<?> clazz, final String pattern, final Object... args) {
            final Annal logger = Annal.get(clazz);
            infoStatus(logger, pattern, args);
        }

        /**
         * 比对报表日志器
         *
         * @param clazz    {@link Class} 调用日志器的类
         * @param compared 比对结果
         */
        static void infoReport(final Class<?> clazz, final ConcurrentMap<ChangeFlag, JsonArray> compared) {
            OxLog.infoCompared(clazz, compared);
        }
    }
}
