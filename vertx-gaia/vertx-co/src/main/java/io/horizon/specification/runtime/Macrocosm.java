package io.horizon.specification.runtime;

import io.vertx.up.util.Ut;

import java.util.Arrays;

/**
 * 核心系统环境变量接口，可直接实现
 * - Macrocosm：宏观世界，核心空间
 *
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface Macrocosm {
    /*
     * 环境变量（配置KEY前缀说明）
     * - Z_DEV_           :       开发专用配置
     * - Z_CACHE_         :       缓存专用配置
     */
    // 「Development」开发专用 ---------------------------------------
    /*
     * development:
     *   env:
     *
     * 该配置中存在环境变量和属性的双读取，优先级
     * 1 .env.development           环境变量文件中的值优先（云发布）
     * 2 vertx-deployment.yml       文件中的值次之（默认值）
     *
     */
    String DEV_IO = "Z_DEV_IO";                         // 查看底层IO日志
    String DEV_JOOQ_COND = "Z_DEV_JOOQ_COND";           // 查看 jooq 查询条件日志（等价SQL日志）
    String DEV_EXPR_BIND = "Z_DEV_EXPR_BIND";           // 表达式引擎专用日志打印
    String DEV_EXCEL_RANGE = "Z_DEV_EXCEL_RANGE";       // Excel数据加载扫描日志
    String DEV_JOB_BOOT = "Z_DEV_JOB_BOOT";             // 任务启动专用日志
    String DEV_JVM_STACK = "Z_DEV_JVM_STACK";           // ex.printStackTrace() 异常日志
    String DEV_WEB_URI = "Z_DEV_WEB_URI";               // URI路由检测状况

    String DEV_DAO_BIND = "Z_DEV_DAO_BIND";             // CRUD的 Dao文件绑定

    String DEV_AUTHORIZED = "Z_DEV_AUTHORIZED";         // 认证日志

    /*
     * Zero 系统启动时对应核心环境变量
     * 原始环境变量（新追加，方便协同开发）
     * - Z_API_PORT / Z_API_HOST
     *   Http Server启动端口专用环境变量，如果未设置则选择配置文件中的环境变量
     * - Z_SOCK_PORT / Z_SOCK_HOST
     *   Sock Server启动端口专用环境变量，如果未设置则选择配置文件中的环境变量
     * - Z_DBS_PORT / Z_DBS_HOST / Z_DBS_INSTANCE
     *   数据库专用端口环境变量，如果未设置则选择配置文件中的环境变量（三库同端口：标准库、工作流库、历史库）
     * - Z_DBW_PORT / Z_DBW_HOST / Z_DBW_INSTANCE
     *   工作流数据库专用环境变量，未设置同标准
     * - Z_DBH_PORT / Z_DBH_HOST / Z_DBH_INSTANCE
     *   历史数据库专用环境变量，未设置同标准
     */
    // 「Production」生产专用 ---------------------------------------
    String HED_COMPONENT = "Z_HED";                     // 外置 HED 模块
    String HED_ENABLED = "Z_HED_ENABLED";               // 是否打开 HED 模块
    String SIS_STORE = "Z_SIS_STORE";                   // 集成服务中的存储

    String CACHE_UI = "Z_CACHE_UI";                     // UI缓存
    String CACHE_ADMIT = "Z_CACHE_ADMIT";               // 安全管理缓存
    // 应用环境
    String CORS_DOMAIN = "Z_CORS_DOMAIN";               // 跨域配置（可支持多个，这个作为额外的添加）

    // RESTful 端口号/主机
    String API_PORT = "Z_API_PORT";
    String API_HOST = "Z_API_HOST";

    // WebSocket 端口号/主机
    String SOCK_PORT = "Z_SOCK_PORT";
    String SOCK_HOST = "Z_SOCK_HOST";

    // 数据库端口号/主机
    String DBS_PORT = "Z_DBS_PORT";
    String DBS_HOST = "Z_DBS_HOST";
    String DBS_INSTANCE = "Z_DBS_INSTANCE";

    // 工作流数据库端口号/主机
    String DBW_PORT = "Z_DBW_PORT";
    String DBW_HOST = "Z_DBW_HOST";
    String DBW_INSTANCE = "Z_DBW_INSTANCE";

    // 历史数据库端口号/主机
    String DBH_PORT = "Z_DBH_PORT";
    String DBH_HOST = "Z_DBH_HOST";
    String DBH_INSTANCE = "Z_DBH_INSTANCE";


    // 「Cloud」云端专用 ---------------------------------------
    /*
     * Aeon 系统启用时的核心的环境变量
     * 1. 系统环境变量
     * - AEON_CLOUD：       本地 vertx-zero-cloud 目录，启用 Aeon 系统时，系统会自动生成临时目录：/var/tmp/zero-aeon/kzero，
     *                      启动完成后，临时目录中的相关信息自动同步到环境变量工作目录中
     * - AEON_APP           原来的应用配置路径，用于处理应用程序配置程序加载专用
     *
     * - Z_APP              本地私库 xxx-app 目录，启用 Aeon 系统时，系统会自动生成临时目录：/var/tmp/zero-aeon/kinect，
     *                      启动完成后，临时目录中的相关信息自动同步到环境变量工作目录中标识符统一Z_SIGMA            名空间
     * * - 的在所该应用Z_NS
     * * -
     * - Z_LANG             当前环境使用的系统语言
     *
     */
    String AEON_CLOUD = "AEON_CLOUD";
    String AEON_APP = "AEON_APP";
    String Z_APP = "Z_APP";
    String Z_NS = "Z_NS";
    String Z_SIGMA = "Z_SIGMA";
    String Z_LANG = "Z_LANG";

    // For Zero
    String Z_PORT_DB = "Z_PORT_DB";

    // 环境变量打印专用
    static String envContent() {
        final StringBuilder content = new StringBuilder();
        final String[] VARS = new String[]{
            AEON_CLOUD, AEON_APP,               // 云端一阶变量
            Z_NS, Z_APP, Z_LANG, Z_SIGMA,       // 应用一阶变量
            CORS_DOMAIN,                        // 跨域
            API_HOST, API_PORT,                 // RESTful
            SOCK_HOST, SOCK_PORT,               // Sock
            DBS_HOST, DBS_PORT, DBS_INSTANCE,   // DB Service
            DBW_HOST, DBW_PORT, DBW_INSTANCE,   // DB Workflow
            DBH_HOST, DBH_PORT, DBH_INSTANCE,   // DB History
        };
        Arrays.stream(VARS).filter(Ut::notNil).forEach(name -> {
            final String value = System.getenv(name);
            content.append("\n\t").append(name).append(" = ").append(value);
        });
        return content.toString();
    }
}
