package cn.originx.cv.em;

/**
 * ## 日志类型
 *
 * ### 1. 基本介绍
 *
 * 日志类型枚举变量，用来设置日志类型，对应到：`X_LOG`表中的`TYPE`字段，该枚举值目前只被`cn.originx.uca.log`包中的日志器使用，
 * 支持<strong>同步</strong>和<strong>异步</strong>两种日志记录模式。
 *
 * ### 2. 核心点
 *
 * - 日志全部存储在`X_LOG`数据表中。
 * - 日志类型映射到`TYPE`字段，并且在日志管理中直接生成左侧的类型信息。
 * - 该数据类型目前只被`cn.originx.uca.log`中的日志器使用。
 *
 * ### 3. 内部代码
 *
 * ```java
 * // <pre><code class="java">
 *     // 数据库错误日志生成
 *     static XLog database(final Class<?> clazz, final DataAtom atom, final Throwable ex) {
 *         final XLog log = KoTool.error(clazz, atom, ex);
 *         log.setType(TypeLog.DATABASE_ERROR.name());
 *         return log;
 *     }
 * // </code></pre>
 * ```
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public enum TypeLog {
    /**
     * 「UCMDB」拉取数据过程中，因为拉取的数据未满足业务标识规则而导致最终未入库，生成数据非法异常。
     */
    INVALID_DATA,
    /**
     * 「UCMDB」数据本身没有满足业务标识规则，导致推送数据未触发。
     */
    PUSH_NONE,
    /**
     * 「UCMDB」发生了数据推送过程中，出现了来自于UCMDB中的服务端异常。
     */
    PUSH_FAIL,
    /**
     * 「UCMDB」数据批量推送过程中，出现了来自于UCMDB中的服务端异常。
     */
    PUSH_BATCH_FAIL,


    /**
     * 「标准」集成配置初始化异常，和第三方集成过程中无法构造正确的集成配置异常。
     */
    INTEGRATION_ERROR,
    /**
     * 「标准」正在执行（待确认）流程，如果是SM被启用，则表示工单未完成，若没有启用SM，则当前配置项的待确认还没处理。
     */
    TODO_ONGOING,
    /**
     * 「标准」访问配置项数据库时出现的数据库访问异常，SQL类型异常。
     */
    DATABASE_ERROR,
}
