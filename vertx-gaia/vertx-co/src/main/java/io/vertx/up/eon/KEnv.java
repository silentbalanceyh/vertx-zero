package io.vertx.up.eon;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface KEnv {
    /*
     * Aeon 系统启用时的核心的环境变量
     * 1. 系统环境变量
     * - ZERO_AEON：        本地 vertx-zero-cloud 目录，启用 Aeon 系统时，系统会自动生成临时目录：/var/tmp/zero-aeon/kzero，
     *                      启动完成后，临时目录中的相关信息自动同步到环境变量工作目录中
     *
     * - ZK_APP             本地私库 xxx-app 目录，启用 Aeon 系统时，系统会自动生成临时目录：/var/tmp/zero-aeon/kinect，
     *                      启动完成后，临时目录中的相关信息自动同步到环境变量工作目录中
     *
     * - ZK_PASS            「操作系统配置」私有云部署时私库的密钥或token，最好配置在操作系统中以防止代码内部被盗窃
     * - ZK_LANG            当前环境使用的系统语言
     *
     * Zero 系统启动时对应核心环境变量
     * 2. 原始环境变量（新追加，方便协同开发）
     * - Z_PORT_WEB         Http Server启动端口专用环境变量，如果未设置则选择配置文件中的环境变量
     * - Z_PORT_SOCK        Sock Server启动端口专用环境变量，如果未设置则选择配置文件中的环境变量
     * - Z_PORT_DB          数据库专用端口环境变量，如果未设置则选择配置文件中的环境变量（三库同端口：标准库、工作流库、历史库）
     *
     * - Z_CORS_WEB         跨域访问专用环境变量（可设置前端跨域环境变量，方便后续协同开发）
     */
    String ZERO_AEON = "ZERO_AEON";
    String ZK_APP = "ZK_APP";
    String ZK_PASS = "ZK_PASS";
    String ZA_LANG = "ZA_LANG";

    // For Zero
    String Z_PORT_WEB = "Z_PORT_WEB";
    String Z_PORT_SOCK = "Z_PORT_SOCK";
    String Z_PORT_DB = "Z_PORT_DB";

    String[] REQUIRED = new String[]{
        ZERO_AEON,
        ZK_APP,
        ZA_LANG,
        ZK_PASS,
    };

    String Z_DEBUG_IO = "Z_DEBUG_IO";
}
