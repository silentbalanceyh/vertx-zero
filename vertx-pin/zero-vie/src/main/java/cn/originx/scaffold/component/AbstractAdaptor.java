package cn.originx.scaffold.component;

import cn.originx.refine.Ox;
import io.vertx.core.Future;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.data.DataAtom;
import io.vertx.tp.atom.refine.Ao;
import io.vertx.tp.error._400KeyLengthException;
import io.vertx.tp.jet.uca.business.AbstractComponent;
import io.vertx.tp.modular.dao.AoDao;
import io.vertx.tp.optic.robin.Switcher;
import io.vertx.up.annotations.Contract;
import io.vertx.up.commune.ActIn;
import io.vertx.up.commune.Record;
import io.vertx.up.commune.config.Database;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.config.XHeader;
import io.vertx.up.commune.exchange.BiMapping;
import io.vertx.up.commune.exchange.DiFabric;
import io.vertx.up.eon.KName;
import io.vertx.up.exception.WebException;
import io.vertx.up.exception.web._501NotSupportException;
import io.vertx.up.uca.adminicle.FieldMapper;
import io.vertx.up.unity.Ux;
import io.vertx.up.util.Ut;

import java.lang.reflect.Array;
import java.util.Objects;
import java.util.function.Function;

/**
 * ## 「Adaptor」顶层适配器
 *
 * ### 1. 基本介绍
 *
 * 直接从`{@link AbstractComponent}`中继承构造的Ox平台专用的顶层组件。
 *
 * ### 2. 组件功能
 *
 * - 提供数据库实例，对应`I_SERVICE`中的数据库配置{@link Database}对象（configDatabase属性）。
 * - 数据库访问Dao的专用创建，Dao分为两种：<strong>配置型</strong>（不传参数）和<strong>切换型</strong>（传入参数）。
 *
 * ### 3. 配置数据结构
 *
 * #### 3.1. 数据库配置结构
 *
 * ```json
 * // <pre><code class="json">
 * {
 *      "hostname": "数据库IP地址或域名",
 *      "instance": "数据库实例名",
 *      "username": "账号信息",
 *      "password": "账号登录口令",
 *      "port": "Number, 数据库端口号",
 *      "category": "数据库类型",
 *      "driverClassName": "JDBC驱动名称",
 *      "jdbcUrl": "JDBC连接字符串（注意时区、编码、重连方式）",
 * }
 * // </code></pre>
 * ```
 *
 * #### 3.2. 关于模型定义
 *
 * 模型定义在Ox中使用类型{@link DataAtom}模型定义类型
 *
 * - 不传参的方法，直接获取原生配置`I_SERVICE`中的`identifier`相关模型定义（配置型）。
 * - 传入参数的方法，可获取动态的模型定义（切换型）。
 *
 * 子类在使用{@link DataAtom}时可使用<strong>静态</strong>和<strong>动态</strong>两种，而且动态只能调用
 * {@link Switcher}执行切换。
 *
 * #### 3.3. 标识规则
 *
 * 标识规则的绑定会区分优先级
 *
 * 1. 先从通道中构造{@link io.vertx.up.commune.rule.RuleUnique}对象（通道定义，高优先级）。
 * 2. 再读取模型定义{@link DataAtom}中的标识规则（模型定义，低优先级）。
 *
 * ### 4. 关于四种组件
 *
 * |父类|组件名|含义|
 * |:---|:---|:---|
 * |AbstractComponent|AbstractAdaptor|适配器（数据库配置）|
 * |AbstractAdaptor|AbstractConnector|连接器（追加集成配置）|
 * |AbstractAdaptor|AbstractDirector|执行器（追加任务配置）|
 * |AbstractConnector|AbstractActor|调度器（追加任务配置）|
 *
 * 四种组件的支持如下：
 *
 * |组件名|数据库配置|集成配置|任务配置|
 * |:---|---|---|---|
 * |AbstractAdaptor|Ok|x|x|
 * |AbstractConnector|Ok|Ok|x|
 * |AbstractDirector|Ok|x|Ok|
 * |AbstractActor|Ok|Ok|Ok|
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public abstract class AbstractAdaptor extends AbstractComponent {

    // ------------ 数据库实例引用 -------------------------
    /**
     * 「合约」成员实例，`I_SERVICE`表中的`configDatabase`属性设置，关联{@link Database}对象。
     *
     * 该实例是通过{@link Contract}注解赋值，配置结构如上述文档中所描述，下边是其中一个示例：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *      "hostname": "localhost",
     *      "instance": "DB_ORIGIN_X",
     *      "username": "lang",
     *      "password": "xxxx",
     *      "port": 3306,
     *      "category": "MYSQL5",
     *      "driverClassName": "Fix driver issue here",
     *      "jdbcUrl": "jdbc:mysql://ox.engine.cn:3306/DB_ORIGIN_X.....",
     * }
     * // </code></pre>
     * ```
     */
    @Contract
    private transient Database database;
    /**
     * 成员实例，配置型（不传参）的模型定义对象{@link DataAtom}。
     */
    private transient DataAtom internalAtom;


    // ------------ Atom 的专用创建方法 -------------------------

    /**
     * 返回当前系统中的数据库实例引用。
     *
     * @return {@link Database}数据库实例
     */
    protected Database database() {
        return this.database;
    }

    /**
     * 成员函数，读取静态模型定义对象{@link DataAtom}。
     *
     * 静态{@link DataAtom}计算方法如下：
     *
     * 1. 如果`internalAtom`为空，重算该值。
     * 2. 若`internalAtom`不为空，返回该模型定义。
     *
     * 重算流程如：
     *
     * 1. 使用`this.options()`返回结果，构造{@link DataAtom}（从缓存中提取新的）。
     * 2. 将{@link DataAtom}和当前模型中的<strong>标识规则</strong>对象绑定。
     * 3. 挂外置的通道专用{@link io.vertx.up.commune.rule.RuleUnique}，默认信息DataAtom和通道中的标识规则连接，该方法是静态场景专用的标识规则对象（通道规则优先）。
     *
     * 内部调用`Ao.toAtom`方法的数据结构如下：
     *
     * ```json
     * // <pre><code class="json">
     *     {
     *         "name": "应用名称",
     *         "identifier": "模型ID标识符"
     *     }
     * // </code></pre>
     * ```
     *
     * > 该方法会对`this.internalAtom`执行重算，若为 null 则直接重算当前通道内的`this.internalAtom`对象（通常用于动态通道）。
     *
     * @return {@link DataAtom}计算过后的模型定义
     */
    protected DataAtom atom() {
        if (Objects.isNull(this.internalAtom)) {
            final DataAtom atom = Ao.toAtom(this.options());
            // Model Performer
            if (Objects.nonNull(atom)) {
                this.internalAtom = atom
                    /*
                     *「Unique」
                     * 挂外置的通道专用 RuleUnique
                     * 默认信息 DataAtom 和通道连接
                     * 静态场景专用的 RuleUnique，不支持子模式
                     * 通道直接连接
                     * */
                    .ruleConnect(this.rule());
            }
        }
        return this.internalAtom;
    }

    /**
     * 根据通道中是否配置了<strong>标识选择器</strong>来获取{@link DataAtom}模型定义的切换器。
     *
     * 基本逻辑：
     *
     * - 如果{@link Identity}为null或内部配置的`identifierComponent`为null，则直接返回空。
     * - 如果存在，则使用参数构造标识规则切换器。
     *
     * 参数结构：
     *
     * ```json
     * // <pre><code class="json">
     * {
     *     "header": "头核心信息（四个键值）",
     *     "options": "服务配置中的 serviceConfig 属性存储信息"
     * }
     * // </code></pre>
     * ```
     *
     * @return {@link Switcher}切换器引用
     */
    protected Switcher switcher() {
        final Identity identity = this.identity();
        if (Objects.isNull(identity) || Objects.isNull(identity.getIdentifierComponent())) {
            /*
             * Identity 不合法
             */
            return null;
        } else {
            final XHeader header = this.header;
            /*
             * XHeader 读取值
             */
            final JsonObject options = new JsonObject();
            if (Objects.nonNull(header)) {
                final JsonObject headerJson = header.toJson();
                options.put(KName.HEADER, headerJson);
            }
            options.put(KName.OPTIONS, this.options());
            return Ao.toSwitcher(identity, options);
        }
    }


    // ------------ Dao/Record 构造方法 -------------------------

    /**
     * 「静态」数据库访问器获取。
     *
     * - 数据库实例{@link Database}。
     * - 模型定义{@link DataAtom}。
     *
     * > 如果系统中未配置`identifier`属性——即通道和模型未绑定，则调用`this.atom()`执行运算修改`this.internalAtom`再构造数据库访问器。
     *
     * @return {@link AoDao}数据库访问器
     */
    protected AoDao dao() {
        return Ao.toDao(this.database, this.atom());
    }

    /**
     * 「动态」数据库访问器获取。
     *
     * - 数据库实例{@link Database}。
     * - 模型定义从外置传入，但在这个过程中需要执行<strong>标识规则</strong>绑定，绑定到当前通道中的标识规则。
     *
     * @param atom 传入的{@link DataAtom}模型定义对象。
     *
     * @return {@link AoDao}数据库访问器
     */
    protected AoDao dao(final DataAtom atom) {
        return Ao.toDao(this.database, atom.ruleConnect(this.rule()));
    }


    /**
     * 「单量」使用{@link ActIn}输入对象构造{@link Record}数据记录。
     *
     * > 内部合约{@link Contract}调用注入{@link DataAtom}模型定义实例。
     *
     * @param request {@link ActIn} 通道专用的请求对象。
     *
     * @return {@link Record} 动态构造数据记录对象
     */
    protected Record activeRecord(final ActIn request) {
        final Record definition = request.getDefinition();
        Ut.contract(definition, DataAtom.class, this.atom());
        return request.getRecord();
    }

    /**
     * 「批量」使用{@link ActIn}输入对象构造{@link Record}[]数据记录。
     *
     * @param request {@link ActIn} 通道专用的请求对象。
     *
     * @return {@link Record}[] 动态构造数据记录对象
     */
    protected Record[] activeRecords(final ActIn request) {
        final Record definition = request.getDefinition();
        Ut.contract(definition, DataAtom.class, this.atom());
        return request.getRecords();
    }

    /**
     * 「单量」使用{@link ActIn}输入对象构造{@link Record}数据记录的主键。
     *
     * @param request {@link ActIn} 通道专用的请求对象。
     * @param <ID>    泛型对象，主键类型。
     *
     * @return 返回泛型主键
     */
    protected <ID> ID activeKey(final ActIn request) {
        return this.activeRecord(request).key();
    }

    /**
     * 「批量」使用{@link ActIn}输入对象构造{@link Record}[]数据记录的主键集。
     *
     * 若定义长度有问题，则抛出`-80527`异常{@link _400KeyLengthException}。
     *
     * @param request {@link ActIn} 通道专用的请求对象。
     * @param <ID>    泛型对象，主键类型。
     *
     * @return 返回泛型主键集
     */
    @SuppressWarnings("unchecked")
    protected <ID> ID[] activeKeys(final ActIn request) {
        /* 解决 Bug：java.lang.ClassCastException: [Ljava.lang.Object; cannot be cast to [Ljava.lang.String; */
        final Record[] records = this.activeRecords(request);
        final int length = records.length;
        if (0 == length) {
            /* 无主键异常`io.vertx.tp.error._400KeyLengthException`抛出。*/
            throw new _400KeyLengthException(this.getClass());
        } else {
            final Record record = records[0];
            final ID key = record.key();
            final ID[] keys = (ID[]) Array.newInstance(key.getClass(), length);
            /* 构造主键集 */
            for (int idx = 0; idx < length; idx++) {
                final Record found = records[idx];
                keys[idx] = found.key();
            }
            return keys;
        }
    }

    // ------------ 字典翻译器方法重写 -----------------

    /**
     * 「静态」字典翻译器获取
     *
     * - Dictionary：字典数据
     * - Epsilon：字典消费组件配置
     * - DualItem：字典映射配置
     *
     * @return {@link DiFabric} 字典翻译器
     */
    public DiFabric fabric() {
        return this.fabric(this.atom());
    }

    /**
     * 「动态」字典翻译器获取
     *
     * - Dictionary：字典数据
     * - Epsilon：字典消费组件配置
     * - DualItem：字典映射配置
     *
     * @param atom 传入的{@link DataAtom}模型定义对象。
     *
     * @return {@link DiFabric} 字典翻译器
     */
    public DiFabric fabric(final DataAtom atom) {
        final BiMapping mapping = this.mapping().child(atom.identifier());
        if (Objects.nonNull(mapping)) {
            mapping.bind(atom.type());
        }
        /*
         * 纯字典
         * this.fabric
         *
         * 1. Dictionary    字典数据
         * 2. Epsilon       字典消费数据
         */
        return this.fabric.copy(mapping);
    }

    // ------------ 通用方法处理 -------------------------

    /**
     * 字段映射器，用于执行字段名转换专用。
     *
     * @return {@link FieldMapper}
     */
    protected FieldMapper vector() {
        return new FieldMapper();
    }

    /**
     * 「Async」异常专用方法，如果未实现，则直接抛出异常{@link _501NotSupportException}标识API未实现。
     *
     * @param <T> 返回的元素泛型
     *
     * @return {@link Future}
     */
    protected <T> Future<T> transferFailure() {
        final WebException error = new _501NotSupportException(this.getClass());
        Ox.Log.infoPlugin(this.getClass(), "[ Plugin ] Do not support api: {0}", error.getMessage());
        return Future.failedFuture(error);
    }

    /**
     * 「Async」统一函数方法，日志器调用会需要（单记录和数组类型）。
     *
     * @param input         {@link JsonObject}/{@link JsonArray} 输入数据类型。
     * @param jsonExecutor  {@link Function} 单记录执行函数。
     * @param arrayExecutor {@link Function} 多记录执行函数。
     * @param <T>           返回的元素泛型（执行元素泛型）
     *
     * @return {@link Future}
     */
    @SuppressWarnings("unchecked")
    protected <T> Future<T> transferAsync(final T input,
                                          final Function<JsonObject, Future<JsonObject>> jsonExecutor,
                                          final Function<JsonArray, Future<JsonArray>> arrayExecutor) {
        if (input instanceof JsonObject) {
            final JsonObject normalized = (JsonObject) input;
            return jsonExecutor.apply(normalized).compose(json -> Ux.future((T) json));
        } else if (input instanceof JsonArray) {
            final JsonArray normalized = (JsonArray) input;
            return arrayExecutor.apply(normalized).compose(json -> Ux.future((T) json));
        } else {
            return Future.failedFuture(new _501NotSupportException(this.getClass()));
        }
    }
}
