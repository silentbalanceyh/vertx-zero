package cn.originx.refine;

import cn.originx.cv.OxCv;
import cn.originx.cv.em.TypeLog;
import io.vertx.core.json.JsonObject;
import io.vertx.tp.atom.modeling.builtin.DataAtom;
import io.vertx.up.commune.config.Identity;
import io.vertx.up.commune.element.JSix;
import io.vertx.up.eon.KName;
import io.vertx.up.eon.Strings;
import io.vertx.up.util.Ut;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * ## 环境配置工具
 *
 * ### 1. 基本介绍
 *
 * 系统中的基础环境配置专用工具类。
 *
 * ### 2. 支持功能
 *
 * - 检查是否开启了ITSM环境。
 * - 读取<strong>六维度</strong>的基础配置信息。
 * - 日志配置解析和读取。
 * - 标识规则选择器读取/Commutator生命周期选择器读取。
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
final class OxConfig {

    /**
     * 六维度核心数据结构，类型{@link JSix}。
     *
     * 3 x 2 的维度结构
     *
     * - 维度1：ChangeFlag（增删改）
     * - 维度2：batch（是否批量）
     *
     * 解析的根节点
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
     * 维度表格：
     *
     * |ChangeType|batch|含义|
     * |---|---|:---|
     * |ADD|true|多记录添加（批量）|
     * |ADD|false|单记录添加|
     * |UPDATE|true|多记录更新（批量）|
     * |UPDATE|false|单记录更新|
     * |DELETE|true|多记录删除（批量）|
     * |DELETE|false|单记录删除|
     */
    static final JSix HEX;
    /**
     * 内部配置原始数据信息，{@link JsonObject}类型。
     *
     * 默认读取路径：`runtime/configuration.json`
     */
    private static final JsonObject CONFIGURATION = new JsonObject();
    /**
     * 错误信息Map，存储了错误信息的哈希表，从`ko`中提取错误信息。
     *
     * ```json
     * // <pre><code class="json">
     *     {
     *          "ko": {
     *              "INTEGRATION_ERROR": "集成环境出现了不可预知的异常，请联系管理员！",
     *              "TODO_ONGOING": "将要生成待确认的配置项正在＂待确认＂流程，完成待确认后才可重新生成！",
     *              "PUSH_NONE": "标识规则没满足，系统自动过滤该数据不执行推送！",
     *              "PUSH_FAIL": "推送过程中出现了不可预知的错误信息！"
     *          }
     *     }
     * // </code></pre>
     * ```
     */
    private static final ConcurrentMap<TypeLog, String> MESSAGE = new ConcurrentHashMap<>();

    static {
        final JsonObject configuration = Ut.ioJObject(OxCv.Ambient.CONFIG_FILE);
        CONFIGURATION.mergeIn(configuration);
        /*
         * 日志
         */
        final JsonObject koJson = configuration.getJsonObject("ko");
        if (Ut.notNil(koJson)) {
            koJson.fieldNames().forEach(field -> {
                final String message = koJson.getString(field);
                if (Ut.notNil(message)) {
                    final TypeLog logKey = Ut.toEnum(TypeLog.class, field);
                    if (Objects.nonNull(logKey)) {
                        MESSAGE.put(logKey, message);
                    }
                }
            });
        }
        HEX = JSix.create(CONFIGURATION);
    }

    /*
     * 私有构造函数（工具类转换）
     */
    private OxConfig() {
    }

    private static JsonObject toConfiguration() {
        return CONFIGURATION;
    }

    static String stellarConnect() {
        return CONFIGURATION.getString(KName.Tenant.STELLAR, null);
    }

    /**
     * <value>item.enabled</value>，ITSM 专用流程开关。
     *
     * @return {@link Boolean} ITSM是否启用
     */
    static boolean isItsmEnabled() {
        final JsonObject configuration = toConfiguration();
        final Boolean enabled = configuration.getBoolean("itsm.enabled");
        if (Objects.isNull(enabled)) {
            return false;
        } else {
            return enabled;
        }
    }

    /**
     * <value>cmdb.commutator</value>，反射专用生命周期处理器配置（下层调用上层，使用反射，不能直接使用类型）。
     *
     * @param commutator `cn.originx.uca.workflow.Commutator`类型默认值
     *
     * @return {@link Class} 返回最终的 clazz 值
     */
    static Class<?> toCommutator(final Class<?> commutator) {
        final JsonObject configuration = toConfiguration();
        final String clsStr = configuration.getString("cmdb.commutator");
        if (Ut.isNil(clsStr)) {
            return commutator;
        } else {
            return Ut.clazz(clsStr, commutator);
        }
    }

    /**
     * <value>options</value>，返回服务配置选项专用数据，构造`options`选项。
     *
     * @return {@link JsonObject}
     */
    static JsonObject toOptions() {
        final JsonObject configuration = toConfiguration();
        final JsonObject pluginConfig = configuration.getJsonObject(KName.OPTIONS);
        if (Ut.isNil(pluginConfig)) {
            return new JsonObject();
        } else {
            return pluginConfig.copy();
        }
    }

    /**
     * 构造标识规则选择器，读取插件<value>plugin.identifier</value>值提取标识规则选择器。
     *
     * @param atom    {@link DataAtom} 模型定义
     * @param options {@link JsonObject} 服务配置选项
     *
     * @return {@link Identity} 构造好的标识规则选择器
     */
    static Identity toIdentity(final DataAtom atom, final JsonObject options) {
        final String identifierCls = options.getString(OxCv.PLUGIN_IDENTIFIER);
        /*
         * Identity 的静态构造
         * 注：sigma 是必须的参数
         */
        final Identity identity = new Identity();
        identity.setIdentifier(atom.identifier());
        identity.setIdentifierComponent(Ut.clazz(identifierCls));
        identity.setSigma(atom.sigma());
        return identity;
    }

    /**
     * 根据日志类型读取日志信息。
     *
     * @param log {@link TypeLog} 日志类型
     *
     * @return 返回该日志类型中的打印日志内容
     */
    static String toMessage(final TypeLog log) {
        if (Objects.isNull(log)) {
            return Strings.EMPTY;
        } else {
            return MESSAGE.getOrDefault(log, Strings.EMPTY);
        }
    }
}
