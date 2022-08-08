package io.vertx.aeon.eon;

/**
 * @author <a href="http://www.origin-x.cn">Lang</a>
 */
public interface HEnv {
    String ZERO_AEON = "ZERO_AEON";           // 基本环境变量
    String ZK_APP = "ZK_APP";                 // 应用环境变量

    String ZK_PASS = "ZK_PASS";               // 私库密钥
    String ZA_LANG = "ZA_LANG";               // 语言变量

    String[] REQUIRED = new String[]{
        ZERO_AEON,
        ZK_APP,
        ZA_LANG,
        ZK_PASS,
    };
}
